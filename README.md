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


### To view Angular UI for some the above stores 
Run the server and browse to http://localhost:8080/index.html

You will see all the tasks that are created in the in-memory H2 database using data.sql file

You can also perform search on all of these tasks on different field like you can search for 'completed' it will give you only the tasks that are in status of completed or you can search for 'John Deo' where it returns only the tasks that are assigned to user 'John Deo'

Like wise you can perform some of the actions on these tasks. 

You can **Delete** a task **NOTE the task should not be tied to any of the other TodoLists** if you are deleting a task that is associated with a todolist that will give referential integrity at the backend so have to better handle these cases which I have not done for now. But we can delete a task that is not associated with any todolist(i.e. independent task record one way is to create a new task and then delete it).

You can **Create/Add** a new task by using giving it a name, desc, assigned user [choose from the list for safety, I have only allowed users to pick from the list of users which we already loaded using data.sql], status [also predefined list of status like 'In Progress', 'Not Started', 'Completed']. The page will redirect to all tasks and you can see your new task created.

```
http://localhost:8080/index.html#/addTask
```

You can **Edit/Modify** an existing task by clicking on the respective row action of the task. The modifiable fields are name, desc, assigned user, status. Once you click save it will redirect you back to all tasks page where you can see your new modified task. 

```
http://localhost:8080/index.html#/editTask2
```

You can also **View Completed Tasks** in a single view. At the bottom of the page you can see this option and once you click it will show you only the tasks that are completed and you can perform an action ** 'DELETE' ** on them. 

```
http://localhost:8080/index.html#/completedTasks
```

Started to implement the **List Users** feature but due the limited time and knowledge that I have in Angular JS **it couldn't completed**.  
  




