package br.com.example.microservice.productreview.controller;

import java.io.IOException;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.example.microservice.infraestructure.utils.Utils;
import br.com.example.microservice.productreview.client.ProductServiceClient;
import br.com.example.microservice.productreview.domain.ProductReview;
import br.com.example.microservice.productreview.domain.ProductReviewValidator;
import br.com.example.microservice.productreview.dto.ProductDTO;
import br.com.example.microservice.productreview.dto.ProductReviewDTO;
import br.com.example.microservice.productreview.infraestructure.repository.ProductReviewRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/product-reviews")
@Log4j2
@RestControllerAdvice
@RefreshScope 
public class ProductReviewController 
{
    private final ProductReviewRepository repository;
    private final ProductReviewValidator productReviewValidator;
    private final ProductServiceClient productServiceClient;
    private final ModelMapper modelMapper;
    private final StreamBridge streamBridge; 
    
    @Autowired
    public ProductReviewController(ProductReviewRepository repository, ProductReviewValidator validator, ProductServiceClient productServiceClient, ModelMapper modelMapper, StreamBridge streamBridge) 
    {
        this.repository = repository;
        this.productReviewValidator = validator;
        this.productServiceClient = productServiceClient;
        this.modelMapper = modelMapper;
        this.streamBridge = streamBridge;
    }
    
    @Operation(summary = "Find all product reviews ")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found at least on product review", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductReview.class)) })
    })
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_FINDALL')")
    @GetMapping()
    public  ResponseEntity<Page<ProductReviewDTO.Response.Public>> findAll(
    	@RequestParam(value = "page", defaultValue = "0", required = false) int page,
	    @RequestParam(value = "count", defaultValue = "10", required = false) int count,
	    @RequestParam(value = "order", defaultValue = "ASC", required = false) Sort.Direction direction,
	    @RequestParam(value = "sort", defaultValue = "authorName", required = false) String sortProperty) 
    {
        
        Page<ProductReview> result = repository.findAll(PageRequest.of(page, count, Sort.by(direction, sortProperty)));
        Page<ProductReviewDTO.Response.Public> resultDTO = result.map(product -> modelMapper.map(product, ProductReviewDTO.Response.Public.class));
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
  
    
    @Operation(summary = "Get a product review by its id")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found the product Review", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductReviewDTO.Response.PublicWithProduct.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",  content = @Content), 
      @ApiResponse(responseCode = "404", description = "Product Review not found", content = @Content) 
    })
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_GET')")
    public ResponseEntity<ProductReviewDTO.Response.PublicWithProduct> getWithProduct(@PathVariable UUID id) 
    {
        log.info("Finding product review with product info: {}", id);
    	ProductReview productReview = repository.findByIdOrNotFoundException(id);

    	ProductDTO product = productServiceClient.getProductById(productReview.getProductId()); 
    	
    	//This is only to an example how one microservice could call another one, but the best practice is to use API composition Pattern in API Gateway
        ProductReviewDTO.Response.PublicWithProduct productReviewDTO =  modelMapper.map(productReview,  ProductReviewDTO.Response.PublicWithProduct.class);
        productReviewDTO.setProduct(product);
        
        return new ResponseEntity<>(productReviewDTO, HttpStatus.OK);
    }
   
    @Operation(summary = "Create a product review")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "201", description = "Product review created", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductReview.class)) })
    })
	@PostMapping()
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_CREATE')")
    public ResponseEntity<ProductReview> create(@RequestBody  ProductReviewDTO.Request.Create productReviewDTO) {
    	log.info("Creating product review: {}", productReviewDTO);
    	ProductReview productReview = modelMapper.map(productReviewDTO, ProductReview.class);
    	productReview.validate(productReviewValidator);
    	
    	ProductReview savedEntity = repository.save(productReview);
    	
    	log.info("Send thanks emails to user");
    	streamBridge.send("notificationEventSupplier-out-0", String.format(" Hello %s, thanks for your review", productReview.getAuthorName()));
    	
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);        
    }
    
    @Operation(summary = "Update a product review")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "202", description = "Product review updated", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductReview.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid product review information to update", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ObjectError.class)) })
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_UPDATE')")
    public ResponseEntity<?> update(@PathVariable("id") UUID id, @RequestBody ProductReviewDTO.Request.Update productReviewDTO) throws IOException 
    {
    	log.info("Updating  product: {}", id);
    	
        ProductReview productReview = repository.findByIdOrNotFoundException(id);
        Utils.merge(productReviewDTO, productReview);
        productReview.validate(productReviewValidator);
        productReview = repository.save(productReview);
    	return new ResponseEntity<>(modelMapper.map(productReview, ProductReviewDTO.Response.Public.class), HttpStatus.ACCEPTED);
    }
    
    @Operation(summary = "Delete a product review")
    @ApiResponses(value = { 
 	      @ApiResponse(responseCode = "202", description = "Product review deleted", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductReview.class)) })
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_DELETE')")
    public HttpEntity<?> delete(@PathVariable("id") UUID id) 
    {
    	log.info("Deleting  product review: {}", id);
        repository.delete(repository.findByIdOrNotFoundException(id));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}