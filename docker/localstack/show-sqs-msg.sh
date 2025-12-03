#!/bin/bash

ENDPOINT="http://localhost:4566"
QUEUE_URL="${ENDPOINT}/000000000000/my-queue"
MAX_MESSAGES=10
WAIT_TIME=1

while true; do
    MESSAGES_JSON=$(aws sqs receive-message \
        --queue-url $QUEUE_URL \
        --max-number-of-messages $MAX_MESSAGES \
        --wait-time-seconds $WAIT_TIME \
        --output json \
        --endpoint-url $ENDPOINT)

    if echo "$MESSAGES_JSON" | grep -q '"Messages":'; then

        echo "$MESSAGES_JSON" | while read -r line; do
            if [[ $line =~ \"MessageId\"\: ]]; then
                MESSAGE_ID=$(echo "$line" | sed 's/.*: "\(.*\)".*/\1/')
            fi

            if [[ $line =~ \"Body\"\: ]]; then
                BODY=$(echo "$line" | sed 's/.*: "\(.*\)".*/\1/' | sed 's/\\n/\n/g' | sed 's/\\\"/"/g')
                echo "MessageId: $MESSAGE_ID"
                echo -e "Body:\n$BODY"
                echo "--------"
            fi

            if [[ $line =~ \"ReceiptHandle\"\: ]]; then
                RECEIPT=$(echo "$line" | sed 's/.*: "\(.*\)".*/\1/' | tr -d '\r')
                aws sqs delete-message \
                    --queue-url $QUEUE_URL \
                    --receipt-handle "$RECEIPT" \
                    --endpoint-url $ENDPOINT
            fi
        done

        echo "Messages removed."
        echo "--------"

    else
        echo "Script finished."
        break
    fi
done
