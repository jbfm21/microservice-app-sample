package br.com.example.microservice.apigateway;



import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableEurekaClient
@Log4j2
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	
	@Bean
    @Primary
    @ConditionalOnProperty("rateLimiter.non-secure")
    KeyResolver userKeyResolver() {
        return exchange -> Mono.just("1");
    }

    @Bean
    @ConditionalOnProperty("rateLimiter.secure")
    KeyResolver authUserKeyResolver() {
        return exchange -> ReactiveSecurityContextHolder.getContext()
                .map(ctx -> ctx.getAuthentication().getPrincipal().toString());
    }	
	
	@Bean
    public RegistryEventConsumer<CircuitBreaker> customRegistryEventConsumer() {

        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) 
            {
                entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {
            	//Make something
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {
            	//Make something
            }
        };
    }

	@Bean
	public RegistryEventConsumer<Retry> customRetryRegistryEventConsumer() 
	{

	    return new RegistryEventConsumer<Retry>() {
	        @Override
	        public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) 
	        {
	            entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
	        }

	        @Override
	        public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {
	        	//Make something
	        }

	        @Override
	        public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {
	        	//Make something
	        }
	    };
	}
	
	@Autowired
	RouteDefinitionLocator locator;

	@Bean
	//TO group de open api by service name
	public List<GroupedOpenApi> apis() {
		List<GroupedOpenApi> groups = new ArrayList<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		if (definitions != null)
		{
			definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> 
			{
				String name = routeDefinition.getId().replace("-service", "");
				groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
			});
		}
		return groups;
	}
}
