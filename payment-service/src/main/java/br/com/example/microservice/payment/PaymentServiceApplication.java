package br.com.example.microservice.payment;

import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.example.microservice.payment.infraestructure.interceptor.ExceptionWrappingHandlerInterceptor;
import br.com.example.microservice.payment.infraestructure.interceptor.LoggingCommandMessageDispatchInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
@ComponentScan ({"br.com.example.microservice.payment", "br.com.example.microservice.infraestructure"})
@EnableAutoConfiguration
@EnableEurekaClient
@EnableOAuth2Client
@EnableFeignClients
@EnableCaching 
public class PaymentServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}	
	
	@Bean
	public LoggingCommandMessageDispatchInterceptor loggingMessageDispatchInterceptor() {
		return new LoggingCommandMessageDispatchInterceptor();
	}
	
	@Bean
    public ExceptionWrappingHandlerInterceptor exceptionWrappingHandlerInterceptor() {
        return new ExceptionWrappingHandlerInterceptor();
    }
	
	@Bean
	public OpenAPI openAPI() {
	      return new OpenAPI()
	              .info(new Info().title("Payment")
	              .description("Payment Service application")
	              .version("v0.0.1")
	              .license(new License().name("MIT").url("http://springdoc.org")));
   }
	

   //https://github.com/AxonFramework/AxonFramework/issues/1418 to fix Retrieved response [class java.util.ArrayList] is not convertible to a List of the expected response type [cl
   //TODO: TRY to find best way
   @Qualifier("messageSerializer")
   @Bean
   public Serializer messageSerializer(ObjectMapper mapper) 
   {
	   ObjectMapper newMapper = mapper.copy();
	   newMapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
	   return JacksonSerializer.builder()
					.objectMapper(newMapper)
					.lenientDeserialization()
					.build();
	}
		

}

