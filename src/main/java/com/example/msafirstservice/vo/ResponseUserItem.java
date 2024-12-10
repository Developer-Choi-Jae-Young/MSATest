package com.example.msafirstservice.vo;

import com.example.msafirstservice.dto.ItemDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class ResponseUserItem {
    private String email;
    private String name;
    List<ItemDto> itemDtos;
}
