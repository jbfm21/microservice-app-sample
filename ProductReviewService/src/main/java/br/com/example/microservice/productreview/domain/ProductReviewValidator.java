package br.com.example.microservice.productreview.domain;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

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
    public void validate(Object target, Errors errors) 
    {
    	ProductReview product = (ProductReview) target;
        if (product.getProductId() == null)
        {
        	errors.rejectValue("productId", "productId.invalid", "Identificador do produto inválido");
        }
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<ProductReview>> violations = factory.getValidator().validate(product);
        violations.forEach(e->

        	errors.rejectValue(
        			e.getPropertyPath().toString(), 
        			String.format("%s.invalid",e.getPropertyPath()), 
        			String.format("%s: %s", e.getMessage(), e.getInvalidValue()))
        );
    }
    
}