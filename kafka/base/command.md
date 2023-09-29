### Apache Kafka commands

##### kafka-topics
```shell
bin/kafka-topics --describe --topic quickstart-events --bootstrap-server localhost:9092
```

##### kafka-console-producer
```shell
bin/kafka-console-producer --topic quickstart-events --bootstrap-server localhost:9092
```

##### kafka-console-consumer
```shell
bin/kafka-console-consumer --topic quickstart-events --from-beginning --bootstrap-server localhost:9092
```
