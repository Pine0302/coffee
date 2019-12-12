package geektime.spring.data.datasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class DataSourceDemoApplication implements ApplicationRunner {

	public static void main(String[] args) {
		SpringApplication.run(DataSourceDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	}

}

