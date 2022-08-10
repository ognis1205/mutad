# Spring Boot Elasticsearch Backend
This subproject is responsible for implementing the backend APIs of Elasticsearch for the Mutad project.

## Getting Started
First, configure the `src/main/resources/application.properties` file to specify the Elasticsearch endpoint URI as follows:

```properties
application.description=Spring Boot Based API for Elasticsearch
logging.level.tracer=TRACE
spring.data.elasticsearch.repositories.enabled=true
spring.elasticsearch.rest.uris=ecA-B-C-D-E.ap-northeast-1.compute.amazonaws.com:9200
```

After the configuration, deploy the Docker image to your Docker application:

```bash
 $ ./gradlew :spring-boot-elasticsearch:docker
```

Finally, run the Docker container on your Docker application:

```bash
 $ docker run -d -p 6060:8080 spring-boot-elasticsearch:1.0.0-SNAPSHOT
```

Open http://localhost:6060/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config with your browser to see the Swagger UI page.
All available functionalities will be shown up on the dashboard page.

## Features/Roadmap
The following is a checklist of features and their progress:
- [x] Twitter API
    - [x] Usual Text Search
    - [x] Geoparsing Heatmap
    - [x] Trend Chart
- [x] Dockernize the Service
