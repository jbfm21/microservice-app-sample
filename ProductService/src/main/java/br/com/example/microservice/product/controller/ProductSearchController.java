package br.com.example.microservice.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.example.microservice.product.infraestructure.ProductRepository;

@RestController
@RequestMapping("/search")
public class ProductSearchController {
    
	private final ProductRepository repository;
    
    @Autowired
    public ProductSearchController(ProductRepository repository) {
        this.repository = repository;
    }
    
    @PreAuthorize("hasRole('PRF_PRODUCT_FINDALL')")
    @RequestMapping(method = RequestMethod.GET)
    public List<?> search(@RequestParam("q") String queryTerm) {
        List<?> productDetails = repository.search("%"+queryTerm+"%");
        return productDetails == null ? new ArrayList<>() : productDetails;
    }
}