package com.example.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageResultDto {
    private String destination;
    private String info;
}
