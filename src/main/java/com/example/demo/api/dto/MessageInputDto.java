package com.example.demo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MessageInputDto {
    private String id;
    private String payloadJson;
}
