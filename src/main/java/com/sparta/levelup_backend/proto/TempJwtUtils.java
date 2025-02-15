package com.sparta.levelup_backend.proto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TempJwtUtils {

	private final String JWT_SECRET = "NDkxOWJkYzRmNTU3N2RjMGMyZDFlZmM5NDMxODk3ZTUxYjdkZDNkOGZmNzU2YTJiMmQ0ZmNlNzVmYzE1MTRhZA==";
	private final long JWT_EXPIRATION = 60L * 60L * 1000L; // 1시간

	private final Key key;
	public TempJwtUtils() {
		// 비밀키 문자열을 Key 객체로 변환 (UTF-8 인코딩)
		this.key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

		return Jwts.builder()
			.setSubject(userPrincipal.getUsername())
			.setIssuedAt(now)
			.setExpiration(expiryDate)
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public String getUserEmailFromJWT(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
		return claims.getSubject();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(authToken);
			return true;
		} catch (JwtException | IllegalArgumentException ex) {
			// 예외 로깅 처리 가능
		}
		return false;
	}
}
