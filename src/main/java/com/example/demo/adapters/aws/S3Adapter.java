package com.example.demo.adapters.aws;

import com.example.demo.ports.out.S3Port;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class S3Adapter implements S3Port {

    @Value("${aws.s3.bucket}")
    private String bucket;

    private final S3Client s3Client;

    @Override
    public void putJson(String key, String body) {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType("application/json")
                        .build(),
                RequestBody.fromString(body));
    }

    @Override
    public String bucketName() {
        return bucket;
    }
}
