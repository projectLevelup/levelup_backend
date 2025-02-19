package com.sparta.levelup_backend.domain.user.dto.request;

import org.hibernate.validator.constraints.URL;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserImgUrlReqeustDto {

	@URL
	private final String imgUrl;

}
