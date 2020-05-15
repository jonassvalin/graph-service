#!/bin/bash

# Ensure SECRETS_BUCKET_NAME environment variable is present
if [ -z "$SECRETS_BUCKET_NAME" ]; then
  echo >&2 'Error: missing SECRETS_BUCKET_NAME environment variable.'
  exit 1
fi

# Ensure DEPLOYMENT_IDENTIFIER environment variable is present
if [ -z "$DEPLOYMENT_IDENTIFIER" ]; then
  echo >&2 'Error: missing DEPLOYMENT_IDENTIFIER environment variable.'
  exit 1
fi

# Ensure AWS_REGION environment variable is present
if [ -z "$AWS_REGION" ]; then
  echo >&2 'Error: missing AWS_REGION environment variable.'
  exit 1
fi

# Fetch and source env file
eval $(aws s3 cp --sse AES256 --region ${AWS_REGION} \
    s3://${SECRETS_BUCKET_NAME}/graph-service/environments/${DEPLOYMENT_IDENTIFIER}.env - | sed 's/^/export /')

# Run uberjar
java \
    -Xms256m -Xmx436m \
    -XX:MaxMetaspaceSize=64m \
    -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled \
    -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 \
    -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark \
    -Dnetworkaddress.cache.ttl=30 \
    -Duser.timezone=UTC \
    -jar /opt/graph-service/graph-service-standalone.jar
