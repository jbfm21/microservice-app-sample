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
import br.com.example.microservice.order.domain.query.OrderDTO;
import br.com.example.microservice.order.domain.query.Queries;
import br.com.example.microservice.order.domain.query.OrderDTO.Response.Public;
import br.com.example.microservice.order.domain.query.Queries.FindAllOrderQuery;
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

    public OrderQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }
    
    @Operation(summary = "List all orders ")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found at least one order", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = OrderDTO.Response.Public.class)) })
    })
    //TODO: enable security
    //@PreAuthorize("hasRole('PRF_ORDER_FINDALL')")
    @GetMapping("/all-orders")
    public CompletableFuture<List<OrderDTO.Response.Public>> findAllOrders() 
    {
    	FindAllOrderQuery query = new Queries.FindAllOrderQuery();
    	log.info("Executing command: {}", query);
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(OrderDTO.Response.Public.class));
    }
   
}