bayes-dota
==========

This is the [task](TASK.md).

Any additional information about your solution goes here.

## Dev environment
- run server
```
mvn clean spring-boot:run 
```

- upload log file(as binary data)
```
curl -H "Content-Type: text/plain" --data-binary @data/combatlog_1.log.txt  http://localhost:2020/api/match
curl -H "Content-Type: text/plain" --data-binary @data/combatlog_2.log.txt  http://localhost:2020/api/match
```

- browse api doc
```
http://localhost:2020/swagger-ui.html
```
- call rest api
```
http://localhost:2020/api/match

```

