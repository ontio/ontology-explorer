################################
### Builder image
################################
FROM maven:3.6.1-slim as builder

ENV VERSION=version2.0

RUN apt update && \
    apt install -y \
    git

RUN git clone https://github.com/ontio/ontology-explorer.git && \
    cd ontology-explorer && \
    git checkout $VERSION

WORKDIR /ontology-explorer

RUN cd back-end-projects/Explorer && \
    mvn clean package

#################################
#### Runner image
#################################
FROM openjdk:8-jdk-alpine

RUN apk add --no-cache ca-certificates bash

RUN addgroup ontology && \
    adduser -G ontology -s /bin/sh -D ontology -h /home/ontology

COPY --from=builder /ontology-explorer/back-end-projects/Explorer/target/explorer-1.1.0.RELEASE.jar /home/ontology/app.jar
COPY --from=builder /ontology-explorer/back-end-projects/Explorer/config /home/ontology/config

# # Run as non root
USER ontology
WORKDIR /home/ontology

EXPOSE 8090

CMD ["java", "-jar", "app.jar"]