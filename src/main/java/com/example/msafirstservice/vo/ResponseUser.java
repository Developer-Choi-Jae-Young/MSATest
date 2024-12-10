package com.example.msafirstservice.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUser {
    private String email;
    private String name;
}
