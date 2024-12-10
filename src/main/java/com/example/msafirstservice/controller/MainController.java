package com.example.msafirstservice.controller;

import com.example.msafirstservice.client.SecondServiceClient;
import com.example.msafirstservice.service.UserService;
import com.example.msafirstservice.vo.RequestUser;
import com.example.msafirstservice.vo.ResponseItemList;
import com.example.msafirstservice.vo.ResponseUser;
import com.example.msafirstservice.vo.ResponseUserItem;
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
    private final UserService userService;
    private final SecondServiceClient secondServiceClient;

    @GetMapping("/welcome")
    public String main() {
        return "This is First Service";
    }

    @GetMapping("/message")
    public String message(@RequestHeader("first-request") String header) {
        return header;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port={}", request.getServerPort());
        return String.format("this is first-service check API on PORT %s", env.getProperty("local.server.port"));
    }

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> insertUser(@RequestBody RequestUser requestUser) {
        ResponseUser responseUser = userService.insertUser(requestUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUserItem> getUser(@PathVariable("userId") Long userId) {
        ResponseUserItem responseUserItem = userService.getUser(userId);
        ResponseItemList responseItemList = secondServiceClient.getData(userId);
        responseUserItem.setItemDtos(responseItemList.getItemDtos());
        return ResponseEntity.status(HttpStatus.OK).body(responseUserItem);
    }
}
