package br.com.example.microservice.product.infraestructure;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.product.domain.Product;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> 
{
	 @Query("select p from Product p where UPPER(p.productName) like UPPER(?1) or UPPER(p.longDescription) like UPPER(?1)")
	 List search(String term);
}

