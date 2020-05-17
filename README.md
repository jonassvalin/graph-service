Graph-service
====================
This is a service that provides a REST API (according to the HAL+JSON specification)
against which you can create and query nodes and relationships in a graph.

The service uses neo4j version 3.5 internally to store data.

There is also a small client, that implements a (very poor) algorithm to calculate
the shortest path between two nodes using the API. The client is naively implemented
and will not take edge cases and circular dependencies into consideration (due to
time constraints).

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

Then start a REPL and run `(go)`

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
