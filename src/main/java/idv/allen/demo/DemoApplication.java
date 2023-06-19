package idv.allen.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class DemoApplication {
	
	private static final Logger logger = LogManager.getLogger();

	public static void main(String[] args) {
		
		logger.debug("Application start...");
		
		SpringApplication.run(DemoApplication.class, args);
		
	}

}
