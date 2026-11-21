FROM golang:1.27.4-bookworm@sha256:4fa7c968ba93f1635670522dbb01a2c78150443e1385746924566079f36570ea AS pebble
WORKDIR /src
RUN git init . \
    && git remote add origin https://github.com/canonical/pebble.git \
    && git fetch --depth=1 origin f5da9476802a1b58b6566d3e7f4cd37debc29fec \
    && git checkout --detach f5da9476802a1b58b6566d3e7f4cd37debc29fec \
    && CGO_ENABLED=0 go build -trimpath -ldflags='-s -w' -o /out/pebble ./cmd/pebble
FROM amazoncorretto:21-al2023@sha256:3284a05cb82e9bceb4857f9d70788a6f72681d9070b550ae5f477b5e3dc81a5c AS java
FROM mcr.microsoft.com/devcontainers/base:ubuntu@sha256:e15941f70f26542ef9b0ef4bb64f88b8ecdfe8c3372e730ab6e405792805e742
USER root
RUN apt-get update && apt-get upgrade -y && rm -rf /var/lib/apt/lists/*
COPY --from=java /usr/lib/jvm/java-21-amazon-corretto /opt/java
COPY --from=pebble /out/pebble /usr/bin/pebble
ENV JAVA_HOME=/opt/java
ENV PATH="/opt/java/bin:${PATH}"
