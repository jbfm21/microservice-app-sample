package br.com.example.microservice.product.infraestructure;

import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;


public class MethodArgumentNotValidRuntimeException extends RuntimeException {

	private final BindingResult bindingResult;
	
	/**
	 * Create a new BindException instance for a BindingResult.
	 * @param bindingResult the BindingResult instance to wrap
	 */
	public MethodArgumentNotValidRuntimeException(BindingResult bindingResult) {
		Assert.notNull(bindingResult, "BindingResult must not be null");
		this.bindingResult = bindingResult;
	}

	/**
	 * Create a new BindException instance for a target bean.
	 * @param target the target bean to bind onto
	 * @param objectName the name of the target object
	 * @see BeanPropertyBindingResult
	 */
	public MethodArgumentNotValidRuntimeException(Object target, String objectName) {
		Assert.notNull(target, "Target object must not be null");
		this.bindingResult = new BeanPropertyBindingResult(target, objectName);
	}


	/**
	 * Return the BindingResult that this BindException wraps.
	 */
	public final BindingResult getBindingResult() {
		return this.bindingResult;
	}
	
	
}
