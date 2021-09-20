package br.com.example.microservice.productreview.controller;

import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;

import br.com.example.microservice.productreview.client.ProductServiceClient;
import br.com.example.microservice.productreview.domain.ProductReview;
import br.com.example.microservice.productreview.domain.ProductReviewValidator;
import br.com.example.microservice.productreview.dto.ProductDTO;
import br.com.example.microservice.productreview.dto.ProductReviewDTO;
import br.com.example.microservice.productreview.infraestructure.ProductReviewRepository;
import br.com.example.microservice.productreview.infraestructure.exceptions.CustomRestExceptions;
import br.com.example.microservice.productreview.infraestructure.utils.Utils;
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
public class ProductReviewController 
{
    private final ProductReviewRepository repository;
    private final ProductReviewValidator productReviewValidator;
    private final RestTemplate restTemplate;
    private final ProductServiceClient productServiceClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private ModelMapper modelMapper;
    
    @Autowired
    public ProductReviewController(ProductReviewRepository repository, ProductReviewValidator validator, RestTemplate restTemplate, ProductServiceClient productServiceClient, Resilience4JCircuitBreakerFactory circuitBreakerFactory, ModelMapper modelMapper) 
    {
        this.repository = repository;
        this.productReviewValidator = validator;
        this.productServiceClient = productServiceClient;
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
        this.modelMapper = modelMapper;
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
    public ResponseEntity<ProductReviewDTO.Response.PublicWithProduct> getWithProduct(@PathVariable Long id) 
    {
        log.info("Finding product review with product info: {}", id);
    	ProductReview productReview = getById(id);

    	//Exemplo de restTemplate: 
        //Product product = restTemplate.getForObject(String.format("https://PRODUCT-SERVICE/products/%s", productReview.getProductId()), Product.class);
        
        Resilience4JCircuitBreaker circuitBreaker =  circuitBreakerFactory.create("product");
        java.util.function.Supplier<ProductDTO> productSupplier = () -> productServiceClient.getProductById(productReview.getProductId()); 
        ProductDTO product = circuitBreaker.run(productSupplier, throwable -> handleProductServiceNotAvailable());
        
        ProductReviewDTO.Response.PublicWithProduct productReviewDTO =  modelMapper.map(productReview,  ProductReviewDTO.Response.PublicWithProduct.class);
        productReviewDTO.setProduct(product);
        
        return new ResponseEntity<>(productReviewDTO, HttpStatus.OK);
    }
    
    private ProductDTO handleProductServiceNotAvailable() 
    {
		throw new ServiceUnavailableException();
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
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);        
    }
    
    @Operation(summary = "Update a product review")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "202", description = "Product review updated", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductReview.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid product review information to update", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ObjectError.class)) })
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_UPDATE')")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody ProductReviewDTO.Request.Update productReviewDTO) throws IOException 
    {
    	log.info("Updating  product: {}", id);
    	
        ProductReview productReview = getById(id);
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
    public HttpEntity<?> delete(@PathVariable("id") Long id) 
    {
    	log.info("Deleting  product review: {}", id);
        repository.delete(getById(id));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    static class ServiceUnavailableException extends RuntimeException {

		private static final long serialVersionUID = 7310312040927768939L;
    }
    
    private ProductReview getById(Long id)
    {
    	return repository.findById(id).orElseThrow(CustomRestExceptions.ProductReviewNotFoundException::new);
    }
    
}