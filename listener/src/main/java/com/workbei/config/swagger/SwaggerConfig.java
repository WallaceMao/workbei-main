package com.workbei.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger的基本配置，只用于dev或者test环境下
 * 开启spring.profiles.active=dev配置时才有效。
 * 开启swagger的方式为：
 * (1)设置环境变量SPRING_PROFILES_ACTIVE为dev或者test
 * (2)设置jvm参数：-Dspring.profiles.active=dev
 * swagger集成教程：
 * @link https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api
 * @link https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/
 * @author Wallace Mao
 * Date: 2018-12-06 16:25
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
@Profile({"dev", "test"})
public class SwaggerConfig extends WebMvcConfigurerAdapter {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.workbei.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("功倍同步中心")
                .description("功倍后台的同步接口，用来操作企业组织结构")
                .version("0.0.1")
                .termsOfServiceUrl("https://www.workbei.com")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
