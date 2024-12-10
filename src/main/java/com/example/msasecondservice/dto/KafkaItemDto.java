package com.example.msasecondservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaItemDto {
    private Schema schema;
    private Payload payload;
}
