package br.com.example.microservice.productreview.domain;

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
@Table(name="product_reviews")
public class ProductReview {

    @Id
    @Column(name = "product_review_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")    
    private UUID productReviewId;
    
    @NotBlank(message = "The author name is required")
    private String authorName;

    @Size( max = 500, message="The review must be less then 500 characters")
    private String review;

    @Column(name = "product_id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID productId;
    
    public void validate(ProductReviewValidator validator) 
    {
    	DataBinder binder = new DataBinder(this);
        binder.addValidators(validator);
        binder.validate();
        BindingResult validationResult = binder.getBindingResult();
        if (validationResult.hasErrors()) 
        {
        	throw new MethodArgumentNotValidRuntimeException("Invalid product review arguments", validationResult);
        }        
    }
}