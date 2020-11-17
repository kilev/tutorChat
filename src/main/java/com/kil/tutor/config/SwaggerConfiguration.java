package com.kil.tutor.config;

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

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    @Bean
    public Docket TutorChatApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Tutor Chat API")
                .version("1.0")
                .description("API for Tutor chat Application")
//                .contact(new Contact("Kil Alexander", "https://vk.com/kilev", "xyz@email.com"))
                .contact(new Contact("STOMP Api", "/websockets.json", null))
                .license("Apache License Version 2.0")
                .build();
    }

//    @Primary
//    @Bean
//    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider) {
//        return () -> {
//            SwaggerResource wsResource = new SwaggerResource();
//            wsResource.setName("ws endpoints");
//            wsResource.setSwaggerVersion("2.0");
//            wsResource.setLocation("/websockets.json");
//
//            List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
//            resources.add(wsResource);
//            return resources;
//        };
//    }
}
