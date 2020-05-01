bayes-dota
==========
This is an assignment project. Implemented code would be a nice example of process small
log file. There are two parts of this project 
1. process log file and save data to database 
2. using rest api to get back the important data 

### Solution path
1. Split the payload by lin(so logfile must be separated with new line)
2. LogProcessManager pass each line to each processor
3. If pattern match with the processor regex, processor process the line and store it on the list
4. After parse is done, LogProcessManager save the data from list to database.
5. Java Pattern and Matcher is used to find out the pattern on the specific line.
6. H2 database is used to store the processed data.


##  Design Decisions 
Based on the task description, I have found the pattern on the log file
and created following processors to process specific data. 
- HeroBuyItemProcessor
- HeroDamageProcessor
- HeroKillProcessor
- HeroSpellProcessor

### How to add new data from log (if we want to add in future) 
If we want to add new data from the logfile, we have to add a new processor on the LogProcessorManager.
We may need to create a new table and repository.   


### Modules/class Introduction
- logprocessor: package related to the log processor class   
- MatchValidation: class to validate service input those need database to validate.
- LogProcessorManager: main interface to process log.  
- MatchControllerIT: integration test 

## new dependency 
- added maven-surefire-plugin to run unit test and integration test separately.
```
<plugin>
 <groupId>org.apache.maven.plugins</groupId>
 <artifactId>maven-surefire-plugin</artifactId>
    ........
<plugin>
```


## Dev environment
- run server
```
mvn clean spring-boot:run 
```

- upload log file(as binary data) from the root of the project
```
curl -H "Content-Type: text/plain" --data-binary @data/combatlog_1.log.txt  http://localhost:2020/api/match
curl -H "Content-Type: text/plain" --data-binary @data/combatlog_2.log.txt  http://localhost:2020/api/match
```

- browse api doc
```
http://localhost:2020/swagger-ui.html
```

- h2 console 
````
http://localhost:2020/h2-console/
````

# Run test
- unit test 
```
mvn clean test
```

- unit test + integration test 
```
mvn clean verify
```

