package br.com.example.microservice.product.controller;


import java.io.IOException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import br.com.example.microservice.product.domain.Product;
import br.com.example.microservice.product.domain.ProductValidator;
import br.com.example.microservice.product.dto.ProductDTO;
import br.com.example.microservice.product.infraestructure.ProductRepository;
import br.com.example.microservice.product.infraestructure.bootstrap.CustomRestExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/products")
@Log4j2
@RestControllerAdvice 
//@RefreshScope -> Descomentar caso deseje que as configurações sejam atualizadas dinamicamentes caso sejam atualizadas no repositório de configurações
public class ProductController 
{
    private final ProductRepository repository;
    private final ProductValidator productValidator;
	private ModelMapper modelMapper;    
    
    @Autowired
    public ProductController(ProductRepository repository, ProductValidator validator, ModelMapper modelMapper) {
        this.repository = repository;
        this.productValidator = validator;
        this.modelMapper = modelMapper; 
    }
    
    @Operation(summary = "Find all products")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found at least on product", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductDTO.Response.Public.class)) })
    })
    @GetMapping()
    @PreAuthorize("hasRole('PRF_PRODUCT_FINDALL')")
    public  ResponseEntity<Page<ProductDTO.Response.Public>> findAll(
    	@RequestParam(value = "page", defaultValue = "0", required = false) int page,
	    @RequestParam(value = "count", defaultValue = "10", required = false) int count,
	    @RequestParam(value = "order", defaultValue = "ASC", required = false) Sort.Direction direction,
	    @RequestParam(value = "sort", defaultValue = "productName", required = false) String sortProperty) 
    {
        Page<Product> result = repository.findAll(PageRequest.of(page, count, Sort.by(direction, sortProperty)));
        Page<ProductDTO.Response.Public> resultDTO = result.map(product -> modelMapper.map(product, ProductDTO.Response.Public.class));
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
    
    @Operation(summary = "Get a product by its id")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "200", description = "Found the product", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductDTO.Response.Public.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid id supplied",  content = @Content), 
      @ApiResponse(responseCode = "404", description = "Product not found", content = @Content) 
    })
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_GET')")
    public ResponseEntity<ProductDTO.Response.Public> get(@PathVariable Long id) 
    {
        log.info("Finding  product: {}", id);
    	Product product = getById(id); 
    	/*try {
			Thread.sleep(10000000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return new ResponseEntity<>(modelMapper.map(product,  ProductDTO.Response.Public.class), HttpStatus.OK);
    }
    
    @Operation(summary = "Create a product")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "201", description = "Product created", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = Product.class)) })
    })
    @PostMapping()
    @PreAuthorize("hasRole('PRF_PRODUCT_CREATE')")
    public ResponseEntity<Object> create(@RequestBody ProductDTO.Request.Create productDTO) 
    {
    	log.info("Creating  product: {}", productDTO);
    	Product product = modelMapper.map(productDTO, Product.class);
    	product.validate(productValidator);
    	
    	Product savedEntity = repository.save(product);
        return new ResponseEntity<>(savedEntity, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Update a product")
    @ApiResponses(value = { 
      @ApiResponse(responseCode = "202", description = "Product updated", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductDTO.Response.Public.class)) }),
      @ApiResponse(responseCode = "400", description = "Invalid product information to update", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ObjectError.class)) })
    })
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_UPDATE')")
    public HttpEntity<Object> update(@PathVariable("id") Long id, @RequestBody ProductDTO.Request.Update productDTO) throws IOException {
    	log.info("Updating  product: {}", id);
    	
        Product product = getById(id);
        Utils.merge(productDTO, product);
        product.validate(productValidator);
        product = repository.save(product);
    	return new ResponseEntity<>(modelMapper.map(product, ProductDTO.Response.Public.class), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Delete a product")
    @ApiResponses(value = { 
 	      @ApiResponse(responseCode = "202", description = "Product deleted", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = Product.class)) })
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasRole('PRF_PRODUCT_DELETE')")
    public HttpEntity<String> delete(@PathVariable("id") Long id) 
    {
    	log.info("Deleting  product: {}", id);
        repository.delete(getById(id));
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
    
    private Product getById(Long id)
    {
    	return repository.findById(id).orElseThrow(CustomRestExceptions.ProductNotFoundException::new);
    }
    
  

}