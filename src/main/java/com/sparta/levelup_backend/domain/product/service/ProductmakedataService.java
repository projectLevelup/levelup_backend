package com.sparta.levelup_backend.domain.product.service;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repository.GameRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.utill.ProductStatus;
import com.sparta.levelup_backend.utill.UserRole;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductmakedataService {

	private final ProductRepository productRepository;
	private final GameRepository gameRepository;
	private final UserRepository userRepository;
	private static final Random random = new Random();

	@Transactional
	public void generateUsers(int totalUsers) {
		IntStream.range(0, totalUsers).forEach(i -> {
			String uniqueEmail;
			do {
				uniqueEmail = "user" + random.nextInt(10000) + "@example.com";
			} while (userRepository.existsByEmail(uniqueEmail)); // 중복된 이메일이면 다시 생성

			UserEntity user = UserEntity.builder()
				.email(uniqueEmail)
				.nickName("사용자" + i)
				.password("password123")
				.phoneNumber("010-1234-" + String.format("%04d", i))
				.role(UserRole.USER)
				.imgUrl(null)
				.build();

			userRepository.save(user);
		});

		System.out.println(totalUsers + "명의 유저 데이터가 생성되었습니다.");
	}

	@Transactional
	public void generateGames(int totalGames) {
		List<UserEntity> users = userRepository.findAll();
		if (users.isEmpty()) {
			throw new IllegalStateException("유저 데이터가 존재하지 않습니다. 유저 데이터를 먼저 생성하세요.");
		}

		IntStream.range(0, totalGames).forEach(i -> {
			UserEntity randomUser = users.get(random.nextInt(users.size())); // 랜덤 유저 선택

			GameEntity game = GameEntity.builder()
				.name("게임" + i)
				.imgUrl("https://example.com/game" + i + ".jpg")
				.genre(generateRandomGameGenre()) // 랜덤 장르 생성
				.user(randomUser) // 랜덤 유저 할당
				.build();

			gameRepository.save(game);
		});

		System.out.println(totalGames + "개의 게임 데이터가 삽입되었습니다.");
	}

	@Transactional
	public void generateProducts(int totalRecords) {
		List<UserEntity> users = userRepository.findAll();
		List<GameEntity> games = gameRepository.findAll();

		if (users.isEmpty() || games.isEmpty()) {
			throw new IllegalStateException("유저 또는 게임 데이터가 없습니다. 먼저 유저와 게임 데이터를 생성하세요.");
		}

		IntStream.range(0, totalRecords).forEach(i -> {
			UserEntity randomUser = users.get(random.nextInt(users.size())); // 랜덤 유저 선택
			GameEntity randomGame = games.get(random.nextInt(games.size())); // 랜덤 게임 선택

			ProductEntity product = ProductEntity.builder()
				.user(randomUser)
				.game(randomGame)
				.productName(generateRandomProductName())
				.contents("설명 " + i)
				.price((long)(10000 + (random.nextInt(100) * 1000)))
				.amount(random.nextInt(100) + 1)
				.status(random.nextBoolean() ? ProductStatus.ACTIVE : ProductStatus.INACTIVE)
				.imgUrl(null)
				.build();

			productRepository.save(product);
		});

		System.out.println(totalRecords + "개의 제품 데이터가 삽입되었습니다.");
	}

	/** ✅ 랜덤 게임 장르 생성 */
	private static String generateRandomGameGenre() {
		String[] genres = {"RPG", "FPS", "스포츠", "퍼즐", "레이싱", "전략", "MMORPG", "시뮬레이션"};
		return genres[random.nextInt(genres.length)];
	}

	/** ✅ 랜덤 유저 역할 생성 */
	private static UserRole generateRandomUserRole() {
		return random.nextBoolean() ? UserRole.USER : UserRole.ADMIN;
	}

	/** ✅ 랜덤 전화번호 생성 */
	private static String generateRandomPhoneNumber() {
		return "010-" + (random.nextInt(9000) + 1000) + "-" + (random.nextInt(9000) + 1000);
	}

	/** ✅ 랜덤 상품명 생성 */
	private static String generateRandomProductName() {
		String[] WORDS1 = {"스위치", "플레이스테이션", "엑스박스", "닌텐도", "PC", "스팀덱", "애플", "삼성", "LG", "레노버"};
		String[] WORDS2 = {"인기", "한정판", "특가", "세일", "베스트셀러", "최신", "업그레이드", "프리미엄", "가성비", "한정수량"};
		String[] WORDS3 = {"판매", "예약", "중고", "신상품", "할인", "정품", "리퍼", "체험판", "렌탈", "사전예약"};

		return WORDS1[random.nextInt(WORDS1.length)] + " " +
			WORDS2[random.nextInt(WORDS2.length)] + " " +
			WORDS3[random.nextInt(WORDS3.length)];
	}
}
