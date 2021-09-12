package br.com.example.microservice.productreview.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor @Builder  @AllArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productReviewId;
    
    @NotBlank(message = "Nome é obrigatório")
    private String authorName;

    @Size( max = 500, message="A review deve ter no máximo 100 caracteres")
    private String review;
    
    private Long productId;
}