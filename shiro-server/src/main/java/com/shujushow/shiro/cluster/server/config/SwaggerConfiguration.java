package com.shujushow.shiro.cluster.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Created by xinshu on 2016/5/25.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements EnvironmentAware {

    @Value("${swagger.basepackage}")
    String basePackage;

    public static final String DEFAUTL_INCLUDE_PATERN = "/.*";
    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "swagger.");
    }


    @Bean
    public Docket swaggerSpringfoxDocket() {
        StopWatch watch = new StopWatch();
        watch.start();

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .genericModelSubstitutes(ResponseEntity.class)
                .select()
                .paths(regex(DEFAUTL_INCLUDE_PATERN))
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(propertyResolver.getProperty("title"),
                propertyResolver.getProperty("description"),
                propertyResolver.getProperty("version"),
                propertyResolver.getProperty("termsOfServiceUrl"),
                propertyResolver.getProperty("contact"),
                propertyResolver.getProperty("license"),
                propertyResolver.getProperty("licenseUrl")
        );
    }

}
