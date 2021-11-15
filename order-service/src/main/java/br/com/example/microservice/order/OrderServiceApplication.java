package br.com.example.microservice.order;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import br.com.example.microservice.infraestructure.services.JwtTokenService;
import br.com.example.microservice.order.infraestructure.interceptor.JwtMessageDispatchInterceptor;
import br.com.example.microservice.order.infraestructure.interceptor.LoggingCommandMessageDispatchInterceptor;
import br.com.example.microservice.order.infraestructure.interceptor.ExampleMessageHandlerInterceptor;
import br.com.example.microservice.order.infraestructure.interceptor.ExceptionWrappingHandlerInterceptor;
import feign.Logger;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@SpringBootApplication
@ComponentScan ({"br.com.example.microservice.order", "br.com.example.microservice.infraestructure"})
@EnableAutoConfiguration
@EnableEurekaClient
@EnableOAuth2Client
@EnableFeignClients
@EnableCaching 
public class OrderServiceApplication {
	
	@Autowired
    private JwtTokenService jwtTokenService;

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
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
	public JwtMessageDispatchInterceptor authenticationInterceptor() {
		return new JwtMessageDispatchInterceptor();
	}
	
	@Bean 
	public ExampleMessageHandlerInterceptor authenticationMessageHandlerInterceptor()
	{
		return new ExampleMessageHandlerInterceptor();
	}
	
	//Usado pelo Feing
	/*@Bean
	public RequestInterceptor requestTokenBearerInterceptor() 
	{
		return requestTemplate -> 
		{
			String jwtToken = jwtTokenService.getToken();
			requestTemplate.header("Authorization", String.format("Bearer %s", jwtToken));
		};
	}*/

	
	@Bean
	public OpenAPI productServiceOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("Order Service API")
	              .description("Order Service application")
	              .version("v0.0.1")
	              .license(new License().name("MIT").url("http://springdoc.org")));
   }
	
   @Bean
   public RedisCacheConfiguration cacheConfiguration() {
	    return RedisCacheConfiguration.defaultCacheConfig()
	      .entryTtl(Duration.ofMinutes(60))
	      .disableCachingNullValues()
	      .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
   }	
	
	

}
