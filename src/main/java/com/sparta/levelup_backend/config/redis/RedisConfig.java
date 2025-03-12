package com.sparta.levelup_backend.config.redis;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Qualifier;
import com.sparta.levelup_backend.config.listener.RedisExpireListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.sparta.levelup_backend.domain.chat.document.ChatMessage;
import com.sparta.levelup_backend.domain.chat.service.RedisSubscriber;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;
    //
    // @Value("${spring.data.redis.password}")
    // private String redisPassword;

    // 포트 설정 (설정 안해도 Spring 기본 제공 포트인 127.0.0.1 6379로 접속)
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration(host);
        redisConfiguration.setPort(port);
        // redisConfiguration.setPassword(redisPassword);
        return new LettuceConnectionFactory(redisConfiguration);
    }

    @Bean // 기본 Serializer - 필요할 시 추가 해야함
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, ChatMessage> redisTemplateMessage(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ChatMessage> redisTemplateMessage = new RedisTemplate<>();
        redisTemplateMessage.setConnectionFactory(connectionFactory);
        redisTemplateMessage.setKeySerializer(new StringRedisSerializer());
        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
        return redisTemplateMessage;
    }

    @Bean("redisListenerTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(20);
        threadPoolTaskExecutor.setQueueCapacity(50);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setThreadNamePrefix("RedisListener-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

    /**
     * Redis 메시지 구성 설정
     * @param redisConnectionFactory Redis 연결
     * @param redisSubscriber 수신된 메시지 처리 서비스
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
        RedisConnectionFactory redisConnectionFactory,
        RedisSubscriber redisSubscriber,
        @Qualifier("redisListenerTaskExecutor") ThreadPoolTaskExecutor redisListenerTaskExecutor
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.setTaskExecutor(redisListenerTaskExecutor);
        container.addMessageListener(redisSubscriber, new PatternTopic("chatroom:*"));
        return container;
    }

    // Redis 리스너 설정 추가 (주문 생성 자동삭제)
    @Bean
    public RedisMessageListenerContainer expireEventListener(
        RedisConnectionFactory redisConnectionFactory,
        RedisExpireListener redisExpireListener
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisExpireListener, new PatternTopic("__keyevent@*__:expired"));
        return container;
    }
}
