package br.com.example.microservice.product.infraestructure;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError 
{
    private HttpStatus status;
    private String message;
    private List<FieldError> fieldErros; 
}
