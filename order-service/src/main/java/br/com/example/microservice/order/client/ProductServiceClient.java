package br.com.example.microservice.order.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.example.microservice.infraestructure.exceptions.MicroserviceUnavailableException;
import br.com.example.microservice.order.infraestructure.AppConstants;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "catalog-product-service")
public interface ProductServiceClient {

	//TODO: Enable circuitbreaker
	@GetMapping("/products/all-products")
	//@CircuitBreaker(name=AppConstants.Services.ProductService, fallbackMethod = "fallbackProductService")
	//@RateLimiter(name = AppConstants.Services.ProductService)
	//@Bulkhead(name = AppConstants.Services.ProductService)
	//@Retry(name = AppConstants.Services.ProductService, fallbackMethod = "fallbackProductService")
	//nao suportado: @TimeLimiter(name = AppConstants.Services.ProductService)	
	public List<ProductDTO> listAllProducts();
	//TODO: When enable ocurr exception  (https://github.com/OpenFeign/feign/issues/935)
	/*default List<ProductDTO> fallbackProductService(Exception ex) 
	{
		throw new MicroserviceUnavailableException();
	}*/
}
