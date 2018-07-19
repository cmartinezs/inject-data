package cl.smartware.machali;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import cl.smartware.machali.service.DownloadService;

@SpringBootApplication
public class InjectDataApplication {

	@Autowired
	private DownloadService downloadService;
	
	public static void main(String[] args) {
		SpringApplication.run(InjectDataApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			File file = downloadService.download("");
		};
	}
}
