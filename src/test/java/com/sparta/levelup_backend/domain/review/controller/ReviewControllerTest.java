package com.sparta.levelup_backend.domain.review.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.levelup_backend.common.ApiResMessage;
import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.repsitory.GameRepository;
import com.sparta.levelup_backend.domain.product.entity.ProductEntity;
import com.sparta.levelup_backend.domain.product.repository.ProductRepository;
import com.sparta.levelup_backend.domain.review.dto.request.ReviewRequestDto;
import com.sparta.levelup_backend.domain.review.dto.response.ReviewResponseDto;
import com.sparta.levelup_backend.domain.review.repository.ReviewRepository;
import com.sparta.levelup_backend.domain.user.entity.UserEntity;
import com.sparta.levelup_backend.domain.user.repository.UserRepository;
import com.sparta.levelup_backend.utill.ProductStatus;
import com.sparta.levelup_backend.utill.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReviewControllerTest {

    private static final String BASE_URL = "/v1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private UserEntity user;
    private ProductEntity product;
    private GameEntity game;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
            .email("test@test.com")
            .nickName("홍길동")
            .password("Test1234@@")
            .role(UserRole.USER)
            .build();

        userRepository.save(user);

        game = GameEntity.builder()
            .name("LOL")
            .gameImageUrl("url")
            .genre("test")
            .user(user)
            .build();

        gameRepository.save(game);

        product = ProductEntity.builder()
            .user(user)
            .game(game)
            .productName("상품명")
            .contents("상품 설명")
            .price(10000L)
            .status(ProductStatus.INACTIVE)
            .build();

        productRepository.save(product);
    }

    @AfterEach
    void reset() {
    }

    @Test
    void reviewSave() throws Exception {
        //given
        ReviewRequestDto request = new ReviewRequestDto("선생님 좋아용!", 5);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                post("/v1/products/{productId}/reviews", product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();

        ApiResponse<ReviewResponseDto> actualApiResponse = this.objectMapper.readValue(response.getContentAsString(), new TypeReference<ApiResponse<ReviewResponseDto>>() {});
        ReviewResponseDto actualResult = actualApiResponse.getData();

        //then
        ReviewResponseDto expectedDto = new ReviewResponseDto(
            actualResult.getReviewId(),
            product.getId(),
            user.getId(),
            request.getContents(),
            request.getStarScore());

        ApiResponse<ReviewResponseDto> expectedResult = ApiResponse.success(ApiResMessage.REVIEW_SUCCESS, expectedDto);

        assertThat(actualApiResponse).usingRecursiveComparison().isEqualTo(expectedResult);

    }
}