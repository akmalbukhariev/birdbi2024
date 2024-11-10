package com.birdbi.api.configure;

import com.birdbi.api.constant.Constant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final String API_NAME = "BIRDBI API";
    private static final String API_VERSION = "v1";
    private static final String API_DESCRIPTION = "BIRDBI API";

    @Bean
    public Docket apiV1(){

        int order = 1;

        HttpAuthenticationScheme authenticationScheme = HttpAuthenticationScheme
                .JWT_BEARER_BUILDER
                .name("Authorization")
                .build();

        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(true)
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .groupName(API_VERSION)
                .select()
                .apis(RequestHandlerSelectors.basePackage(Constant.basePackage))
                //.paths(PathSelectors.ant("/passblock/api/v1/**"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                //.securitySchemes(Collections.singletonList(authenticationScheme))
                .apiInfo(apiInfo(API_NAME,API_VERSION));
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        consumes.add("multipart/form-data");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    public ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .version(version)
                .description(API_DESCRIPTION)
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Bearer", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }
}
