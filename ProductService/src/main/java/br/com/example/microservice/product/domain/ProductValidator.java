package br.com.example.microservice.product.domain;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator
{
    
    @Override
    public boolean supports(Class<?> clazz) 
    {
        return Product.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) 
    {
    	Product product = (Product) target;
    	
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0)
        {
        	errors.rejectValue("price", "price.invalid", "Invalid price");	
        }
        
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<Product>> violations = factory.getValidator().validate(product);
        violations.forEach(e->

        	errors.rejectValue(
        			e.getPropertyPath().toString(), 
        			String.format("%s.invalid",e.getPropertyPath()), 
        			String.format("%s: %s", e.getMessage(), e.getInvalidValue()))
        );
    }
}