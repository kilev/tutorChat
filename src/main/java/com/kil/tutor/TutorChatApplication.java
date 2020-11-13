package com.kil.tutor;

import com.kil.tutor.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class TutorChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorChatApplication.class, args);
	}

}
