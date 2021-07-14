# Set up Elasticsearch

## Create index

```bash
 $ curl -H "Content-Type: application/json" -XPUT http://localhost:9200/tweet?pretty -d @analysis.json
 $ curl -H "Content-Type: application/json" -XPUT http://localhost:9200/geo?pretty -d @analysis.json
```

## Define mappings

```bash
 $ curl -H "Content-Type: application/json" -XPUT 'http://localhost:9200/tweet/_mapping?pretty' -d @tweet.json
 $ curl -H "Content-Type: application/json" -XPUT 'http://localhost:9200/tweet/_mapping?pretty' -d @geo.json
```
