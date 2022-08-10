# Mutad
The name Mutad is a reverse spelling of datum.

## Overview
An implementation of a real-time data platform/search engine based on various technology
stack. The implementation is rather for my own learning purpose.

<img src="./img/heatmap.gif?raw=true">

The above gif image demonstrates a real-time visualization of the tweet [geoparsing](https://en.wikipedia.org/wiki/Toponym_resolution) of Mutad.
According to my proceeding research, the amount of tweets with geospatial metadata
is quite a few, say 10 per 10,000 tweets, from the Hosebird data feed. Thus I took another
strategy to extract geospatial data from the tweet. Mutad implements real-time geoparsing based
on [CLAVIN](https://github.com/Novetta/CLAVIN). Because of my poor implementation, the accuracy is not that high yet.

<img src="./img/timeline.gif?raw=true">

The above gif image demonstrates a list timeline displaying the latest Tweets ordered from newest to oldest with a specified search query.

<img src="./img/trend.gif?raw=true">

The above gif image demonstrates a trend chart displaying the top most trended hashtags and tweet counts per a minute in the last hour.

## Architecture
<img src="./img/mutad.jpeg?raw=true">

This diagram illustrates the overall architecture of Mutad. The technology stack is as follows:

 - [Apache Storm](https://storm.apache.org/)
 - [Apache Kafka](https://kafka.apache.org/)
 - [CLAVIN](https://www.novetta.com/2020/06/clavin/)
 - [My TRIE](https://github.com/ognis1205/mutad/tree/master/storm-trie)
 - [Elasticsearch](https://www.elastic.co/elasticsearch/)
 - [Spring Boot](https://spring.io/projects/spring-boot)
 - [React](https://reactjs.org/)
 - [Next.js](https://nextjs.org/)

## Getting Started
Launch services by following the instructions below:

 - [Set up ELK Environment](https://github.com/ognis1205/mutad/tree/master/dev/elk)
 - [Set up Kafka Cluster](https://github.com/ognis1205/mutad/tree/master/dev/kafka)
 - [Set up Storm Cluster](https://github.com/ognis1205/mutad/tree/master/dev/storm)
 - [Build Storm TRIE library](https://github.com/ognis1205/mutad/tree/master/java-backends/storm-trie)
 - [Deploy Storm Collectors Topology](https://github.com/ognis1205/mutad/tree/master/java-backends/storm-collectors)
 - [Deploy Storm Processors Topology](https://github.com/ognis1205/mutad/tree/master/java-backends/storm-processors)
 - [Start Spring Boot Elasticsearch Backend](https://github.com/ognis1205/mutad/tree/master/java-backends/spring-boot-elasticsearch)
 - [Start React User Interface](https://github.com/ognis1205/mutad/tree/master/react-frontend)

## Future Work
The project is still WIP. See [Issues](https://github.com/ognis1205/mutad/issues).
