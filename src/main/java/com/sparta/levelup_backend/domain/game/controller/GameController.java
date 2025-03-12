package com.sparta.levelup_backend.domain.game.controller;

import static com.sparta.levelup_backend.common.apiresponse.ApiResMessage.*;
import static com.sparta.levelup_backend.common.apiresponse.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.levelup_backend.common.apiresponse.ApiResponse;
import com.sparta.levelup_backend.common.security.CustomUserDetails;
import com.sparta.levelup_backend.domain.game.dto.requestDto.GameCreateRequestDto;
import com.sparta.levelup_backend.domain.game.dto.requestDto.GameUpdateRequestDto;
import com.sparta.levelup_backend.domain.game.dto.responseDto.GameListResponseDto;
import com.sparta.levelup_backend.domain.game.dto.responseDto.GameResponseDto;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.service.GameService;
import com.sparta.levelup_backend.domain.s3.service.S3Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GameController {

	private final GameService gameService;
	private final S3Service s3Service;

	// 게임 생성
	@PostMapping("/admin/games")
	public ApiResponse<GameResponseDto> saveGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestPart MultipartFile image, @RequestPart GameCreateRequestDto dto) {
		Long userId = customUserDetails.getId();
		String imgUrl = s3Service.upload(image);
		GameEntity game = gameService.saveGame(dto.getName(), imgUrl, dto.getGenre(), userId);

		return success(OK, GAME_SAVE_SUCCESS, GameResponseDto.from(game));
	}

	// gameId를 통한 게임 조회
	@GetMapping("/admin/games/{gameId}")
	public ApiResponse<GameResponseDto> findGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long gameId) {
		Long userId = customUserDetails.getId();
		GameEntity game = gameService.findGame(userId, gameId);

		return success(OK, GAME_FOUND_SUCCESS, GameResponseDto.from(game));
	}

	// 게임 수정
	@PatchMapping("/admin/games/{gameId}")
	public ApiResponse<GameResponseDto> updateGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long gameId, @RequestPart(required = false) MultipartFile image,
		@RequestPart GameUpdateRequestDto dto) {
		Long userId = customUserDetails.getId();
		GameEntity game = gameService.updateGame(userId, gameId, dto, image);

		return success(OK, GAME_UPDATE_SUCCESS, GameResponseDto.from(game));
	}

	// 게임 삭제
	@DeleteMapping("/admin/games/{gameId}")
	public ApiResponse<Void> deleteGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long gameId) {
		Long userId = customUserDetails.getId();
		gameService.deleteGame(userId, gameId);

		return success(OK, GAME_DELETE_SUCCESS);
	}

	// 모든 게임 조회
	@GetMapping("/games")
	public ApiResponse<GameListResponseDto> findGames() {

		return success(OK, GAME_FOUND_SUCCESS, gameService.findGames());
	}
}

