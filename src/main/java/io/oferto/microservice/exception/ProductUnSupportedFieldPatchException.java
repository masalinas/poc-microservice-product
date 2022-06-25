package io.oferto.microservice.exception;

import java.util.Set;

public class ProductUnSupportedFieldPatchException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public ProductUnSupportedFieldPatchException(Set<String> keys) {
        super("Field " + keys.toString() + " update is not allow.");
    }

}
