// Filename: AllProductESTests.java
package com.sparta.levelup_backend.domain.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sparta.levelup_backend.domain.product.document.ProductDocument;
import com.sparta.levelup_backend.utill.ProductStatus;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.SignificantStringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.SignificantStringTermsBucket;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;

// ====================== GetGenreAggregationsESTest ======================
@ExtendWith(MockitoExtension.class)
class GetGenreAggregationsESTest {

	public static class ProductService {
		private final ElasticsearchClient elasticsearchClient;

		public ProductService(ElasticsearchClient elasticsearchClient) {
			this.elasticsearchClient = elasticsearchClient;
		}

		public Map<String, Long> getGenreAggregationsES() {
			try {
				SearchRequest request = SearchRequest.of(s -> s
					.index("product")
					.size(0)
					.requestCache(true)
					.aggregations("genre_counts", a -> a.terms(t -> t.field("gameGenre").size(10)))
				);
				SearchResponse<Void> response = elasticsearchClient.search(request, Void.class);

				Aggregate aggregate = response.aggregations().get("genre_counts");
				if (aggregate == null || !aggregate.isSterms()) {
					throw new RuntimeException("Unexpected aggregation response");
				}
				StringTermsAggregate genreAggregation = aggregate.sterms();
				return genreAggregation.buckets().array().stream()
					.collect(Collectors.toMap(
						bucket -> bucket.key().stringValue(),
						bucket -> bucket.docCount()
					));
			} catch (IOException e) {
				throw new RuntimeException("Error executing aggregation query", e);
			}
		}
	}

	@Mock
	private ElasticsearchClient elasticsearchClient;

	@InjectMocks
	private ProductService productService;

	@Test
	void testGetGenreAggregationsES() throws IOException {
		StringTermsBucket mockBucket = mock(StringTermsBucket.class);
		when(mockBucket.key()).thenReturn(FieldValue.of("Action"));
		when(mockBucket.docCount()).thenReturn(10L);

		Buckets<StringTermsBucket> mockBuckets = mock(Buckets.class);
		when(mockBuckets.array()).thenReturn(List.of(mockBucket));

		StringTermsAggregate mockStringTermsAggregate = mock(StringTermsAggregate.class);
		when(mockStringTermsAggregate.buckets()).thenReturn(mockBuckets);

		Aggregate mockAggregate = mock(Aggregate.class);
		when(mockAggregate.isSterms()).thenReturn(true);
		when(mockAggregate.sterms()).thenReturn(mockStringTermsAggregate);

		@SuppressWarnings("unchecked")
		SearchResponse<Void> mockAggResponse = mock(SearchResponse.class);
		when(mockAggResponse.aggregations()).thenReturn(Collections.singletonMap("genre_counts", mockAggregate));

		when(elasticsearchClient.search(any(SearchRequest.class), eq(Void.class)))
			.thenReturn(mockAggResponse);

		Map<String, Long> result = productService.getGenreAggregationsES();
		assertNotNull(result);
		assertEquals(1, result.size());
		assertTrue(result.containsKey("Action"));
		assertEquals(10L, result.get("Action"));
		verify(elasticsearchClient).search(any(SearchRequest.class), eq(Void.class));
	}
}

// ====================== GetAllProductsESTest ======================
@ExtendWith(MockitoExtension.class)
class GetAllProductsESTest {

	public interface ProductESRepository {
		List<ProductDocument> findByIsDeletedFalseAndStatus(ProductStatus status);
	}

	public static class ProductService {
		private final ProductESRepository productESRepository;

		public ProductService(ProductESRepository productESRepository) {
			this.productESRepository = productESRepository;
		}

		public List<ProductDocument> getAllProductsES() {
			return productESRepository.findByIsDeletedFalseAndStatus(ProductStatus.ACTIVE);
		}
	}

	@Mock
	private ProductESRepository productESRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void testGetAllProductsES() {
		ProductDocument doc1 = ProductDocument.builder()
			.productId(1L)
			.productName("Product 1")
			.contents("Contents 1")
			.price(50L)
			.amount(5)
			.status(ProductStatus.ACTIVE)
			.imgUrl("img1.jpg")
			.userId(1L)
			.gameId(1L)
			.gameGenre("Action")
			.sentimentScore(0.8)
			.isDeleted(false)
			.build();
		ProductDocument doc2 = ProductDocument.builder()
			.productId(2L)
			.productName("Product 2")
			.contents("Contents 2")
			.price(70L)
			.amount(3)
			.status(ProductStatus.ACTIVE)
			.imgUrl("img2.jpg")
			.userId(2L)
			.gameId(2L)
			.gameGenre("Adventure")
			.sentimentScore(0.7)
			.isDeleted(false)
			.build();
		List<ProductDocument> productList = Arrays.asList(doc1, doc2);
		when(productESRepository.findByIsDeletedFalseAndStatus(ProductStatus.ACTIVE))
			.thenReturn(productList);
		List<ProductDocument> result = productService.getAllProductsES();
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(productESRepository).findByIsDeletedFalseAndStatus(ProductStatus.ACTIVE);
	}
}

