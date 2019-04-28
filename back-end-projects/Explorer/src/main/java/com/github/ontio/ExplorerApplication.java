package com.github.ontio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
@tk.mybatis.spring.annotation.MapperScan("com.github.ontio.mapper")
public class ExplorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExplorerApplication.class, args);
	}
}
