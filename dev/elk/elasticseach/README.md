# Set up Elasticsearch

## Create index

```bash
 $ curl -XPUT http://localhost:9200/tweet?pretty
```

## Define mappings

```bash
 $ curl -H "Content-Type: application/json" -XPUT 'http://localhost:9200/tweet?pretty' -d @tweet_mappings.json
```
