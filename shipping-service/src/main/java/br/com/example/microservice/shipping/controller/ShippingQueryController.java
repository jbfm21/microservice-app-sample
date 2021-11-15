package br.com.example.microservice.shipping.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.example.microservice.shipping.domain.query.Queries;
import br.com.example.microservice.shipping.domain.query.Queries.FindAllShippingQuery;
import br.com.example.microservice.shipping.domain.query.ShippingDTO;
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
public class ShippingQueryController {

    private final QueryGateway queryGateway;

    public ShippingQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }
    
    @Operation(summary = "List all shipping ")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found at least one payment", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ShippingDTO.Response.Public.class)) })
    })
    //TODO: enable security
    //@PreAuthorize("hasRole('PRF_ORDER_FINDALL')")
    @GetMapping("/all-orders")
    public CompletableFuture<List<ShippingDTO.Response.Public>> findAllOrders() 
    {
    	FindAllShippingQuery query = new Queries.FindAllShippingQuery();
    	log.info("Executing command: {}", query);
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(ShippingDTO.Response.Public.class));
    }
   
}