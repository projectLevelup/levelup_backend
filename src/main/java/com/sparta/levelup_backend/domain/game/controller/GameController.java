package com.sparta.levelup_backend.domain.game.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;
import static com.sparta.levelup_backend.common.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.config.CustomUserDetails;
import com.sparta.levelup_backend.domain.game.dto.requestDto.CreateGameRequestDto;
import com.sparta.levelup_backend.domain.game.dto.requestDto.UpdateGameRequestDto;
import com.sparta.levelup_backend.domain.game.dto.responseDto.GameListResponseDto;
import com.sparta.levelup_backend.domain.game.dto.responseDto.GameResponseDto;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class GameController {

	private final GameService gameService;

	@PostMapping("/admin/games")
	public ApiResponse<GameResponseDto> saveGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody CreateGameRequestDto dto) {
		Long userId = customUserDetails.getId();
		GameEntity game = gameService.saveGame(dto.getName(), dto.getImgUrl(), dto.getGenre(), userId);

		return success(CREATED, GAME_SAVE_SUCCESS, GameResponseDto.from(game));
	}

	@GetMapping("/admin/games/{gameId}")
	public ApiResponse<GameResponseDto> findGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long gameId) {
		Long userId = customUserDetails.getId();
		GameEntity game = gameService.findGame(userId, gameId);

		return success(OK, GAME_FOUND_SUCCESS, GameResponseDto.from(game));
	}

	@PatchMapping("/admin/games/{gameId}")
	public ApiResponse<GameResponseDto> updateGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long gameId, @RequestBody UpdateGameRequestDto dto) {
		Long userId = customUserDetails.getId();
		GameEntity game = gameService.updateGame(userId, gameId, dto);

		return success(OK, GAME_UPDATE_SUCCESS, GameResponseDto.from(game));
	}

	@DeleteMapping("/admin/games/{gameId}")
	public ApiResponse<Void> deleteGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@PathVariable Long gameId) {
		Long userId = customUserDetails.getId();
		gameService.deleteGame(userId, gameId);

		return success(OK, GAME_DELETE_SUCCESS);
	}

	@GetMapping("/games")
	public ApiResponse<GameListResponseDto> findGames(){
		GameListResponseDto listDto = gameService.findGames();

		return success(OK, GAME_FOUND_SUCCESS, listDto);
	}
}

