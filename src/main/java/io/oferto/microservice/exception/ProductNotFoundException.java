package io.oferto.microservice.exception;

public class ProductNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ProductNotFoundException(Long id) {
        super("Product id not found : " + id);
    }
}
