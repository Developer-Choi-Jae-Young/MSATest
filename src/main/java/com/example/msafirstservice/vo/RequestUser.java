package com.example.msafirstservice.vo;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class RequestUser {
    private String email;

    private String name;

    private String password;
}
