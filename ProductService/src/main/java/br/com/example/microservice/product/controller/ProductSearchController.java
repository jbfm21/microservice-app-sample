package br.com.example.microservice.product.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.example.microservice.product.domain.Product;
import br.com.example.microservice.product.dto.ProductDTO;
import br.com.example.microservice.product.infraestructure.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/search")
//@RefreshScope -> Descomentar caso deseje que as configurações sejam atualizadas dinamicamentes caso sejam atualizadas no repositório de configurações
public class ProductSearchController {
    
	private final ProductRepository repository;
	private ModelMapper modelMapper;
	
    
    @Autowired
    public ProductSearchController(ProductRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }
    
    @Operation(summary = "Search a product by name or long description term ")
    @ApiResponses(value = { 
 	      @ApiResponse(responseCode = "202", description = "Product review deleted", content = { @Content(mediaType = "application/json",  schema = @Schema(implementation = ProductDTO.Response.Public.class)) })
    })
    
    @PreAuthorize("hasRole('PRF_PRODUCT_FINDALL')")
    @GetMapping()
    public ResponseEntity<List<ProductDTO.Response.Public>> search(@RequestParam("q") String queryTerm) 
    {
        List<Product> result =  Optional.of(repository.search(String.format("%%%s%%", queryTerm))).orElse(new ArrayList<>());
        List<ProductDTO.Response.Public> resultDTO = result.stream().map(p -> modelMapper.map(p, ProductDTO.Response.Public.class)).toList();
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}