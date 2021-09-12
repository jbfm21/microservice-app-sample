package br.com.example.microservice.apigateway;



import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;

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
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableEurekaClient
@Slf4j
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
    public RegistryEventConsumer<CircuitBreaker> myRegistryEventConsumer() {

        return new RegistryEventConsumer<CircuitBreaker>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<CircuitBreaker> entryAddedEvent) 
            {
                entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<CircuitBreaker> entryRemoveEvent) {

            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<CircuitBreaker> entryReplacedEvent) {

            }
        };
    }

	@Bean
	public RegistryEventConsumer<Retry> myRetryRegistryEventConsumer() 
	{

	    return new RegistryEventConsumer<Retry>() {
	        @Override
	        public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) 
	        {
	            entryAddedEvent.getAddedEntry().getEventPublisher().onEvent(event -> log.info(event.toString()));
	        }

	        @Override
	        public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {

	        }

	        @Override
	        public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {

	        }
	    };
	}
	
	@Autowired
	RouteDefinitionLocator locator;

	@Bean
	public List<GroupedOpenApi> apis() {
		List<GroupedOpenApi> groups = new ArrayList<>();
		List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
		definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
			String name = routeDefinition.getId().replaceAll("-service", "");
			groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build());
		});
		return groups;
	}
}
