package com.example.msasecondservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private Long user_id;
    private String content;
}
