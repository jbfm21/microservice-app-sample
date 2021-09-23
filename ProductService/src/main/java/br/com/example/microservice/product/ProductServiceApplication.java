package br.com.example.microservice.product;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;



@SpringBootApplication
@ComponentScan
@EnableEurekaClient
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	@Bean
	public OpenAPI productServiceOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("Product Service API")
	              .description("Product Service application")
	              .version("v0.0.1")
	              .license(new License().name("MIT").url("http://springdoc.org")));
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}	
	
	//@Bean
	/*ScopeDecorator threadContextScopeDecorator() {
		return new ThreadContextScopeDecorator();
	}*/
}
