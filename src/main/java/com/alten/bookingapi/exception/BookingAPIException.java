/**
 * 
 */
package com.alten.bookingapi.exception;

import java.util.Set;

import org.springframework.http.HttpStatus;

import lombok.NoArgsConstructor;

/**
 * @author Danilo Cavalcanti
 *
 */
@NoArgsConstructor
public abstract class BookingAPIException extends Exception {
	
	private static final long serialVersionUID = 5699414681384353363L;
	
	public BookingAPIException(String message) {
		
		super(message);
	}

	public abstract Set<String> getMessages();
	
	public abstract HttpStatus getStatusCode();

}
