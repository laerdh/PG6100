# PG6100 - Enterprise Programming 2

## MyQuizGame
A quiz game with two REST APIs interacting with each other.
Consists of following modules:
1. Quiz (Backend)
2. QuizAPI (REST API)
3. GameAPI (REST API)

Run QuizAPI with following command:
```
mvn clean wildfly:run
```

Run GameAPI with following command:
```
mvn clean package
```
and then:
```
java -jar GameAPI-1.0-SNAPSHOT.jar server config.yml
```

## WorkshopREST
A small REST API created in workshop, 1/12-16. 
Provides CRUD operation to a resource /users