// ====================== GetTop10PopularProductsESTest ======================
@ExtendWith(MockitoExtension.class)
class GetTop10PopularProductsESTest {

	public static class ProductService {
		private final ElasticsearchClient elasticsearchClient;

		public ProductService(ElasticsearchClient elasticsearchClient) {
			this.elasticsearchClient = elasticsearchClient;
		}

		public List<ProductDocument> getTop10PopularProductsES() {
			Map<String, Double> keywords = extractImportantKeywords(10);
			SearchRequest searchRequest = new SearchRequest.Builder()
				.index("product")
				.query(q -> q.bool(b -> {
					keywords.forEach((keyword, boost) ->
						b.should(s -> s.match(m -> m.field("contents").query(keyword).boost(boost.floatValue())))
					);
					return b;
				}))
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

		private Map<String, Double> extractImportantKeywords(int topN) {
			try {
				SearchRequest searchRequest = new SearchRequest.Builder()
					.index("product")
					.query(q -> q.bool(b -> b
						.must(m -> m.matchAll(ma -> ma))
						.filter(f -> f.term(t -> t.field("status").value("ACTIVE")))
						.filter(f -> f.term(t -> t.field("isDeleted").value(false)))
					))
					.aggregations("important_keywords",
						a -> a.significantTerms(st -> st.field("contents.keyword").size(topN)))
					.size(0)
					.build();
				SearchResponse<Void> searchResponse = elasticsearchClient.search(searchRequest, Void.class);
				Aggregate aggregate = searchResponse.aggregations().get("important_keywords");
				if (aggregate == null || !aggregate.isSigsterms()) {
					throw new RuntimeException("Unexpected aggregation type");
				}
				SignificantStringTermsAggregate sigAgg = aggregate.sigsterms();
				List<SignificantStringTermsBucket> buckets = sigAgg.buckets().array();
				Map<String, Double> keywordMap = new LinkedHashMap<>();
				for (SignificantStringTermsBucket bucket : buckets) {
					keywordMap.put(bucket.key(), bucket.score());
				}
				return keywordMap;
			} catch (IOException e) {
				throw new RuntimeException("Elasticsearch Aggregation 실패", e);
			}
		}
	}

	@Mock
	private ElasticsearchClient elasticsearchClient;

	@InjectMocks
	private ProductService productService;

	@Test
	void testGetTop10PopularProductsES() throws IOException {
		// Mock SignificantStringTermsBucket
		SignificantStringTermsBucket mockBucket = mock(SignificantStringTermsBucket.class);
		when(mockBucket.key()).thenReturn("popular");
		when(mockBucket.score()).thenReturn(2.0);
		// Mock Buckets<SignificantStringTermsBucket>
		Buckets<SignificantStringTermsBucket> mockBuckets = mock(Buckets.class);
		when(mockBuckets.array()).thenReturn(List.of(mockBucket));
		// Mock SignificantStringTermsAggregate
		SignificantStringTermsAggregate mockSigAgg = mock(SignificantStringTermsAggregate.class);
		when(mockSigAgg.buckets()).thenReturn(mockBuckets);
		// Mock Aggregate
		Aggregate agg = mock(Aggregate.class);
		when(agg.isSigsterms()).thenReturn(true);
		when(agg.sigsterms()).thenReturn(mockSigAgg);
		// Mock SearchResponse<Void>
		@SuppressWarnings("unchecked")
		SearchResponse<Void> mockAggResponse = mock(SearchResponse.class);
		when(mockAggResponse.aggregations()).thenReturn(Collections.singletonMap("important_keywords", agg));
		when(elasticsearchClient.search(any(SearchRequest.class), eq(Void.class)))
			.thenReturn(mockAggResponse);
		// 모의 ProductDocument 검색 결과
		ProductDocument doc = ProductDocument.builder()
			.productId(1L)
			.productName("Popular Product")
			.contents("popular content")
			.price(100L)
			.amount(1)
			.status(ProductStatus.ACTIVE)
			.imgUrl("popular.jpg")
			.userId(1L)
			.gameId(1L)
			.gameGenre("Action")
			.sentimentScore(0.9)
			.isDeleted(false)
			.build();
		@SuppressWarnings("unchecked")
		Hit<ProductDocument> hit = mock(Hit.class);
		when(hit.source()).thenReturn(doc);
		@SuppressWarnings("unchecked")
		HitsMetadata<ProductDocument> hitsMetadata = mock(HitsMetadata.class);
		when(hitsMetadata.hits()).thenReturn(Collections.singletonList(hit));
		@SuppressWarnings("unchecked")
		SearchResponse<ProductDocument> mockProductResponse = mock(SearchResponse.class);
		when(mockProductResponse.hits()).thenReturn(hitsMetadata);
		when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductDocument.class)))
			.thenReturn(mockProductResponse);
		// 메서드 실행 및 검증
		List<ProductDocument> result = productService.getTop10PopularProductsES();
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(1L, result.get(0).getProductId());
		verify(elasticsearchClient, times(2)).search(any(SearchRequest.class), any());
	}
}

