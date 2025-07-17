// package com.katech.service.chatai.auth.jwt.producers;
//
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.katech.service.chatai.auth.jwt.dto.CreateOrgUsersRequest;
// import com.katech.service.chatai.auth.jwt.dto.MessageModel;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.stereotype.Service;
//
// @Service
// @RequiredArgsConstructor
// @Slf4j
// public class RabbitPublisher {
//
//    private final RabbitTemplate rabbitTemplate;
//
//    private final ObjectMapper objectMapper;
//
//    public void publishAccountImportMessage(MessageModel<CreateOrgUsersRequest> message) {
//        try {
//            rabbitTemplate.convertAndSend(
//                    "katech.bot.user.import", objectMapper.writeValueAsString(message));
//        } catch (JsonProcessingException e) {
//            log.error("Error while processing user import message: {}", e.getMessage(), e);
//        }
//    }
// }
