package br.com.example.microservice.product.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator
{

    @Autowired
    public ProductValidator() {
    }
    
    @Override
    public boolean supports(Class clazz) 
    {
        return Product.class.isAssignableFrom(clazz);
    }
    
    @Override
    public void validate(Object target, Errors errors) {
        Product detail = (Product) target;
        if (!StringUtils.hasText(detail.getInventoryId()))
        {
        	errors.rejectValue("inventoryId", "inventory.id.invalid", "ID de Estoque inválido");
        }
    }
}