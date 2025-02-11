package com.sparta.levelup_backend.config;

import java.security.Key;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtils {
	private final String PREFIX = "Bearer ";

	private final Long EXPIRE_TIME = 60L * 60L * 1000L;

	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;


	private final String SECRET_KEY;

	private final Key KEY;


	JwtUtils(@Value("${jwt.secret.key}") String SECRET_KEY){
		this.SECRET_KEY = SECRET_KEY;
			byte [] secret_key_bytes = Base64.getDecoder().decode(SECRET_KEY.getBytes());
			KEY = Keys.hmacShaKeyFor(secret_key_bytes);
	}



}
