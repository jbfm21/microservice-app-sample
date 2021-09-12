package br.com.example.microservice.productreview.infraestructure;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.example.microservice.productreview.domain.ProductReview;

@Repository
public interface ProductReviewRepository extends PagingAndSortingRepository<ProductReview, Long> 
{
}
