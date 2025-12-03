#!/bin/bash

ENDPOINT="http://localhost:4566"
BUCKET_NAME="my-messages-bucket"

OBJECT_KEYS=$(aws s3api list-objects \
    --bucket "$BUCKET_NAME" \
    --endpoint-url "$ENDPOINT" \
    --query "Contents[].Key" \
    --output text)

if [[ -z "$OBJECT_KEYS" || "$OBJECT_KEYS" == "None" ]]; then
    echo "Bucket is empty."
    exit 0
fi

for KEY in $OBJECT_KEYS; do
    echo "Object Key: $KEY"
    echo "Body content:"
    aws s3 cp "s3://$BUCKET_NAME/$KEY" - --endpoint-url "$ENDPOINT"
    echo -e "\n--------"
done

echo "Done."
