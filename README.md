bayes-dota
==========
Dota2 match logfile processor to get the specific data using rest api. 


### 
Based on the task description, I have found the pattern. Created four processors. 
- HeroBuyItemProcessor
- HeroDamageProcessor
- HeroKillProcessor
- HeroSpellProcessor

### Solution path:
1. Split the payload by line
2. LogProcessManager pass each line to each processor
3. If line matched with the processor regex, processor process the line and store it on the list
4. After parse is done, LogProcessManager save the data from list to database.



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

- h2 console
````
http://localhost:2020/h2-console/
````
