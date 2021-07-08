# Set up Elasticsearch

## Create index

```bash
 $ curl -XPUT http://localhost:9200/tweet?pretty
 $ curl -XPUT http://localhost:9200/geo?pretty
```

## Define mappings

```bash
 $ curl -H "Content-Type: application/json" -XPUT 'http://localhost:9200/tweet/_mapping?pretty' -d @tweet.json
 $ curl -H "Content-Type: application/json" -XPUT 'http://localhost:9200/tweet/_mapping?pretty' -d @geo.json
```
