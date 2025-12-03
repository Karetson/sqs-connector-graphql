#!/bin/bash

ENDPOINT="http://localhost:4566"
BUCKET_NAME="my-messages-bucket"

OBJECT_KEYS=$(aws s3api list-objects \
    --bucket "$BUCKET_NAME" \
    --endpoint-url "$ENDPOINT" \
    --query "Contents[].Key" \
    --output text)

if [[ -z "$OBJECT_KEYS" || "$OBJECT_KEYS" == "None" ]]; then
    echo "Bucket is already empty."
    exit 0
fi

for KEY in $OBJECT_KEYS; do
    echo "Deleting object: $KEY"
    aws s3api delete-object \
        --bucket "$BUCKET_NAME" \
        --key "$KEY" \
        --endpoint-url "$ENDPOINT"
done

echo "All objects deleted."
