package com.example.demo.api;

import com.example.demo.api.dto.MessageInputDto;
import com.example.demo.domain.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MessageService service;

    @MutationMapping
    public void receiveMessage(@Argument("input") MessageInputDto input) {
        service.publish(input);
    }
}
