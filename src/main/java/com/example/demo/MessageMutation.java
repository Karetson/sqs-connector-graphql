package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageMutation {

    private final MessageService service;

    @MutationMapping()
    public MessageResultDto receiveMessage(@Argument("input") MessageInputDto input) {
        return service.process(input);
    }
}
