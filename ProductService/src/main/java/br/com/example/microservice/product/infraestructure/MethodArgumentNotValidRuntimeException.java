package br.com.example.microservice.product.infraestructure;

import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;


public class MethodArgumentNotValidRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1043621711381044311L;
	private final BindingResult bindingResult;
	
	/**
	 * Create a new BindException instance for a BindingResult.
	 * @param bindingResult the BindingResult instance to wrap
	 */
	public MethodArgumentNotValidRuntimeException(String message, BindingResult bindingResult) {
		super(message);
		Assert.notNull(bindingResult, "BindingResult must not be null");
		this.bindingResult = bindingResult;
	}

	/**
	 * Return the BindingResult that this BindException wraps.
	 */
	public final BindingResult getBindingResult() {
		return this.bindingResult;
	}
	
	
}
