package com.sparta.levelup_backend.domain.product.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.TransportUtils;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
public class ElasticsearchConfig {

	@Value("${elasticsearch.host}")
	private String host;

	@Value("${elasticsearch.port}")
	private Integer port;

	@Value("${elasticsearch.username}")
	private String username;

	@Value("${elasticsearch.password}")
	private String password;

	@Value("${elasticsearch.fingerprint:}") // 기본값을 빈 문자열로 설정
	private String fingerprint; 이거 엔티티 기반으로 고쳐야해

	private final ObjectMapper objectMapper;

	// 🔹 ObjectMapper를 생성자 주입
	public ElasticsearchConfig(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.objectMapper.registerModule(new JavaTimeModule()); // ✅ LocalDateTime 직렬화 지원
	}

	@Bean
	public ElasticsearchClient elasticsearchClient() {
		// 기본 인증 (Username / Password)
		CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

		// RestClient 빌드 (기본 인증만 사용)
		RestClient restClient = RestClient.builder(new HttpHost(host, port, "http"))
			.setHttpClientConfigCallback(httpClientBuilder -> {
				if (fingerprint != null && !fingerprint.isEmpty()) {
					// SSL 인증서를 검증하기 위한 컨텍스트 설정
					httpClientBuilder = httpClientBuilder.setSSLContext(
						TransportUtils.sslContextFromCaFingerprint(fingerprint));
				}
				return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
			})
			.build();

		// ✅ ObjectMapper 기반의 JacksonJsonpMapper 생성
		JacksonJsonpMapper jacksonJsonpMapper = new JacksonJsonpMapper(objectMapper);

		ElasticsearchTransport transport = new RestClientTransport(restClient, jacksonJsonpMapper);
		return new ElasticsearchClient(transport);
	}
}
