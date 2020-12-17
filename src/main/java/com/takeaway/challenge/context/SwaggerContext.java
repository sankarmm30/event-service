package com.takeaway.challenge.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration for enabling Springfox swagger 2 and mentioning spring where to scan for API controllers
 *
 */
@Configuration
public class SwaggerContext {

    private static final String PACKAGE = "com.takeaway.challenge.controller";
    private static final String REGEX = "/.*";
    private static final String API_NAME = "Event Service REST API";
    private static final String DESC = "This service is responsible for consuming the employee event " +
            "and also provides endpoints for accessing the event data";
    private static final String VERSION = "1.0.0";

    @Bean
    public Docket api() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors
                        .basePackage(PACKAGE))
                .paths(PathSelectors.regex(REGEX))
                .build().apiInfo(apiEndPointsInfo());
    }

    private ApiInfo apiEndPointsInfo() {

        return new ApiInfoBuilder().title(API_NAME)
                .description(DESC)
                .contact(new Contact("Anonymous", "", "anonymous@global.com"))
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version(VERSION)
                .build();
    }
}