# Storm Topology for Processing Tweet Data
This subproject is responsible for implementing the Storm topology for processing tweet data.

## Getting Started
First, make the shadow jar file of the project:

```bash
 $ ./gradlew :storm-processors:shadowJar
```

After copying the resulting jar file to the Storm cluster, run the following command inside:

```bash
 $ storm jar storm-processors-1.0.0-SNAPSHOT.jar io.github.ognis1205.mutad.storm.ProcessorTopology \
   --kafka-broker-list kafka_broker_dns_1:9092,kafka_broker_dns_2:9092 \
   --kafka-topic twitter \
   --es-node-list elastic_search_node_dns:9200
```

Open http://localhost:8080 with your browser to see the Storm UI page.


