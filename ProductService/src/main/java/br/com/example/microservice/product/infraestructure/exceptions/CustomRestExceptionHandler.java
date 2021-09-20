package br.com.example.microservice.product.infraestructure.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.example.microservice.product.infraestructure.exceptions.CustomRestExceptions.ApiException;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    // 400
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

    	final List<FieldError> errors = new ArrayList<>();
        
        errors.addAll(ex.getBindingResult().getFieldErrors());
        
        errors.addAll(ex.getBindingResult().getGlobalErrors().stream().map(e-> new FieldError(e.getObjectName(), null, e.getDefaultMessage())).toList());
        
        final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(ex.getLocalizedMessage()).fieldErros(errors).build();
        
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    	final List<FieldError> errors = new ArrayList<>();
        
        errors.addAll(ex.getBindingResult().getFieldErrors());
        
        errors.addAll(ex.getBindingResult().getGlobalErrors().stream().map(e-> new FieldError(e.getObjectName(), null, e.getDefaultMessage())).toList());

        final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(ex.getLocalizedMessage()).fieldErros(errors).build();
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String error = String.format("%s value for %s should be of type %s", ex.getValue(), ex.getPropertyName(), ex.getRequiredType());

        final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(String.format("%s %s", ex.getLocalizedMessage(), error)).build();
        
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String error = String.format("%s part is missing", ex.getRequestPartName());
        final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(String.format("%s %s", ex.getLocalizedMessage(), error)).build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    	final String error = String.format("%s parameter is missing", ex.getParameterName());
    	final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(String.format("%s %s", ex.getLocalizedMessage(), error)).build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    //

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex, final WebRequest request) {
    	final String error = String.format("%s should be of type %s", ex.getName(), Optional.of(ex.getRequiredType()).map(Class::getName));
    	final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(String.format("%s %s", ex.getLocalizedMessage(), error)).build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    
    
    @ExceptionHandler({ MethodArgumentNotValidRuntimeException.class })
    public ResponseEntity<Object> handleDataValidationException(final MethodArgumentNotValidRuntimeException ex, final WebRequest request) 
    {
    	final List<FieldError> errors = new ArrayList<>();
        
        errors.addAll(ex.getBindingResult().getFieldErrors());
        
        errors.addAll(ex.getBindingResult().getGlobalErrors().stream().map(e-> new FieldError(e.getObjectName(), null, e.getDefaultMessage())).toList());
        
        final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(ex.getLocalizedMessage()).fieldErros(errors).build();
        
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }
    

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) 
    {
    	final List<String> errors = ex.getConstraintViolations().stream().map(violation-> String.format("%s %s : %s", violation.getRootBeanClass().getName(), violation.getPropertyPath(), violation.getMessage())).toList();
    	final ApiError apiError = ApiError.builder().status(HttpStatus.BAD_REQUEST).message(String.format("%s %s", ex.getLocalizedMessage(), errors)).build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 404
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String error = String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL());
        final ApiError apiError = ApiError.builder().status(HttpStatus.NOT_FOUND).message(String.format("%s %s", ex.getLocalizedMessage(), error)).build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 405
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    	final String error = String.format("%s method is not supported for this request.", ex.getMethod());
        final ApiError apiError = ApiError.builder().status(HttpStatus.METHOD_NOT_ALLOWED).message(String.format("%s %s", ex.getLocalizedMessage(), error)).build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 415
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
    	final String error = String.format("%s media type is not supported.", ex.getContentType());
        final ApiError apiError = ApiError.builder().status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).message(String.format("%s %s", ex.getLocalizedMessage(), error)).build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
    

    @ExceptionHandler({ ApiException.class })
    public ResponseEntity<Object> handleApiBusinessException(final ApiException ex, final WebRequest request) 
    {
    	throw ex;
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) 
    {
        logger.error("error", ex);
        final ApiError apiError = ApiError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).message(String.format("Error Ocurred. %s.", ex.getLocalizedMessage())).build();
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

}
