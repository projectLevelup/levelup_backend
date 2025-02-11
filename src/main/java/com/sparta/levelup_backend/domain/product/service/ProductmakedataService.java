package com.sparta.levelup_backend.domain.product.service;

import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.utill.ProductStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductmakedataService {

	private static final ProductRepository productRepository = null;
	private static final Random random = new Random();

	@Transactional
	public void generateProducts(int totalRecords) {
		IntStream.range(0, totalRecords).forEach(i -> {
			ProductEntity product = ProductEntity.builder()
				.user(UserEntity.builder().id((long) (random.nextInt(1000) + 1)).build())
				.game(GameEntity.builder().id((long) (random.nextInt(500) + 1)).build())
				.productName(generateRandomProductName())
				.contents("설명 " + i)
				.price((long)(10000 + (random.nextInt(100) * 1000)))
				.amount(random.nextInt(100) + 1)
				.status(random.nextBoolean() ? ProductStatus.ACTIVE : ProductStatus.DELETED)
				.imgUrl(null)
				.build();

			productRepository.save(product);
		});

		System.out.println(totalRecords + "개의 제품 데이터가 삽입되었습니다.");
	}

	private static String generateRandomProductName() {
		String[] WORDS1 = {"스위치", "플레이스테이션", "엑스박스", "닌텐도", "PC"};
		String[] WORDS2 = {"인기", "한정판", "특가", "세일", "베스트셀러"};
		String[] WORDS3 = {"판매", "예약", "중고", "신상품", "할인"};

		return WORDS1[random.nextInt(WORDS1.length)] + " " +
			WORDS2[random.nextInt(WORDS2.length)] + " " +
			WORDS3[random.nextInt(WORDS3.length)];
	}
}
