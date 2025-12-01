package com.example.demo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.sqs.max-msg-size}")
    private int maxSize;

    private final SqsClient sqs;
    private final S3Client s3;

    public MessageResultDto process(MessageInputDto input) {
        String payload = input.getPayloadJson();
        int payloadSize = payload.getBytes(StandardCharsets.UTF_8).length;

        if (payloadSize > maxSize) {
            // -------------------------------
            // 1. Upload do S3
            // -------------------------------
            String key = "messages/" + input.getId() + "-" + UUID.randomUUID() + ".json";

            s3.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType("application/json")
                            .build(),
                    RequestBody.fromString(payload));

            // -------------------------------
            // 2. Wysyłamy do SQS TYLKO REFERENCJĘ
            // -------------------------------
            String referenceJson = """
                    {
                      "type": "S3_REFERENCE",
                      "bucket": "%s",
                      "key": "%s",
                      "originalId": "%s"
                    }
                    """.formatted(bucket, key, input.getId());

            sqs.sendMessage(SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(referenceJson)
                    .build());

            return new MessageResultDto(
                    true,
                    "S3:" + key,
                    "Payload too large → stored in S3, reference sent to SQS"
            );
        }

        // -------------------------------
        // Payload mieści się → wysyłamy bezpośrednio do SQS
        // -------------------------------
        sqs.sendMessage(SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(payload)
                .build());

        return new MessageResultDto(true, "SQS", "Payload delivered directly to SQS");
    }
}
