package com.example.msasecondservice.controller;

import com.example.msasecondservice.dto.ItemDto;
import com.example.msasecondservice.service.ItemProducer;
import com.example.msasecondservice.service.ItemService;
import com.example.msasecondservice.vo.RequestItem;
import com.example.msasecondservice.vo.ResponseItem;
import com.example.msasecondservice.vo.ResponseItemList;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final Environment env;
    private final ItemProducer itemProducer;
    private final ItemService itemService;

    @GetMapping("/welcome")
    public String main() {
        return "This is Second Service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("second-request") String header) {
        return header;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port={}", request.getServerPort());
        return String.format("this is second-service check API on PORT %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/data/{userId}")
    public ResponseEntity<ResponseItem> createData(@PathVariable("userId") Long userId, @RequestBody RequestItem requestItem) {
        ItemDto itemDto = ItemDto.builder().user_id(userId).content(requestItem.getContent()).build();
        ItemDto resultItemDto = itemProducer.send("item", itemDto);
        ResponseItem responseItem = ResponseItem.builder().userId(resultItemDto.getUser_id()).content(resultItemDto.getContent()).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(responseItem);
    }

    @GetMapping("/data/{userId}")
    public ResponseEntity<ResponseItemList> getData(@PathVariable("userId") Long userId) {
        ResponseItemList responseItemList = itemService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(responseItemList);
    }
}