// ====================== GetProductByIdESTest ======================
@ExtendWith(MockitoExtension.class)
class GetProductByIdESTest {

	public interface ProductESRepository {
		ProductDocument findByProductIdAndIsDeletedFalseAndStatus(Long id, ProductStatus status);
	}

	public static class ProductService {
		private final ProductESRepository productESRepository;

		public ProductService(ProductESRepository productESRepository) {
			this.productESRepository = productESRepository;
		}

		public ProductDocument getProductByIdES(Long id) {
			return productESRepository.findByProductIdAndIsDeletedFalseAndStatus(id, ProductStatus.ACTIVE);
		}
	}

	@Mock
	private ProductESRepository productESRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void testGetProductByIdES() {
		Long productId = 1L;
		ProductDocument doc = ProductDocument.builder()
			.productId(productId)
			.productName("Test Product")
			.contents("Test Contents")
			.price(100L)
			.amount(10)
			.status(ProductStatus.ACTIVE)
			.imgUrl("test.jpg")
			.userId(1L)
			.gameId(1L)
			.gameGenre("Action")
			.sentimentScore(0.9)
			.isDeleted(false)
			.build();
		when(productESRepository.findByProductIdAndIsDeletedFalseAndStatus(productId, ProductStatus.ACTIVE))
			.thenReturn(doc);
		ProductDocument result = productService.getProductByIdES(productId);
		assertNotNull(result);
		assertEquals(productId, result.getProductId());
		verify(productESRepository).findByProductIdAndIsDeletedFalseAndStatus(productId, ProductStatus.ACTIVE);
	}
}

// ====================== SearchByGameIdESTest ======================
@ExtendWith(MockitoExtension.class)
class SearchByGameIdESTest {

	public interface ProductESRepository {
		List<ProductDocument> findByGameIdAndIsDeletedFalseAndStatus(Long gameId, ProductStatus status);
	}

	public static class ProductService {
		private final ProductESRepository productESRepository;

		public ProductService(ProductESRepository productESRepository) {
			this.productESRepository = productESRepository;
		}

		public List<ProductDocument> searchByGameIdES(Long gameId) {
			return productESRepository.findByGameIdAndIsDeletedFalseAndStatus(gameId, ProductStatus.ACTIVE);
		}
	}

	@Mock
	private ProductESRepository productESRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void testSearchByGameIdES() {
		Long gameId = 100L;
		ProductDocument doc = ProductDocument.builder()
			.productId(10L)
			.productName("Game Product")
			.contents("Game content")
			.price(200L)
			.amount(2)
			.status(ProductStatus.ACTIVE)
			.imgUrl("game.jpg")
			.userId(2L)
			.gameId(gameId)
			.gameGenre("RPG")
			.sentimentScore(0.85)
			.isDeleted(false)
			.build();
		List<ProductDocument> productList = Collections.singletonList(doc);
		when(productESRepository.findByGameIdAndIsDeletedFalseAndStatus(gameId, ProductStatus.ACTIVE))
			.thenReturn(productList);
		List<ProductDocument> result = productService.searchByGameIdES(gameId);
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(productESRepository).findByGameIdAndIsDeletedFalseAndStatus(gameId, ProductStatus.ACTIVE);
	}
}

// ====================== SearchByStatusESTest ======================
@ExtendWith(MockitoExtension.class)
class SearchByStatusESTest {

	public interface ProductESRepository {
		List<ProductDocument> findByStatusAndIsDeletedFalse(String status, ProductStatus activeStatus);
	}

	public static class ProductService {
		private final ProductESRepository productESRepository;

		public ProductService(ProductESRepository productESRepository) {
			this.productESRepository = productESRepository;
		}

		public List<ProductDocument> searchByStatusES(String status) {
			return productESRepository.findByStatusAndIsDeletedFalse(status, ProductStatus.ACTIVE);
		}
	}

