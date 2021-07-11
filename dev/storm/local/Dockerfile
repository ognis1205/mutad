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


ENV MAVEN_VERSION=3.8.1 \
    M2_HOME=/usr/lib/mvn \
    MAVEN_OPTS=-Xmx4g

RUN cd /tmp && \
    wget "http://www-us.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz" && \
    tar -zxvf "apache-maven-$MAVEN_VERSION-bin.tar.gz" && \
    mv "apache-maven-$MAVEN_VERSION" "$M2_HOME" && \
    ln -s "$M2_HOME/bin/mvn" /usr/bin/mvn && \
    rm -R /tmp/* && \
    cd -


ARG SPARK_DESTRO=apache-storm-2.2.0

# THIS IS TEMPORAL
COPY $SPARK_DESTRO.zip $SPARK_DESTRO.zip

# THIS IS TEMPORAL
RUN set -ex && \
#    wget -q "http://www.apache.org/dist/storm/$SPARK_VERSION/$SPARK_DESTRO.tar.gz" && \
    export GNUPGHOME="$(mktemp -d)" && \
    unzip "$SPARK_DESTRO.zip" && \
    rm "$SPARK_DESTRO.zip"

ENV PATH $PATH:/$SPARK_DESTRO/bin


ARG CLAVIN_DESTRO=CLAVIN-clavin-2.1.0

# THIS IS TEMPORAL
COPY $CLAVIN_DESTRO.zip $CLAVIN_DESTRO.zip

# THIS IS TEMPORAL
RUN set -ex && \
    unzip "$CLAVIN_DESTRO.zip" && \
    rm "$CLAVIN_DESTRO.zip"

# THIS IS TEMPORAL
COPY allCountries.zip /$CLAVIN_DESTRO/allCountries.zip

# THIS IS TEMPORAL
RUN cd /$CLAVIN_DESTRO && \
    unzip allCountries.zip && \
    rm allCountries.zip && \
    mvn compile && \
    mvn exec:java -Dexec.mainClass="com.bericotech.clavin.index.IndexDirectoryBuilder" && \
    cd -


RUN mkdir /storm

WORKDIR /storm

COPY start.sh /storm/start.sh

CMD /storm/start.sh