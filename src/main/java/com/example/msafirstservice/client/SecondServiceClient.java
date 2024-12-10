package com.example.msafirstservice.client;

import com.example.msafirstservice.vo.ResponseItemList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="MSASECONDSERVICE", configuration = FeignErrorDecoder.class)
public interface SecondServiceClient {
    @GetMapping("/data/{userId}")
    ResponseItemList getData(@PathVariable Long userId);
}
