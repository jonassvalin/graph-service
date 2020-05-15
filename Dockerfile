FROM openjdk:8-jre

# Install AWS CLI
RUN apt-get update \
    && apt-get -y install python curl unzip \
    && cd /tmp \
    && curl "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" -o "awscli-bundle.zip" \
    && unzip awscli-bundle.zip \
    && ./awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws \
    && rm awscli-bundle.zip \
    && rm -rf awscli-bundle

# Install entrypoint script
COPY service.sh /opt/graph-service/graph-service.sh

RUN ["chmod", "+x", "/opt/graph-service/graph-service.sh"]

# Install uberjar
ADD graph-service-standalone.jar /opt/graph-service/graph-service-standalone.jar

# Add metadata
ADD VERSION /VERSION
ADD TAG /TAG

# Run graph-service.sh start script by default
ENTRYPOINT ["/opt/graph-service/graph-service.sh"]
