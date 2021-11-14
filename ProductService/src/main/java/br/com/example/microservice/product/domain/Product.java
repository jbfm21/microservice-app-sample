package br.com.example.microservice.product.domain;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
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
@Table(name="products")
public class Product {

    @Id
    @Column(name = "product_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID productId;
    
    @NotBlank(message = "Product name is required")
    private String productName;

    @Size(min = 1, max = 100, message="The short description must be 1 to 100 characters long.")
    private String shortDescription;
    
    @Size( max = 500, message="The long description must be less then 500 characters")
    private String longDescription;
    
    private BigDecimal price;
    
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