package com.github.ontio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@EnableAsync
@EnableSwagger2
@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan("com.github.ontio.mapper")
public class ExplorerApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExplorerApplication.class, args);
	}
}
