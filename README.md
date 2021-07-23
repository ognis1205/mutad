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

## Getting Started
First, run the development server:
