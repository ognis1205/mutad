FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive


RUN apt-get update && \
    apt-get install -y wget && \
    apt-get install -y build-essential && \
    apt-get install -y software-properties-common && \
    apt-get install -y python && \
    add-apt-repository ppa:openjdk-r/ppa && \
    apt-get install -y openjdk-8-jdk  && \
    rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64


ARG DISTRO_NAME=apache-storm-2.2.0

RUN set -ex; \
    wget -q "http://www.apache.org/dist/storm/$DISTRO_NAME/$DISTRO_NAME.tar.gz"; \
    export GNUPGHOME="$(mktemp -d)"; \
    tar -xzf "$DISTRO_NAME.tar.gz"; \
    rm "$DISTRO_NAME.tar.gz";

ENV PATH $PATH:/$DISTRO_NAME/bin


RUN mkdir /storm

WORKDIR /storm

COPY start.sh start.sh

CMD start.sh