package com.example.msafirstservice.vo;

import com.example.msafirstservice.dto.ItemDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseItemList {
    List<ItemDto> itemDtos;
}
