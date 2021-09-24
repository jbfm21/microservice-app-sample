package br.com.example.microservice.product.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import br.com.example.microservice.infraestructure.exceptions.MethodArgumentNotValidRuntimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor @Builder  @AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    
    @NotBlank(message = "Nome é obrigatório")
    private String productName;

    @Size(min = 1, max = 100, message="A descrição curta deve ter entre 1 a 100 caracteres")
    private String shortDescription;
    
    @Size( max = 500, message="A descrição longa deve ter no máximo 100 caracteres")
    private String longDescription;
    
    private String inventoryId;
    
    
    public void validate(ProductValidator validator) 
    {
    	DataBinder binder = new DataBinder(this);
        binder.addValidators(validator);
        binder.validate();
        BindingResult validationResult = binder.getBindingResult();
        if (validationResult.hasErrors()) 
        {
        	throw new MethodArgumentNotValidRuntimeException("Invalid product arguments", validationResult);
        }        
    }
    
}