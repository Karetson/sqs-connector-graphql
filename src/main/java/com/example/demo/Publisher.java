package com.example.demo;

import com.example.demo.api.dto.MessageInputDto;

public interface Publisher {
    void publish(MessageInputDto input);
}
