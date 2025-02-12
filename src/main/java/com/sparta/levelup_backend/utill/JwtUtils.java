package com.sparta.levelup_backend.utill;

import com.sparta.levelup_backend.exception.common.ErrorCode;
import com.sparta.levelup_backend.exception.common.NotFoundException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
	private final String BEARER_PREFIX = "Bearer ";

	private final Long EXPIRE_TIME = 60L * 60L * 1000L;

	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;


	private final String SECRET_KEY;

	private final Key KEY;


	JwtUtils(@Value("${jwt.secret.key}") String SECRET_KEY){
		this.SECRET_KEY = SECRET_KEY;
		byte [] secret_key_bytes = Base64.getDecoder().decode(SECRET_KEY.getBytes());
		KEY = Keys.hmacShaKeyFor(secret_key_bytes);
	}

	public String createToken(String email, String role){
		Date date = new Date();


		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(email)
				.claim("role",role)
				.setExpiration(
					new Date(date.getTime() + EXPIRE_TIME))
				.setIssuedAt(date)
				.signWith(KEY, SIGNATURE_ALGORITHM)
				.compact();
	}

	public String substringToken(String token){
		if(token.startsWith(BEARER_PREFIX)){
			return token.substring(7);
		}
		throw new NotFoundException(ErrorCode.TOKEN_NOT_FOUND);
	}

	public Claims extractClaims(String token){
		return Jwts.parserBuilder()
			.setSigningKey(KEY)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}



}