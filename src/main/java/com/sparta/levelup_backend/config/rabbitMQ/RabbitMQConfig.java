package com.sparta.levelup_backend.config.rabbitMQ;

import org.hibernate.result.UpdateCountOutput;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

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

    public Queue billPayCanceledQueue() {
        return new Queue("bill.paycanceled.queue", true);
    }

    // Exchange, Queue 바인딩
    public Binding bindingPaid(Queue billPaidQueue, DirectExchange billExchange) {
        return BindingBuilder.bind(billPaidQueue).to(billExchange).with("bill.paid");
    }

    public Binding bindingPayCanceled(Queue billPayCanceledQueue, DirectExchange billExchange) {
        return BindingBuilder.bind(billPayCanceledQueue).to(billExchange).with("bill.paycanceled");
    }
}
