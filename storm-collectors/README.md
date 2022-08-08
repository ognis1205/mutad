# Storm Topology for Collecting Tweet Data
This subproject is responsible for implementing the Storm topology for collecting tweet data.

## Getting Started
First, make the shadow jar file of the project:

```bash
 $ ./gradlew :storm-collectors:shadowJar
```

After copying the resulting jar file to the Storm cluster, run the following command inside:

```bash
 $ storm jar storm-collectors-1.0.0-SNAPSHOT.jar io.github.ognis1205.mutad.storm.CollectorTopology \
   --hosebird-api-key hosebird_api_key \
   --hosebird-api-secret hosebird_api_secret_key \
   --hosebird-token hosebird_token_key \
   --hosebird-token-secret hosebird_toke_secret_key \
   --kafka-broker-list kafka_broker_dns:9092 \
   --kafka-topic twitter
```

Open http://localhost:8080 with your browser to see the Storm UI page.


