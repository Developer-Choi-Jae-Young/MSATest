package com.example.msasecondservice.vo;

import com.example.msasecondservice.dto.ItemDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseItemList {
    List<ItemDto> itemDtos;
}