	@Mock
	private ProductESRepository productESRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void testSearchByStatusES() {
		String status = "ACTIVE";
		ProductDocument doc = ProductDocument.builder()
			.productId(3L)
			.productName("Status Product")
			.contents("Status content")
			.price(150L)
			.amount(7)
			.status(ProductStatus.ACTIVE)
			.imgUrl("status.jpg")
			.userId(3L)
			.gameId(3L)
			.gameGenre("Strategy")
			.sentimentScore(0.88)
			.isDeleted(false)
			.build();
		List<ProductDocument> productList = Collections.singletonList(doc);
		when(productESRepository.findByStatusAndIsDeletedFalse(status, ProductStatus.ACTIVE))
			.thenReturn(productList);
		List<ProductDocument> result = productService.searchByStatusES(status);
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(productESRepository).findByStatusAndIsDeletedFalse(status, ProductStatus.ACTIVE);
	}
}

// ====================== SearchByProductNameESTest ======================
@ExtendWith(MockitoExtension.class)
class SearchByProductNameESTest {

	public static class ProductService {
		private final ElasticsearchClient elasticsearchClient;

		public ProductService(ElasticsearchClient elasticsearchClient) {
			this.elasticsearchClient = elasticsearchClient;
		}

		public List<ProductDocument> searchByProductNameES(String productName) {
			SearchRequest request = SearchRequest.of(s -> s
				.index("product")
				.query(q -> q.matchPhrasePrefix(m -> m.field("productName").query(productName)))
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
	}

	@Mock
	private ElasticsearchClient elasticsearchClient;

	@InjectMocks
	private ProductService productService;

	@Test
	void testSearchByProductNameES() throws IOException {
		String productName = "TestProduct";
		ProductDocument doc = ProductDocument.builder()
			.productId(1L)
			.productName(productName)
			.contents("Some content")
			.price(100L)
			.amount(5)
			.status(ProductStatus.ACTIVE)
			.imgUrl("img.jpg")
			.userId(1L)
			.gameId(1L)
			.gameGenre("Action")
			.sentimentScore(0.95)
			.isDeleted(false)
			.build();
		@SuppressWarnings("unchecked")
		Hit<ProductDocument> hit = mock(Hit.class);
		when(hit.source()).thenReturn(doc);
		@SuppressWarnings("unchecked")
		HitsMetadata<ProductDocument> hitsMetadata = mock(HitsMetadata.class);
		when(hitsMetadata.hits()).thenReturn(Collections.singletonList(hit));
		@SuppressWarnings("unchecked")
		SearchResponse<ProductDocument> mockResponse = mock(SearchResponse.class);
		when(mockResponse.hits()).thenReturn(hitsMetadata);
		when(elasticsearchClient.search(any(SearchRequest.class), eq(ProductDocument.class)))
			.thenReturn(mockResponse);
		List<ProductDocument> result = productService.searchByProductNameES(productName);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(productName, result.get(0).getProductName());
	}
}

// ====================== SearchByUserIdESTest ======================
@ExtendWith(MockitoExtension.class)
class SearchByUserIdESTest {

	public interface ProductESRepository {
		List<ProductDocument> findByUserIdAndIsDeletedFalseAndStatus(Long userId, ProductStatus status);
	}

	public static class ProductService {
		private final ProductESRepository productESRepository;

		public ProductService(ProductESRepository productESRepository) {
			this.productESRepository = productESRepository;
		}

		public List<ProductDocument> searchByUserIdES(Long userId) {
			return productESRepository.findByUserIdAndIsDeletedFalseAndStatus(userId, ProductStatus.ACTIVE);
		}
	}

	@Mock
	private ProductESRepository productESRepository;

	@InjectMocks
	private ProductService productService;

	@Test
	void testSearchByUserIdES() {
		Long userId = 200L;
		ProductDocument doc = ProductDocument.builder()
			.productId(4L)
			.productName("User Product")
			.contents("User content")
			.price(180L)
			.amount(4)
			.status(ProductStatus.ACTIVE)
			.imgUrl("user.jpg")
			.userId(userId)
			.gameId(4L)
			.gameGenre("Puzzle")
			.sentimentScore(0.92)
			.isDeleted(false)
			.build();
		List<ProductDocument> productList = Collections.singletonList(doc);
		when(productESRepository.findByUserIdAndIsDeletedFalseAndStatus(userId, ProductStatus.ACTIVE))
			.thenReturn(productList);
		List<ProductDocument> result = productService.searchByUserIdES(userId);
		assertNotNull(result);
		assertEquals(1, result.size());
		verify(productESRepository).findByUserIdAndIsDeletedFalseAndStatus(userId, ProductStatus.ACTIVE);
	}
}
