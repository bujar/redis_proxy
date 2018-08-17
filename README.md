# Redis Proxy Application

This application serves as a proxy between a user and a backing redis db. This proxy implements a local cache which replicates key/value pairs that are stored in the db. The cache is used for faster retrieval but it has a fixed size and expiration time for entries in the cache. A request to our proxy app would contain the key of the item we are searching for. We first check the local cache for the key and if it is not found there, we then check the backing redis db for the key. We then return the value to the user.

## Compile and run
In one terminal window:
```
docker-compose up   # runs redis server through docker
```

In second terminal window:
```
make		# compiles .class files into bin/
make test	# compiles junit tests
make run_test	# launches junit tests
make run	# runs web server for testing HTTP endpoints via web browser or curl
```

### Dependencies
- Docker (docker & docker-compose)
- Java 7 or above

## Components
 ### HTTP server
  - Configurable to run on a configurable portNum
  - Endpoint localhost:portNum/get/key=X  -  Retrieves entry with Key X from our cache or redis db
  - Endpoint localhost:portNum/set?key=X&value=Y  -  Creates new entry into our cache and redis db
  - Functions as a web app as well

 ### LRUCache
  - Uses LinkedHashMap to store entries in our cache. This serves as a queue where the first item in the LinkedHashMap is the oldest entry or least recently used
  - Each entry also contains the time it was stored so that we only return the item if it is fresh.
  - When cache fills up to the configurable max size, we evict the least recently used key
  - 'Used' is defined as a key that has been retrieved or created

 ### KVEntry
  - Object that stores key,value and timeStored. These are stored in our LRUCache<LinkedHashMap> class
  
 ### RedisClient
  - Used to interface with our backing redis db. Implements method set, get, del and exists
  - Redis port which it connects to is configurable
 
 ### Config file
  - Can specify redis port, http port, cache size and cache expiry time
 
## Complexity
 - Key Lookup
   - Cache Hit: O(1)
   - Cache Miss, Redis Lookup: O(1)
 - Key Insertion
   - Cache: O(1)
   - Redis: O(1)

## Time Taken
 - Read Requirements: 30 min
 - Set up HTTP Server: 45 min
 - Set up LRU Cache: 1.5 hr
 - Set up RedisClient: 45 min
 - Set up HTTP Server: 1 hour
 - Set up Testing: 1 hour
 - Docker + Building: 1 hours
 - 6.5 Hours

## Requirements Omitted
 - Single-click build and test
   - I was not successful in setting up a docker image to build and compile the code in a Java environment. I got this mostly working but there was an issue with the HTTP server responses when running in docker. Therefore, Java 7 is a prerequisite for the host machine to run this application
 - Bonus Requirements
   - I was not able to complete these due to time restrictions
