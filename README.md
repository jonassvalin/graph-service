Graph-service
====================
This is a service that provides a REST API (according to the HAL+JSON specification)
against which you can create and query nodes and relationships in a graph.

The service uses neo4j version 3.5 internally to store data.

There is also a small client, that implements a (very poor) algorithm to calculate
the shortest path between two nodes using the API. The algorithm is naively implemented
and will not take edge cases and circular dependencies into consideration (with more
time I would have adapted a proper algorithm such as Dijkstras).

### To run the tests
First start a neo4j database using Docker:

```
docker run \
     --name testneo4j \
     -p7474:7474 -p7687:7687 \
     -d \
     -v $HOME/neo4j/data:/data \
     -v $HOME/neo4j/logs:/logs \
     -v $HOME/neo4j/import:/var/lib/neo4j/import \
     -v $HOME/neo4j/plugins:/plugins \
     --env NEO4J_AUTH=neo4j/test \
     neo4j:3.5
```

Then run tests by running `lein test`

The tests use the open source library halboy `https://github.com/jimmythompson/halboy`
to traverse the API and its resources.

### To run the service
First start a neo4j database using Docker:

```
docker run \
     --name testneo4j \
     -p7474:7474 -p7687:7687 \
     -d \
     -v $HOME/neo4j/data:/data \
     -v $HOME/neo4j/logs:/logs \
     -v $HOME/neo4j/import:/var/lib/neo4j/import \
     -v $HOME/neo4j/plugins:/plugins \
     --env NEO4J_AUTH=neo4j/test \
     neo4j:3.5
```

Then run `lein run`

The service will be available under `http://localhost:1234`

The API (mostly) conforms to the HAL+JSON specification
so it's partially self-documenting and explorable. 

Nodes require a JSON body but only optionally
takes decorative fields under the key `propeties`, for example:
```JSON
{
  "properties": {
    "someField": "someValue"
  }
}
```


Relationships require the fields `from, to, type` and optionally
takes additional fields under the key `propeties`, for example:
```JSON
{
  "from": "<SOME_NODE_ID>",
  "to": "<SOME_OTHER_NODE_ID>",
  "type": "friend",
  "properties": {
    "someField": "someValue"
  }
}
``` 

Due to time constraints there is no validation added and currently the running service
and the component tests use the same underlying database, which of course you wouldn't
do on an actual production service.
