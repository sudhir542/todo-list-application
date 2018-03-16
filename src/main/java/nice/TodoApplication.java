package nice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/*
* This is the main Spring Boot application class. 
* It configures Spring Boot, JPA, Swagger
*/

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TodoApplication {

	public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class);
    } 
}