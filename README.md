bayes-dota
==========
This is an assignment project. Implemented code would be a nice example of process small
log file, create necessary data from log and expose them in a rest api call.   

### Solution path
1. Split the payload line by line(so logfile must be separated with new line)
2. LogProcessManager pass each line to the processors. 
3. If processor's pattern match with line, processor will process the line, create entity and store on list
4. After processing, LogProcessManager call save to store data from list to database.
5. Java Pattern and Matcher used to find out the pattern on the specific line.
6. H2 database used to store the processed data.


##  Design Decisions 
Based on the task description, I have found the pattern on the log file
and created following processors to process specific data. 
- HeroBuyItemProcessor
- HeroDamageProcessor
- HeroKillProcessor
- HeroSpellProcessor

### How to add new data from log (if we want to add in future) 
- create a new processor ex. FutureProcessor
- add FutureProcessor on the log processor list 

### Modules/class Introduction
- entity: package related to database entity/data model
- repository: package related to spring data jpa repository to write necessary query to fetch data.
- logprocessor: package related to the log processor classes   
- exception: package related to exception handling classes
- MatchValidation: class to validate service input those need database to validate.
- LogProcessorManager: main interface to process log.  
- MatchControllerIT: integration test 

## New dependency 
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

## Run test
- unit test 
```
mvn clean test
```

- unit test + integration test 
```
mvn clean verify
```

## Limitations
### Large log file 
As we are posting our payload using post method we have a limitaiton of 
max 4 GB file size.
 
### Data model
Data model is not optimized for hero names and items. 
We are storing redundant name and item each table instead of creating 
separate table for them and put reference. 

### No track of log file
You can upload same log file over and over. System will create new match for them.   

### Serial processing 
On this project, we are processing log file line by line and serially. For large file 
with many processors it could be an issue.  

### Log message change
If log is changed, regex must be change accordingly,  
otherwise this system will not work, even for adding a single white space. 

