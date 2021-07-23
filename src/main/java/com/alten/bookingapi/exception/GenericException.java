package com.alten.bookingapi.exception;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Danilo Cavalcanti
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GenericException extends BookingAPIException {

	private static final long serialVersionUID = 3705554846424346326L;
	
	private static final HttpStatus DEFAULT_STATUS_CODE = HttpStatus.INTERNAL_SERVER_ERROR;

	private Set<String> messages;
	
	private HttpStatus statusCode;
	
	public GenericException(String message) {
		
		super(message);

		Set<String> messages = new HashSet<String>();
		
		messages.add(message);
		
		setStatusCode(DEFAULT_STATUS_CODE);
	}

	public GenericException(Exception e) {
		
		super(e.getLocalizedMessage());

		Set<String> messages = new HashSet<String>();
		
		messages.add(e.getLocalizedMessage());
		
		setStatusCode(DEFAULT_STATUS_CODE);
	}

	public GenericException(Exception e, HttpStatus statusCode) {
		
		super(e.getLocalizedMessage());

		Set<String> messages = new HashSet<String>();
		
		messages.add(e.getLocalizedMessage());

		setStatusCode(statusCode);
	}

	public GenericException(Exception e, int statusCode) {
		
		super(e.getLocalizedMessage());

		Set<String> messages = new HashSet<String>();
		
		messages.add(e.getLocalizedMessage());

		setStatusCode(HttpStatus.resolve(statusCode));
	}
}
