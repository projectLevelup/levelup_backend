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

	private final Long REFRESH_TOKEN_EXPIRE_TIME =  12L * 60L * 60L * 1000L;

	private final Long ACCESS_TOKEN_EXPIRE_TIME = 30L * 60L * 1000L;

	private final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;


	private final String SECRET_KEY;

	private final Key KEY;


	JwtUtils(@Value("${jwt.secret.key}") String SECRET_KEY){
		this.SECRET_KEY = SECRET_KEY;
		byte [] secret_key_bytes = Base64.getDecoder().decode(SECRET_KEY.getBytes());
		KEY = Keys.hmacShaKeyFor(secret_key_bytes);
	}

	public String createAccessToken(String email, Long id, String role){
		Date date = new Date();


		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(email)
				.claim("role",role)
				.claim("id",id.toString())
				.claim("type","ACCESS")
				.setExpiration(
					new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
				.setIssuedAt(date)
				.signWith(KEY, SIGNATURE_ALGORITHM)
				.compact();
	}

	public String createRefreshToken(String email, Long id, String role){
		Date date = new Date();


		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(email)
				.claim("role",role)
				.claim("id",id.toString())
				.claim("type","REFRESH")
				.setExpiration(
					new Date(date.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
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

	public String refresingToken(String validToken) throws Exception {
		Claims claims = extractClaims(substringToken(validToken));

		String email = claims.getSubject();
		String role = claims.get("role", String.class);
		Long id = Long.parseLong(claims.get("id", String.class));
		String type = claims.get("type",String.class);

		if(type.equals("ACCESS")){
			return createRefreshToken(email,id,role);
		}else {
			return createAccessToken(email,id,role);
		}

	}



}