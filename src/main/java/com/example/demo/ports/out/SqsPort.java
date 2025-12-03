package com.example.demo.ports.out;

public interface SqsPort {
    void sendMessage(String body);
}
