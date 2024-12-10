package com.example.msafirstservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {
    private Long user_id;
    private String content;
}
