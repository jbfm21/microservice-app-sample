package br.com.example.microservice.productreview.infraestructure.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.productreview.domain.ProductReview;
import br.com.example.microservice.productreview.infraestructure.exception.BusinessExceptions;

@Repository
public interface ProductReviewRepository extends PagingAndSortingRepository<ProductReview, UUID> 
{
	default ProductReview findByIdOrNotFoundException(UUID id) 
	{
		return findById(id).orElseThrow(BusinessExceptions.ProductReviewNotFoundException::new);
    }

}
