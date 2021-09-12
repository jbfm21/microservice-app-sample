package br.com.example.microservice.product.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.example.microservice.product.domain.Product;
import br.com.example.microservice.product.domain.ProductValidator;
import br.com.example.microservice.product.infraestructure.ProductRepository;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/products")
@Log4j2
@RestControllerAdvice 
public class ProductController 
{
    private final ProductRepository repository;
    private final ProductValidator validator;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public ProductController(ProductRepository repository, ProductValidator validator, ObjectMapper objectMapper) {
        this.repository = repository;
        this.validator = validator;
        this.objectMapper = objectMapper;
    }
    
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @GetMapping()
    @PreAuthorize("hasRole('PRF_PRODUCT_FINDALL')")
    public  ResponseEntity<Page<Product>> findAll(
    	@RequestParam(value = "page", defaultValue = "0", required = false) int page,
	    @RequestParam(value = "count", defaultValue = "10", required = false) int count,
	    @RequestParam(value = "order", defaultValue = "ASC", required = false) Sort.Direction direction,
	    @RequestParam(value = "sort", defaultValue = "productName", required = false) String sortProperty) 
    {
        Page<Product> result = repository.findAll(PageRequest.of(page, count, Sort.by(direction, sortProperty)));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_GET')")
    public ResponseEntity<Product> get(@PathVariable Long id) 
    {
        log.info("Finding  product: {}", id);
    	Optional<Product> product = repository.findById(id);
        if (!product.isPresent()) 
        {
            throw new ProductNotFoundException();
        } 
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }
    
    @PostMapping()
    @PreAuthorize("hasRole('PRF_PRODUCT_CREATE')")
    public ResponseEntity<Product> create(@RequestBody @Valid Product product) 
    {
    	log.info("Creating  product: {}", product);
    	Product savedEntity = repository.save(product);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
        
    }
    
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_UPDATE')")
    public HttpEntity<?> update(@PathVariable("id") Long id, HttpServletRequest request) throws IOException {
    	log.info("Updating  product: {}", id);
    	
        Product existing = get(id).getBody();
        Product updated = objectMapper.readerForUpdating(existing).readValue(request.getReader());
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        propertyValues.add("productId", updated.getProductId());
        propertyValues.add("productName", updated.getProductName());
        propertyValues.add("shortDescription", updated.getShortDescription());
        propertyValues.add("longDescription", updated.getLongDescription());
        propertyValues.add("inventoryId", updated.getInventoryId());
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
    @PreAuthorize("hasRole('PRF_PRODUCT_DELETE')")
    public HttpEntity<?> delete(@PathVariable("id") Long id) 
    {
    	log.info("Deleting  product: {}", id);
    	Product product = get(id).getBody();
        repository.delete(product);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
     
    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class ProductNotFoundException extends RuntimeException {

		private static final long serialVersionUID = -1547073032113405697L;
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
}