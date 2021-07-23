# Mutad
The name Mutad is a reverse spelling of datum.

## Overview
An end-to-end implementation of a real-time data platform/search engine based on various technology
stack. The implementation is rather for my own learning purpose.

<img src="./img/mutad.gif?raw=true">

The above gif image demonstrates a real-time visualization of the tweet [geoparsing](https://en.wikipedia.org/wiki/Toponym_resolution) of Mutad.
According to my proceeding research, the amount of tweets with geospatial metadata
is quite a few, say 10 per 10,000 tweets, from the Hosebird data feed. Thus I took another
strategy to extract geospatial data from the tweet. Mutad implements real-time geoparsing based
on [CLAVIN](https://github.com/Novetta/CLAVIN). Because of my poor implementation, the accuracy is not that high yet.


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
 - [Build Storm TRIE library](https://github.com/ognis1205/mutad/tree/master/storm-trie)
 - [Deploy Storm Collectors Topology]()
 - [Deploy Storm Processors Topology]()
