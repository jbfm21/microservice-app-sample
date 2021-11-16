package br.com.example.microservice.payment.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.example.microservice.payment.domain.query.PaymentDTO;
import br.com.example.microservice.payment.domain.query.Queries;
import br.com.example.microservice.payment.domain.query.Queries.FindAllPaymentQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequestMapping("/payments")
@RestControllerAdvice
@RefreshScope 
public class PaymentQueryController {

    private final QueryGateway queryGateway;

    public PaymentQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }
    
    //TODO: @PreAuthorize("hasRole('PRF_PAYMENT_FINDALL')")
    @Operation(summary = "List all payments ")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found at least one payment", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = PaymentDTO.Response.Public.class)) })
    })
    @GetMapping("/all-payments")
    public CompletableFuture<List<PaymentDTO.Response.Public>> findAllPayments() 
    {
    	FindAllPaymentQuery query = new Queries.FindAllPaymentQuery();
    	log.info("Executing command: {}", query);
        return queryGateway.query(query, ResponseTypes.multipleInstancesOf(PaymentDTO.Response.Public.class));
    }
   
}