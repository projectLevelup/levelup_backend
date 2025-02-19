package com.sparta.levelup_backend.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
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
	"com.sparta.levelup_backend.domain.community.repositoryES"
})
public class ElasticsearchConfig {

	private static final String ELASTICSEARCH_HOST = "localhost";
	private static final int ELASTICSEARCH_PORT = 9200;
	private static final String ELASTICSEARCH_SCHEME = "http"; // "https" 사용 시 변경
	private static final String ELASTICSEARCH_USERNAME = "elastic";
	private static final String ELASTICSEARCH_PASSWORD = "your-secure-password";

	@Bean
	public RestClient restClient() {
		// 인증 정보 설정
		final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
			new UsernamePasswordCredentials(ELASTICSEARCH_USERNAME, ELASTICSEARCH_PASSWORD));

		// RestClient 설정
		RestClientBuilder builder = RestClient.builder(
				new HttpHost(ELASTICSEARCH_HOST, ELASTICSEARCH_PORT, ELASTICSEARCH_SCHEME))
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