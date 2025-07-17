// package com.katech.service.h04_pm2.configuration;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import java.util.ArrayList;
// import java.util.List;
// import org.springframework.amqp.core.*;
// import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
// import org.springframework.amqp.rabbit.connection.ConnectionFactory;
// import org.springframework.amqp.rabbit.core.RabbitAdmin;
// import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
// import org.springframework.amqp.support.converter.MessageConverter;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
// @Configuration
// public class RabbitMQConfiguration {
//
//    @Value("${spring.rabbitmq.host}")
//    private String rabbitMQHost;
//
//    @Value("${spring.rabbitmq.port}")
//    private int port;
//
//    @Value("${spring.rabbitmq.username}")
//    private String userName;
//
//    @Value("${spring.rabbitmq.password}")
//    private String password;
//
//    @Bean
//    public ConnectionFactory connectionFactory() {
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost(rabbitMQHost);
//        connectionFactory.setPort(port);
//        connectionFactory.setUsername(userName);
//        connectionFactory.setPassword(password);
//        return connectionFactory;
//    }
//
//    @Bean
//    public DirectExchange myExchange() {
//        return new DirectExchange("katech.bot.user");
//    }
//
//    @Bean(name = "pimAmqpAdmin")
//    public AmqpAdmin pimAmqpAdmin(ConnectionFactory connectionFactory) {
//        return new RabbitAdmin(connectionFactory);
//    }
//
//    @Bean
//    public List<Queue> queues(AmqpAdmin amqpAdmin, DirectExchange exchange) {
//        List<Queue> queues = new ArrayList<>();
//        for (String queueName : List.of("katech.bot.user.import")) {
//            Queue queue = new Queue(queueName);
//            amqpAdmin.declareQueue(queue);
//            Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueName);
//            amqpAdmin.declareBinding(binding);
//        }
//        return queues;
//    }
//
//    @Bean
//    @ConditionalOnBean
//    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
//        return new Jackson2JsonMessageConverter(objectMapper);
//    }
// }
