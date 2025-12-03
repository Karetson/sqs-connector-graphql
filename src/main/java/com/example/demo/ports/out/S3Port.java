package com.example.demo.ports.out;

public interface S3Port {
    void putJson(String key, String json);
    String bucketName();
}
