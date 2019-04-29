package com.github.ontio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Timestamp;
import java.util.Date;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.enable}")
    private boolean isEnable;
    public static final String DEFAULT_INCLUDE_PATTERN = "/v2.*";

/*    @Bean
    public Docket swaggerSpringfoxDocket() {
        //联系信息
        Contact contact = new Contact(
                "zzsZhou",
                "https://explorer.ont.io",
                "contact@ont.io");

        List<VendorExtension> vext = new ArrayList<>();
        //api基本信息，展示在页面
        ApiInfo apiInfo = new ApiInfo(
                "Ontology Explorer API",
                "This is explorer API",
                "2.0",
                "https://github.com/ontio/ontology-explorer",
                contact,
                "MIT",
                "https://github.com/ontio/ontology-explorer",
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
    }*/


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(isEnable)
                .directModelSubstitute(Date.class, Long.class)//将Date类型全部转为Long类型
                .directModelSubstitute(Timestamp.class, Long.class)//将Timestamp类型全部转为Long类型
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.github.ontio"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        //联系信息
        Contact contact = new Contact(
                "zzsZhou",
                "https://explorer.ont.io",
                "zhouqiang@ont.io");
        //api基本信息，展示在页面
        return new ApiInfoBuilder()
                .title("Ontology Explorer APIs")
                .description("This is Ontology explorer apis")
                .termsOfServiceUrl("https://github.com/ontio/ontology-explorer")
                .contact(contact)
                .version("2.0")
                .build();
    }


}