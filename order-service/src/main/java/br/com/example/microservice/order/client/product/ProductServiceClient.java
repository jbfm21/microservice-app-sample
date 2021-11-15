package br.com.example.microservice.order.client.product;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import br.com.example.microservice.order.infraestructure.AppConstants;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

//TODO: The best practice is to call microservice directly or api-gatway?
@FeignClient(name = "catalog-product-service")
public interface ProductServiceClient {

	//TODO: Enable circuitbreaker
	@GetMapping("/products/{productId}")
	//TODO: @CircuitBreaker(name=AppConstants.Services.ProductService, fallbackMethod = "fallbackProductService")
	@RateLimiter(name = AppConstants.Services.ProductService)
	@Bulkhead(name = AppConstants.Services.ProductService)
	//TODO: @Retry(name = AppConstants.Services.ProductService, fallbackMethod = "fallbackProductService")
	//nao suportado: @TimeLimiter(name = AppConstants.Services.ProductService)	
	public ProductDTO getProduct(@RequestHeader(value = "Authorization", required = true)  String token, @PathVariable UUID productId);
	
	
	//TODO: When enable ocurr exception  (https://github.com/OpenFeign/feign/issues/935)
	/*default ProductDTO fallbackProductService(String token, UUID productId, Throwable ex) 
	{
		throw new MicroserviceUnavailableException();
	}*/
	
	
}
