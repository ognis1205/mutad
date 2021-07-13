FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive


RUN apt-get update && \
    apt-get install -y wget && \
    apt-get install -y curl && \
    apt-get install -y unzip && \
    apt-get install -y git && \
    apt-get install -y build-essential && \
    apt-get install -y software-properties-common && \
    apt-get install -y python && \
    add-apt-repository ppa:openjdk-r/ppa && \
    apt-get install -y openjdk-8-jdk  && \
    rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64


ARG SPARK_DISTRO=apache-storm-2.2.0

RUN set -ex; \
    wget -q "http://www.apache.org/dist/storm/$SPARK_DISTRO/$SPARK_DISTRO.tar.gz"; \
    export GNUPGHOME="$(mktemp -d)"; \
    tar -xzf "$SPARK_DISTRO.tar.gz"; \
    rm "$SPARK_DISTRO.tar.gz";

ENV PATH $PATH:/$SPARK_DISTRO/bin


COPY IndexDirectory.zip /IndexDirectory.zip

RUN IndexDirectory.zip && \
    rm IndexDirectory.zip

COPY worldcities.csv.zip /worldcities.csv.zip

RUN unzip worldcities.csv.zip && \
    rm worldcities.csv.zip


RUN mkdir /storm

WORKDIR /storm

COPY start.sh /storm/start.sh

CMD /storm/start.sh