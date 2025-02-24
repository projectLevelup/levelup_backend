package com.sparta.levelup_backend.domain.product.service;

import static com.sparta.levelup_backend.exception.common.ErrorCode.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openkoreantext.processor.OpenKoreanTextProcessorJava;
import org.openkoreantext.processor.tokenizer.KoreanTokenizer;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductCreateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductRequestAllDto;
import com.sparta.levelup_backend.domain.product.dto.requestDto.ProductUpdateRequestDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductCreateResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductDeleteResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductResponseDto;
import com.sparta.levelup_backend.domain.product.dto.responseDto.ProductUpdateResponseDto;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.product.repositoryES.ProductESRepository;
import com.sparta.levelup_backend.domain.review.document.ReviewDocument;
import com.sparta.levelup_backend.domain.review.repositoryES.ReviewESRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.exception.common.DuplicateException;
import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import com.sparta.levelup_backend.utill.ProductStatus;
import com.sparta.levelup_backend.utill.UserRole;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import co.elastic.clients.elasticsearch._types.aggregations.SignificantStringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.SignificantStringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.UpdateRequest;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import scala.collection.JavaConverters;
import scala.collection.Seq;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final GameRepository gameRepository;
	private final ProductESRepository productESRepository;
	private final ElasticsearchClient elasticsearchClient;
	private final RedissonClient redissonClient;
	private final ReviewESRepository reviewESRepository;

	/**
	 * 모든 활성화된 상품 정보를 조회합니다.
	 * @return 활성화된 상품들의 리스트(ProductResponseDto)
	 */
	@Override
	public List<ProductResponseDto> getAllProducts() {
		return productRepository.findAllByIsDeletedFalseAndStatus(ProductStatus.ACTIVE)
			.stream()
			.map(ProductResponseDto::new)
			.collect(Collectors.toList());
	}

	/**
	 * 특정 상품을 ID로 조회합니다.
	 * 소유주나 관리자일 경우 비활성화 상품도 조회 가능합니다.
	 * @param id 상품 ID
	 * @param userId 사용자 ID
	 * @return 조회된 상품의 상세 정보(ProductResponseDto)
	 */
	@Override
	public ProductResponseDto getProductById(Long id, Long userId) {
		ProductEntity product = productRepository.findByIdOrElseThrow(id);
		if (product.getIsDeleted()) {
			throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
		}
		if (isOwner(product, userId) || isAdmin(userId)) {
			return new ProductResponseDto(product);
		}

		if (product.getStatus() != ProductStatus.ACTIVE) {
			throw new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
		}
		return new ProductResponseDto(product);
	}

	/**
	 * 상품을 새로 등록합니다.
	 * @param userId 사용자 ID
	 * @param dto 상품 생성 요청 DTO
	 * @return 생성된 상품에 대한 응답(ProductCreateResponseDto)
	 */
	@Transactional
	@Override
	public ProductCreateResponseDto saveProduct(Long userId, ProductCreateRequestDto dto) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		GameEntity game = gameRepository.findByIdOrElseThrow(dto.getGameId());
		ProductEntity product = new ProductEntity(dto, user, game);
		ProductEntity savedProduct = productRepository.save(product);
		ProductDocument document = ProductDocument.fromEntity(savedProduct);
		productESRepository.save(document);
		return new ProductCreateResponseDto(document);
	}

	/**
	 * 기존 상품 정보를 업데이트합니다.
	 * 분산 락으로 중복 수정 방지를 처리합니다.
	 * @param id 상품 ID
	 * @param userId 사용자 ID
	 * @param requestDto 업데이트 요청 DTO
	 * @return 업데이트된 상품 정보(ProductUpdateResponseDto)
	 */
	@Transactional
	@Override
	public ProductUpdateResponseDto updateProduct(Long id, Long userId, ProductUpdateRequestDto requestDto) {
		RLock lock = redissonClient.getLock("stock_lock_" + id);
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		ProductEntity saveProduct = null;
		ProductDocument updatedDocument = null;
		try {
			boolean available = lock.tryLock(1, 10, TimeUnit.SECONDS);
			if (!available) {
				throw new RuntimeException("Lock acquisition failed");
			}
			ProductEntity product = getFindByIdWithLock(id);
			if (!isOwner(product, userId) && !isAdmin(userId)) {
				throw new DuplicateException(FORBIDDEN_ACCESS);
			}
			product.update(requestDto);
			saveProduct = productRepository.save(product);
			updatedDocument = ProductDocument.fromEntity(saveProduct);
			productESRepository.save(updatedDocument);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Thread interrupted during locking", e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
		return new ProductUpdateResponseDto(updatedDocument);
	}

	/**
	 * 상품을 삭제 처리합니다.
	 * 실제 데이터는 물리 삭제되지 않고 isDeleted 플래그를 변경합니다.
	 * @param id 상품 ID
	 * @param userId 사용자 ID
	 * @return 삭제 처리된 상품 정보(ProductDeleteResponseDto)
	 */
	@Transactional
	@Override
	public ProductDeleteResponseDto deleteProduct(Long id, Long userId) {
		ProductEntity product = productRepository.findByIdOrElseThrow(id);
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		if (product.getIsDeleted()) {
			throw new DuplicateException(PRODUCT_ISDELETED);
		}
		if (!product.getUser().getId().equals(user.getId()) && !user.getRole().equals(UserRole.ADMIN)) {
			throw new DuplicateException(FORBIDDEN_ACCESS);
		}

		ProductDocument document = ProductDocument.fromEntity(product);
		document.updateIsDeleted(true);
		productESRepository.save(document);
		return new ProductDeleteResponseDto(document);
	}

	/**
	 * Elasticsearch에서 상품을 ID로 조회합니다.
	 * 캐시를 적용하여 자주 조회되는 상품에 대해 성능을 향상시킵니다.
	 * @param id 상품 ID
	 * @return 조회된 상품의 Elasticsearch 문서(ProductDocument)
	 */
	@Cacheable(value = "product", key = "#productId")
	public ProductDocument getProductByIdES(Long id) {
		return productESRepository.findByProductIdAndIsDeletedFalseAndStatus(id, ProductStatus.ACTIVE);
	}

	/**
	 * Elasticsearch에서 활성화된 모든 상품을 조회합니다.
	 * 캐시를 적용하여 자주 조회되는 상품에 대해 성능을 향상시킵니다.
	 * @return 활성화된 상품의 Elasticsearch 문서 목록(List<ProductDocument>)
	 */
	@Cacheable(value = "product")
	public List<ProductDocument> getAllProductsES() {
		return productESRepository.findByIsDeletedFalseAndStatus(ProductStatus.ACTIVE);
	}

	/**
	 * Elasticsearch에서 상품명을 기준으로 검색합니다.
	 * @param productName 검색할 상품명
	 * @return 검색된 상품 문서 목록(List<ProductDocument>)
	 */
	public List<ProductDocument> searchByProductNameES(String productName) {
		SearchRequest request = SearchRequest.of(s -> s
			.index("product")
			.query(q -> q
				.matchPhrasePrefix(m -> m
					.field("productName")
					.query(productName)
				)
			)
		);

		SearchResponse<ProductDocument> response;
		try {
			response = elasticsearchClient.search(request, ProductDocument.class);
		} catch (IOException e) {
			throw new RuntimeException("Elasticsearch 검색 실패", e);
		}

		return response.hits().hits().stream()
			.map(Hit::source)
			.collect(Collectors.toList());
	}

	/**
	 * Elasticsearch에서 특정 게임 ID에 해당하는 활성화된 상품을 조회합니다.
	 * 캐시를 적용하여 성능을 향상시킵니다.
	 * @param gameId 게임 ID
	 * @return 해당 게임의 상품 문서 목록(List<ProductDocument>)
	 */
	@Cacheable(value = "product", key = "#gameId")
	public List<ProductDocument> searchByGameIdES(Long gameId) {
		return productESRepository.findByGameIdAndIsDeletedFalseAndStatus(gameId, ProductStatus.ACTIVE);
	}

	/**
	 * Elasticsearch에서 특정 상태의 상품을 조회합니다.
	 * 캐시를 적용하여 성능을 향상시킵니다.
	 * @param status 상품 상태
	 * @return 해당 상태의 상품 문서 목록(List<ProductDocument>)
	 */
	@Cacheable(value = "product", key = "#status")
	public List<ProductDocument> searchByStatusES(String status) {
		return productESRepository.findByStatusAndIsDeletedFalse(status, ProductStatus.ACTIVE);
	}

	/**
	 * Elasticsearch에서 특정 사용자가 등록한 상품을 조회합니다.
	 * 캐시를 적용하여 성능을 향상시킵니다.
	 * @param userId 사용자 ID
	 * @return 해당 사용자가 등록한 활성화된 상품 목록(List<ProductDocument>)
	 */
	@Cacheable(value = "product", key = "#userId")
	public List<ProductDocument> searchByUserIdES(Long userId) {
		return productESRepository.findByUserIdAndIsDeletedFalseAndStatus(userId, ProductStatus.ACTIVE);
	}

	/**
	 * Elasticsearch에서 장르별 상품 개수를 집계합니다.
	 * 캐시를 사용하여 자주 쓰이는 통계를 빠르게 조회합니다.
	 * @return 장르별 상품 개수를 담은 Map<String, Long>
	 */
	@Cacheable(value = "product_aggregations", key = "'genre_counts'")
	public Map<String, Long> getGenreAggregationsES() {
		try {
			SearchRequest request = SearchRequest.of(s -> s
				.index("product")
				.size(0)
				.requestCache(true)
				.aggregations("genre_counts", a -> a
					.terms(t -> t.field("gameGenre").size(10))
				)
			);

			SearchResponse<Void> response = elasticsearchClient.search(request, Void.class);

			StringTermsAggregate genreAggregation = response.aggregations()
				.get("genre_counts")
				.sterms();

			return genreAggregation.buckets().array().stream()
				.collect(Collectors.toMap(
					bucket -> bucket.key().stringValue(),
					MultiBucketBase::docCount
				));
		} catch (IOException e) {
			throw new RuntimeException("Error executing aggregation query", e);
		}
	}

	/**
	 * 특정 키워드를 기반으로 인기 상품 Top 10을 Elasticsearch에서 조회합니다.
	 * 의미 있는 키워드를 추출해 검색 가중치로 사용합니다.
	 * @return 인기 상품 상위 10개(List<ProductDocument>)
	 */
	@Override
	public List<ProductDocument> getTop10PopularProductsES() {
		Map<String, Double> keywords = extractImportantKeywords(10);
		BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
		for (Map.Entry<String, Double> entry : keywords.entrySet()) {
			String keyword = entry.getKey();
			float boost = entry.getValue().floatValue();
			boolQueryBuilder.should(q -> q
				.match(m -> m
					.field("contents")
					.query(keyword)
					.boost(boost)
				)
			);
		}
		SearchRequest searchRequest = new SearchRequest.Builder()
			.index("product")
			.query(q -> q.bool(boolQueryBuilder.build()))
			.from(0)
			.size(10)
			.build();
		try {
			SearchResponse<ProductDocument> searchResponse =
				elasticsearchClient.search(searchRequest, ProductDocument.class);
			return searchResponse.hits().hits().stream()
				.map(Hit::source)
				.collect(Collectors.toList());
		} catch (IOException e) {
			System.err.println("Elasticsearch 검색 중 오류 발생: " + e.getMessage());
			throw new RuntimeException("Elasticsearch 검색 실패", e);
		}
	}

	/**
	 * Elasticsearch를 이용해 중요 키워드를 추출합니다.
	 * @param topN 추출할 키워드 개수
	 * @return 키워드와 가중치를 담은 Map<String, Double>
	 */
	private Map<String, Double> extractImportantKeywords(int topN) {
		Aggregation significantTermsAgg = Aggregation.of(a -> a
			.significantTerms(st -> st
				.field("contents.keyword")
				.size(topN)
			)
		);

		SearchRequest searchRequest = new SearchRequest.Builder()
			.index("product")
			.query(q -> q.bool(b -> b
				.must(m -> m.matchAll(ma -> ma))
				.filter(f -> f.term(t -> t.field("status").value(ProductStatus.ACTIVE.name())))
				.filter(f -> f.term(t -> t.field("isDeleted").value(false)))
			))
			.aggregations("important_keywords", significantTermsAgg)
			.size(0)
			.build();

		try {
			SearchResponse<Void> searchResponse = elasticsearchClient.search(searchRequest, Void.class);
			Aggregate aggregate = searchResponse.aggregations().get("important_keywords");

			if (aggregate.isSigsterms()) {
				SignificantStringTermsAggregate significantTermsAggResult = aggregate.sigsterms();
				if (significantTermsAggResult == null) {
					throw new IllegalStateException("Significant terms aggregation result is null");
				}

				Map<String, Double> keywordMap = new LinkedHashMap<>();
				if (significantTermsAggResult.buckets() != null) {
					List<SignificantStringTermsBucket> buckets = significantTermsAggResult.buckets().array();
					for (SignificantStringTermsBucket bucket : buckets) {
						keywordMap.put(bucket.key(), bucket.score());
					}
				}
				return keywordMap;
			} else {
				throw new RuntimeException(
					"잘못된 Aggregation 타입: expected SignificantStringTermsAggregate but got " + aggregate.getClass()
						.getSimpleName());
			}
		} catch (IOException e) {
			System.err.println("Elasticsearch Aggregation 실행 중 오류 발생: " + e.getMessage());
			throw new RuntimeException("Elasticsearch Aggregation 실패", e);
		}
	}

	/**
	 * Product의 평균 감성 점수 계산 후 Elasticsearch에 업데이트
	 */
	public void updateProductSentimentScores() {
		List<ProductDocument> products = productESRepository.findAllByIsDeletedFalseAndStatus(ProductStatus.ACTIVE);

		for (ProductDocument product : products) {
			List<ReviewDocument> reviews = reviewESRepository.findByProductId(product.getProductId());

			double totalScore = 0.0;
			int count = 0;

			for (ReviewDocument review : reviews) {
				double sentimentScore = analyzeSentiment(review.getContents());
				totalScore += sentimentScore;
				count++;
			}

			double averageScore = (count > 0) ? totalScore / count : 0.0;

			try {
				UpdateResponse<ProductDocument> response = elasticsearchClient.update(UpdateRequest.of(u -> u
					.index("product")
					.id(String.valueOf(product.getProductId()))
					.doc(ProductDocument.builder().sentimentScore(averageScore).build())
				), ProductDocument.class);

				if (response.result().name().equalsIgnoreCase("not_found")) {
					System.err.println("ProductDocument not found in Elasticsearch: " + product.getProductId());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 30분마다 상품 감성 분석 점수 업데이트 (자동 실행)
	 */
	@Scheduled(cron = "0 */30 * * * *") // 30분마다 실행
	public void scheduledUpdateProductSentimentScores() {
		updateProductSentimentScores();
	}

	/**
	 * 감성 점수 TOP 3 제품 반환
	 */
	public List<ProductRequestAllDto> getTop3Products() {
		updateProductSentimentScores();

		List<ProductDocument> productList = productESRepository.findAllByIsDeletedFalseAndStatus(ProductStatus.ACTIVE);

		return productList.stream()
			.sorted(Comparator.comparingDouble(ProductDocument::getSentimentScore).reversed())
			.limit(3)
			.map(ProductRequestAllDto::fromDocument)
			.toList();
	}

	/**
	 * 간단한 감성 분석을 수행합니다.
	 * 토큰화 후 긍정/부정 단어, 부정어, 강조어 등을 분석하여 점수를 계산합니다.
	 * @param content 리뷰 내용
	 * @return 감성 점수
	 */
	private double analyzeSentiment(String content) {
		CharSequence normalized = OpenKoreanTextProcessorJava.normalize(content);
		Seq<KoreanTokenizer.KoreanToken> tokens = OpenKoreanTextProcessorJava.tokenize(normalized);
		List<KoreanTokenizer.KoreanToken> tokenList = JavaConverters.seqAsJavaList(tokens);
		List<String> positiveWords = Arrays.asList("좋다", "최고", "만족", "훌륭", "감사");
		List<String> negativeWords = Arrays.asList("나쁘다", "별로", "실망", "최악", "불만");
		List<String> negationWords = Arrays.asList("안", "못", "전혀");
		List<String> intensifiers = Arrays.asList("매우", "아주", "정말");
		double score = 0.0;
		for (int i = 0; i < tokenList.size(); i++) {
			String word = tokenList.get(i).text();
			boolean isNegated = false;
			double multiplier = 1.0;
			if (i > 0 && negationWords.contains(tokenList.get(i - 1).text())) {
				isNegated = true;
			}
			if (i > 0 && intensifiers.contains(tokenList.get(i - 1).text())) {
				multiplier = 2.0;
			}
			if (positiveWords.contains(word)) {
				score += isNegated ? -1 * multiplier : 1 * multiplier;
			} else if (negativeWords.contains(word)) {
				score += isNegated ? 1 * multiplier : -1 * multiplier;
			}
		}
		return score;
	}

	private boolean isAdmin(Long userId) {
		UserEntity user = userRepository.findByIdOrElseThrow(userId);
		return user.getRole().equals(UserRole.ADMIN);
	}

	private boolean isOwner(ProductEntity product, Long userId) {
		return product.getUser().getId().equals(userId);
	}

	@Transactional
	public ProductEntity getFindByIdWithLock(Long productId) {
		return productRepository.findByIdWithLock(productId)
			.orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND));
	}

	public ProductEntity findById(Long productId) {
		return productRepository.findByIdOrElseThrow(productId);
	}
}
