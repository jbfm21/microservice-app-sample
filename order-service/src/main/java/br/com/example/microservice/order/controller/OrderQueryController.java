package br.com.example.microservice.order.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.example.microservice.order.domain.queries.FindAllOrderQuery;
import br.com.example.microservice.order.infraestructure.entity.OrderEntity;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/orders")
public class OrderQueryController {

    private final QueryGateway queryGateway;

    public OrderQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }
    
    @GetMapping("/all-orders")
    public CompletableFuture<List<OrderEntity>> findAllOrders() 
    {
    	FindAllOrderQuery query = new FindAllOrderQuery();
    	log.info("Executing command: {}", query);
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(OrderEntity.class));
    }
}