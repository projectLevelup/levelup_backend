package com.sparta.levelup_backend.domain.game.controller;

import static com.sparta.levelup_backend.common.ApiResMessage.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.levelup_backend.common.ApiResponse;
import com.sparta.levelup_backend.domain.game.dto.requestDto.CreateGameRequestDto;
import com.sparta.levelup_backend.domain.game.dto.requestDto.FindGameRequestDto;
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
	public ResponseEntity<ApiResponse<GameResponseDto>> saveGame(@RequestBody CreateGameRequestDto dto) {
		//TODO: Token 도입이 되면 Token에서 User의 정보를 가져오는 코드로 리펙토링 필요
		Long userId = 1l;
		GameEntity game = gameService.saveGame(dto.getName(), dto.getImgUrl(), dto.getGenre(), userId);
		return ResponseEntity.ok(ApiResponse.success(GAEM_SAVE_SUCCESS, GameResponseDto.from(game)));
	}
}

