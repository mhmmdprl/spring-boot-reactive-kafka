# spring-boot-reactive-kafka


How to run project ?
- open a new terminal
- docker-compose build sb-reactive-kafka
- docker-compose up -d sb-reactive-kafka
- you can produce to  'c-topic' 
- you can send message in json format as an example {"id" : 12}
- comsume to 'p-topic'
- expected result In the logs, the incoming message appears in every 3 messages.
