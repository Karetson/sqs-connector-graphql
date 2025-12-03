#!/bin/bash

echo ">> Creating SQS queue and S3 bucket in LocalStack..."

awslocal sqs create-queue --queue-name my-queue

awslocal s3 mb s3://my-messages-bucket

echo ">> LocalStack resources created!"
