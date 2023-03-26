# Getting Started
```shell
cd gateway

./gradlew clean build

docker-compose up

curl -X POST localhost:8080/coda/gateway -d @requests/post_gateway.json --header "Content-Type: application/json"
```
