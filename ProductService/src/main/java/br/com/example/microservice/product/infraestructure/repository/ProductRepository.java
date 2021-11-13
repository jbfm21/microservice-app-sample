package br.com.example.microservice.product.infraestructure.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.product.domain.Product;
import br.com.example.microservice.product.infraestructure.exception.BusinessExceptions;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, UUID> 
{
	@Query("select p from Product p where UPPER(p.productName) like UPPER(?1) or UPPER(p.longDescription) like UPPER(?1)")
	List<Product> search(String term);
	 
	default Product findByIdOrNotFoundException(UUID id) 
	{
    	return findById(id).orElseThrow(BusinessExceptions.ProductNotFoundException::new);
    }
}

