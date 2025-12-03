package com.example.demo.domain.message;

import com.example.demo.api.dto.MessageInputDto;
import com.example.demo.api.dto.MessageResultDto;
import com.example.demo.ports.out.S3Port;
import com.example.demo.ports.out.SqsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${aws.sqs.max-msg-size}")
    private int maxSize;

    private final SqsPort sqs;
    private final S3Port s3;

    public MessageResultDto process(MessageInputDto input) {
        String payload = input.getPayloadJson();
        int payloadSize = payload.getBytes(StandardCharsets.UTF_8).length;

        if (payloadSize > maxSize) {
            String key = "messages/" + input.getId() + "-" + UUID.randomUUID() + ".json";

            s3.putJson(key, payload);

            String referenceJson = """
                    {
                      "type": "S3_REFERENCE",
                      "bucket": "%s",
                      "key": "%s",
                      "originalId": "%s"
                    }
                    """.formatted(s3.bucketName(), key, input.getId());

            sqs.sendMessage(referenceJson);

            return new MessageResultDto(
                    "S3:" + key,
                    "Payload too large → stored in S3, reference sent to SQS"
            );
        }

        sqs.sendMessage(payload);

        return new MessageResultDto("SQS", "Payload delivered directly to SQS");
    }
}
