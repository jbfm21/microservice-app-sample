package br.com.example.microservice.productreview.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.MutablePropertyValues;
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
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.example.microservice.productreview.client.ProductServiceClient;
import br.com.example.microservice.productreview.domain.ProductReview;
import br.com.example.microservice.productreview.domain.ProductReviewValidator;
import br.com.example.microservice.productreview.infraestructure.ProductReviewRepository;
import br.com.example.microservice.productreview.vo.Product;
import br.com.example.microservice.productreview.vo.ResponseTemplateVO;
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
    private final ProductReviewValidator validator;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ProductServiceClient productServiceClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    
    @Autowired
    public ProductReviewController(ProductReviewRepository repository, ProductReviewValidator validator, ObjectMapper objectMapper, RestTemplate restTemplate, ProductServiceClient productServiceClient, Resilience4JCircuitBreakerFactory circuitBreakerFactory) 
    {
        this.repository = repository;
        this.validator = validator;
        this.objectMapper = objectMapper;
        this.productServiceClient = productServiceClient;
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_FINDALL')")
    @GetMapping()
    public  ResponseEntity<Page<ProductReview>> findAll(
    	@RequestParam(value = "page", defaultValue = "0", required = false) int page,
	    @RequestParam(value = "count", defaultValue = "10", required = false) int count,
	    @RequestParam(value = "order", defaultValue = "ASC", required = false) Sort.Direction direction,
	    @RequestParam(value = "sort", defaultValue = "authorName", required = false) String sortProperty) 
    {
        Page<ProductReview> result = repository.findAll(PageRequest.of(page, count, Sort.by(direction, sortProperty)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
  
    
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_GET')")
    @Operation(summary = "Get a Product Review by its id")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found the product Review", 
        content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductReview.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",  content = @Content), 
      @ApiResponse(responseCode = "404", description = "Product Review not found", content = @Content) })
    public ResponseEntity<ResponseTemplateVO> getWithProduct(@PathVariable Long id) 
    {
        log.info("Finding product review with product info: {}", id);
    	Optional<ProductReview> optionalProductReview = repository.findById(id);
        if (!optionalProductReview.isPresent()) 
        {
            throw new ProductReviewNotFoundException();
        }
        
        ProductReview productReview = optionalProductReview.get();

        //Exemplo de restTemplate: 
        //Product product = restTemplate.getForObject(String.format("https://PRODUCT-SERVICE/products/%s", productReview.getProductId()), Product.class);
        
        Resilience4JCircuitBreaker circuitBreaker =  circuitBreakerFactory.create("product");
        java.util.function.Supplier<Product> productSupplier = () -> productServiceClient.getProductById(productReview.getProductId()); 
        Product product = circuitBreaker.run(productSupplier, throwable -> handleProductServiceNotAvailable());
        
        ResponseTemplateVO responseTemplate = ResponseTemplateVO.builder()
        		.product(product)
        		.productReview(productReview).build();
        
        return new ResponseEntity<>(responseTemplate, HttpStatus.OK);
    }
    
    private Product handleProductServiceNotAvailable() 
    {
		throw new ServiceUnavailableException();
	}

	@PostMapping()
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_CREATE')")
    public ResponseEntity<ProductReview> create(@RequestBody @Valid ProductReview detail) {
    	log.info("Creating product review: {}", detail);
    	ProductReview savedEntity = repository.save(detail);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
        
    }
    
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_UPDATE')")
    public HttpEntity<?> update(@PathVariable("id") Long id, HttpServletRequest request) throws IOException 
    {
    	log.info("Updating product: {}", id);
    	
    	ProductReview existing = findById(id);
    	
    	ProductReview updated = objectMapper.readerForUpdating(existing).readValue(request.getReader());
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("productId", updated.getProductId());
        propertyValues.add("authorName", updated.getAuthorName());
        propertyValues.add("review", updated.getReview());
        propertyValues.add("productReviewId", updated.getProductReviewId());
        DataBinder binder = new DataBinder(updated);
        binder.addValidators(validator);
        binder.bind(propertyValues);
        binder.validate();
        if (binder.getBindingResult().hasErrors()) 
        {
            return new ResponseEntity<>(binder.getBindingResult().getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        
        updated = repository.save(updated);
    	return new ResponseEntity<>(updated, HttpStatus.ACCEPTED);
    }
    
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_REVIEW_DELETE')")
    public HttpEntity<?> delete(@PathVariable("id") Long id) 
    {
    	log.info("Removing product review: {}", id);
    	ProductReview productReview = findById(id);
        repository.delete(productReview);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
     
    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class ProductReviewNotFoundException extends RuntimeException {

		private static final long serialVersionUID = 7310312040927768939L;
    }
    
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    static class ServiceUnavailableException extends RuntimeException {

		private static final long serialVersionUID = 7310312040927768939L;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) 
    {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> 
        {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    
    private ProductReview findById(Long id) 
    {
    	Optional<ProductReview> optionalProductReview = repository.findById(id);
        if (!optionalProductReview.isPresent()) 
        {
            throw new ProductReviewNotFoundException();
        }
        
        return optionalProductReview.get();
    }
    
}