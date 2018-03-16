## How to Run 

This application is packaged as a jar which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the ```java -jar``` command.

* Clone this repository 
* Make sure you are using JDK 1.8 and Maven 3.x
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by one of these two methods:
```
        java -jar -Dspring.profiles.active=test todo-list-app-0.1.0.jar
or
        mvn spring-boot:run -Drun.arguments="spring.profiles.active=test"
```
* Check the stdout to make sure no exceptions are thrown

Once the application runs you should see something like this

```
2018-03-16 04:13:32.667  INFO 4644 --- [           main] s.d.s.w.s.ApiListingReferenceScanner     : Scanning for api listing references
2018-03-16 04:13:33.560  INFO 4644 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8080 (http)
2018-03-16 04:13:33.583  INFO 4644 --- [           main] nice.TodoApplication                     : Started TodoApplication in 20.851 seconds (JVM running for 22.114)
```

## About the Service

The service is just a simple Todo Application REST services. It uses an in-memory database (H2) to store the data. You can also do with a relational database like MySQL or PostgreSQL. If your database connection properties work, you can call some REST endpoints defined in ```nice.controllers``` on **port 8080**. (see below)
 
Here is what this little application demonstrates: 

* Full integration with the latest **Spring** Framework: inversion of control, dependency injection, etc.
* Packaging as a single jar with embedded container (tomcat 8): No need to install a container separately on the host just run using the ``java -jar`` command
* Writing a RESTful service using annotation: supports only JSON response; 
* Exception mapping from application exceptions to the right HTTP response with exception details in the body
* *Spring Data* Integration with JPA/Hibernate with just a few lines of configuration and familiar annotations. 
* Automatic CRUD functionality against the data source using Spring 
* Demonstrates MockMVC test framework with associated libraries
* All APIs are "self-documented" by Swagger2 using annotations 

Here are some endpoints you can call:

## Users Related Services
### Retrieve a list of users

```
GET
http://localhost:8080/users

Response: HTTP 200
```

### Retrieve a specific user resource

```
GET
http://localhost:8080/users/{userId}

Response: HTTP 200
```

### Create a new user

```
POST
http://localhost:8080/users
request body:

Response: HTTP 200
```
### Update a user

```
PUT
http://localhost:8080/users/{userId}
request body:

Response: HTTP 200
```

### Delete a user

```
http://localhost:8080/users/{userId}
Response: HTTP 200
```


## Tasks Related Services
### Retrieve a list of tasks

```
GET
http://localhost:8080/tasks

Response: HTTP 200
```

### Retrieve a specific task resource

```
GET
http://localhost:8080/tasks/{taskId}

Response: HTTP 200
```

### Create a new task

```
POST
http://localhost:8080/tasks
Request body;
Response: HTTP 200
```

### Update a task

```
PUT
http://localhost:8080/tasks/{taskId}
Request body:
Response: HTTP 200
```

### Delete a task

```
DELETE
http://localhost:8080/tasks/{taskId}
Response: HTTP 200
```


### Retrieve Tasks a based on search=status
This search condition will get the tasks whose status has 'not started' or 'completed' or 'in progress'

```
GET
http://localhost:8080/tasks?search=status:not started
Response: HTTP 200
             or
GET
http://localhost:8080/tasks?search=status:in progress
Response: HTTP 200
             or
GET
http://localhost:8080/tasks?search=status:completed
Response: HTTP 200
```

## TodoList Related Services
### Retrieve a list of todolists
```
GET
http://localhost:8080/todolists

Response: HTTP 200
```

### Retrieve a specific todolist
```
GET
http://localhost:8080/todolists/{todolistId}

Response: HTTP 200
```

### Retrieve tasks for a todolist
```
GET
http://localhost:8080/todolists/{todolistId}/tasks

Response: HTTP 200
```

### Create one or multiple todolists
```
POST
http://localhost:8080/todolists
Request body
Response: HTTP 200
```

### Delete a todolist
```
DELETE
http://localhost:8080/todolists/{todolistId}
Request body
Response: HTTP 200
```



### To disable information logging and have only error logging since we do not want to hit the production servers with information logs. 
Please make the below changes on application.yml file

```
logging:
  level:
    ROOT: ERROR
```

### To view Swagger 2 API docs

Run the server and browse to localhost:8080/swagger-ui.html

### To view your H2 in-memory datbase

The 'todo' profile runs on H2 in-memory database. To view and query the database you can browse to http://localhost:8080/h2-console (username is 'nice' with password as 'nice'). Make sure you disable this in your production profiles. 





