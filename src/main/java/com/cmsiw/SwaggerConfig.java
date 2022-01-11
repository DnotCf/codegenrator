package com.cmsiw;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="1396513066@qq.com">Yu Yong</a>
 * @version 1.0
 * @desc SwaggerConfig Swagger2配置
 * @url http://127.0.0.1/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

   /* @Bean
    public Docket api() {
        //添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        Parameter build = tokenPar.name("Authorization").description("令牌以bearer 开头,第一认证内容为: basic Y21zaXc6Y21zaXdzZWNyZXQ=").modelRef(new ModelRef("string")).parameterType("header").defaultValue("bearer").required(false).build();
        pars.add(build);


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }*/

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("test-接口文档")
                .description("1111")
                .version("1.0")
                .contact(new Contact("EMAIL", "http://www.baidu.com", "1398741644@qq.com"))
                .build();
    }
}
