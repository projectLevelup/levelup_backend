package com.sparta.levelup_backend.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Fanout Exchange 생성
    @Bean
    public DirectExchange billExchange() {
        return new DirectExchange("bill.exchange");
    }

    // Queue 생성
    @Bean
    public Queue billPaidQueue() {
        return new Queue("bill.paid.queue", true);
    }

    @Bean
    public Queue billPayCanceledQueue() {
        return new Queue("bill.paycanceled.queue", true);
    }

    // Exchange, Queue 바인딩
    @Bean
    public Binding bindingPaid(Queue billPaidQueue, DirectExchange billExchange) {
        return BindingBuilder.bind(billPaidQueue).to(billExchange).with("bill.paid");
    }

    @Bean
    public Binding bindingPayCanceled(Queue billPayCanceledQueue, DirectExchange billExchange) {
        return BindingBuilder.bind(billPayCanceledQueue).to(billExchange).with("bill.paycanceled");
    }
}
