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
import com.sparta.levelup_backend.domain.game.dto.responseDto.GameResponseDto;
import com.sparta.levelup_backend.domain.game.entity.GameEntity;
import com.sparta.levelup_backend.domain.game.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/admin/games")
@RequiredArgsConstructor
public class GameController {

	private final GameService gameService;

	/**
	 * 게임 생성
	 * @param dto
	 * @return
	 */
	@PostMapping
	public ApiResponse<GameResponseDto> saveGame(@AuthenticationPrincipal CustomUserDetails customUserDetails,
		@RequestBody CreateGameRequestDto dto) {
		Long userId = customUserDetails.getId();
		GameEntity game = gameService.saveGame(dto.getName(), dto.getImgUrl(), dto.getGenre(), userId);

		return success(CREATED, GAME_SAVE_SUCCESS, GameResponseDto.from(game));
	}

	/**
	 * 단일 게임 조회(By ID)
	 * @param gameId
	 * @return
	 */
	@GetMapping("/{gameId}")
	public ApiResponse<GameResponseDto> findGame(@PathVariable Long gameId) {
		GameEntity game = gameService.findGame(gameId);

		return success(OK, GAME_FOUND_SUCCESS, GameResponseDto.from(game));
	}

	@PatchMapping("/{gameId}")
	public ApiResponse<GameResponseDto> updateGame(@PathVariable Long gameId, @RequestBody UpdateGameRequestDto dto){
		GameEntity game = gameService.updateGame(gameId, dto);

		return success(OK, GAME_UPDATE_SUCCESS, GameResponseDto.from(game));
	}

	@DeleteMapping("/{gameId}")
	public ApiResponse<Void> deleteGame(@PathVariable Long gameId) {
		gameService.deleteGame(gameId);

		return success(OK, GAME_DELETE_SUCCESS);
	}
}

