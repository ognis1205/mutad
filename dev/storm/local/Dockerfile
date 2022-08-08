FROM ubuntu

ENV DEBIAN_FRONTEND=noninteractive


RUN apt-get update && \
    apt-get install -y wget && \
    apt-get install -y curl && \
    apt-get install -y unzip && \
    apt-get install -y git && \
    apt-get install -y build-essential && \
    apt-get install -y software-properties-common && \
    apt-get install -y python3 && \
    apt-get install -y python-is-python3 && \
    add-apt-repository ppa:openjdk-r/ppa && \
    apt-get install -y openjdk-8-jdk  && \
    rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64


ARG DISTRO=apache-storm-2.2.1

RUN set -ex; \
    wget -q "http://www.apache.org/dist/storm/$DISTRO/$DISTRO.tar.gz"; \
    export GNUPGHOME="$(mktemp -d)"; \
    tar -xzf "$DISTRO.tar.gz"; \
    rm "$DISTRO.tar.gz";

ENV PATH $PATH:/$DISTRO/bin


COPY IndexDirectory.zip /IndexDirectory.zip

RUN unzip IndexDirectory.zip && \
    rm IndexDirectory.zip

COPY worldcities.csv.zip /worldcities.csv.zip

RUN unzip worldcities.csv.zip && \
    rm worldcities.csv.zip


RUN mkdir /storm

WORKDIR /storm

COPY start.sh /storm/start.sh

CMD /storm/start.sh
