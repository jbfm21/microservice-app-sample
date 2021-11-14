package br.com.example.microservice.order.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.example.microservice.order.client.ProductDTO;
import br.com.example.microservice.order.client.ProductServiceClient;
import br.com.example.microservice.order.domain.queries.OrderDTO;
import br.com.example.microservice.order.domain.queries.Queries;
import br.com.example.microservice.order.domain.queries.OrderDTO.Response.Public;
import br.com.example.microservice.order.domain.queries.Queries.FindAllOrderQuery;
import br.com.example.microservice.order.infraestructure.entity.OrderEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/orders")
@RestControllerAdvice
@RefreshScope 
public class OrderQueryController {

    private final QueryGateway queryGateway;
    private final ProductServiceClient productServiceClient;

    public OrderQueryController(QueryGateway queryGateway, ProductServiceClient productServiceClient) {
        this.queryGateway = queryGateway;
        this.productServiceClient = productServiceClient;
    }
    
    @Operation(summary = "List all orders ")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found at least one order", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = OrderEntity.class)) })
    })
    //TODO: enable security
    //@PreAuthorize("hasRole('PRF_ORDER_FINDALL')")
    @GetMapping("/all-orders")
    public CompletableFuture<List<OrderDTO.Response.Public>> findAllOrders() 
    {
    	//TODO: How to use securitycontext inside queryGateway
    	List<ProductDTO> products = listAllProducts();
    	FindAllOrderQuery query = new Queries.FindAllOrderQuery();
    	log.info("Executing command: {}", query);
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(OrderDTO.Response.Public.class)).thenApply(p->{
        	fillWithProductInfo(products, p);
        	return p;
        });
    }
    
    private void fillWithProductInfo(List<ProductDTO> products, List<OrderDTO.Response.Public> orders) 
    {
    	orders.stream().forEach(order ->
    	{
    		if (!CollectionUtils.isEmpty(order.getOrderItems())) {
    			order.getOrderItems().stream().forEach(orderItem->{
    				ProductDTO product = products.stream()
    											  .filter(p->p.getProductId().compareTo(orderItem.getProductId()) == 0 )
    											  .findFirst().orElse(null);
    				orderItem.setProductInfo(product);
    			});
    		}
    	});
	}
    
    
    @Cacheable(value = "listAllProducts")
    public List<ProductDTO> listAllProducts() 
    {
    	//TODO: increase performance using HashMap ? or direct product Cache By Id
    	return productServiceClient.listAllProducts();
    }
}