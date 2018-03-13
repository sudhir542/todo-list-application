package nice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import nice.models.UserDao;
import nice.models.User;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class TodoApplication {

	@Bean
	CommandLineRunner init(UserDao userDao) {
		return (evt) -> {
			userDao.save(new User("niceUser1"));
			userDao.save(new User("niceUser2"));
			userDao.save(new User("niceUser3"));
		};
	}

    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class);
    } 
}