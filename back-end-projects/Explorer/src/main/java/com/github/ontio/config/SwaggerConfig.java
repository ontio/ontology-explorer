package com.github.ontio.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean isEnable;
    public static final String DEFAULT_INCLUDE_PATTERN = "/v1.*";

    @Bean
    public Docket swaggerSpringfoxDocket() {
        log.debug("Starting Swagger. Isenable:{}", isEnable);
        Contact contact = new Contact(
                "xx",
                "https://explorer.ont.io",
                "explorer@onchain.com");

        List<VendorExtension> vext = new ArrayList<>();
        ApiInfo apiInfo = new ApiInfo(
                "Backend API",
                "This is explorer API",
                "2.0",
                "https://baidu.com",
                contact,
                "MIT",
                "https://baidu.com",
                vext);

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .pathMapping("/")
                .apiInfo(ApiInfo.DEFAULT)
                .enable(isEnable)
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class)
                .ignoredParameterTypes(SpringDataWebProperties.Pageable.class)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .useDefaultResponseMessages(false);

        docket = docket.select()
                .paths(regex(DEFAULT_INCLUDE_PATTERN))
                .build();

        return docket;
    }


}