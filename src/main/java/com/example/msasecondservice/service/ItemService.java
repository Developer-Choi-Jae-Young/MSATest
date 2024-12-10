package com.example.msasecondservice.service;

import com.example.msasecondservice.dto.ItemDto;
import com.example.msasecondservice.entity.Item;
import com.example.msasecondservice.repository.ItemRepository;
import com.example.msasecondservice.vo.ResponseItemList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public ResponseItemList findAllByUserId(Long userId) {
        List<Item> items = itemRepository.findAllByUserId(userId);

        List<ItemDto> itemDtos = new ArrayList<>();

        items.forEach(i -> {
            itemDtos.add(ItemDto.builder().user_id(i.getUserId()).content(i.getContent()).build());
        });

        return ResponseItemList.builder().itemDtos(itemDtos).build();
    }
}
