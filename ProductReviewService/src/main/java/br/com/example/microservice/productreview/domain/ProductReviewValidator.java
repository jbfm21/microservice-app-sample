package br.com.example.microservice.productreview.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductReviewValidator implements Validator
{

    @Autowired
    public ProductReviewValidator() {
    }
    
    @Override
    public boolean supports(Class clazz) 
    {
        return ProductReview.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
    	ProductReview detail = (ProductReview) target;
        if (detail.getProductId() == null)
        {
        	errors.rejectValue("productId", "product.id.invalid", "ID do Produto inválido");
        }
    }
}