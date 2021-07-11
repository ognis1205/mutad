FROM openjdk:16-jdk-alpine

LABEL version="1.0.0"

LABEL description="Docker image for Spring Boot BFF server"

RUN apk update && apk add --no-cache bash

RUN apk add --no-cache libc6-compat

RUN ln -s /lib/libc.musl-x86_64.so.1 /lib/ld-linux-x86-64.so.2


ARG JAR_FILE

ARG APP_NAME

ENV USER_NAME ognis1205

ENV APP_HOME /home/$USER_NAME/mutad/${APP_NAME}

RUN addgroup -S appgroup && adduser -S $USER_NAME -G appgroup

USER $USER_NAME

RUN mkdir -p $APP_HOME

WORKDIR $APP_HOME

RUN mkdir config

RUN mkdir logs

COPY ${JAR_FILE} ${JAR_FILE}

RUN ln -s ${JAR_FILE} server.jar

ENTRYPOINT ["java", "-jar", "server.jar"]