package com.example.msasecondservice.service;

import com.example.msasecondservice.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    List<Field> fields = Arrays.asList(
            new Field("int32", true, "user_id"),
            new Field("string", true, "content"));
    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
            .name("item")
            .build();

    public ItemDto send(String topic, ItemDto itemDto) {
        Payload payload = Payload.builder()
                .user_id(itemDto.getUser_id())
                .content(itemDto.getContent())
                .build();

        KafkaItemDto kafkaOrderDto = new KafkaItemDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            jsonInString = mapper.writeValueAsString(kafkaOrderDto);
        } catch(JsonProcessingException ex) {
            ex.printStackTrace();
        }

        kafkaTemplate.send(topic, jsonInString);
        log.info("Item Producer sent data from the this microservice: " + kafkaOrderDto);

        return itemDto;
    }
}
