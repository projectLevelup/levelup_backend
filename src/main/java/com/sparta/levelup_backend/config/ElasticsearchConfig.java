package com.sparta.levelup_backend.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
@EnableElasticsearchRepositories(basePackages = { // 레파지토리 생성지(패키지) 넣을 것
	"com.sparta.levelup_backend.domain.community.repositoryES",
	"com.sparta.levelup_backend.domain.product.repositoryES",
	"com.sparta.levelup_backend.domain.review.repositoryES"
})
public class ElasticsearchConfig {

	@Value("${spring.elasticsearch.uris}")
	private String elasticsearchUrl;

	@Value("${spring.elasticsearch.username}")
	private String username;

	@Value("${spring.elasticsearch.password}")
	private String elasticsearchPassword;

	@Bean
	public RestClient restClient() {
		// 인증 정보 설정
		final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
										   new UsernamePasswordCredentials(username, elasticsearchPassword));

		// RestClient 설정
		RestClientBuilder builder = RestClient.builder(HttpHost.create(elasticsearchUrl))
			.setHttpClientConfigCallback(httpClientBuilder ->
											 httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
			);

		return builder.build();
	}

	@Bean
	public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
		return new RestClientTransport(restClient, new JacksonJsonpMapper());
	}

	@Bean
	public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
		return new ElasticsearchClient(transport);
	}

	@Bean
	public ElasticsearchAsyncClient elasticsearchAsyncClient(ElasticsearchTransport transport) {
		return new ElasticsearchAsyncClient(transport);
	}

}

