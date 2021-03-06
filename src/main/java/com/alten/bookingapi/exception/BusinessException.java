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
public class BusinessException extends BookingAPIException {

	private static final long serialVersionUID = -8622653774415163146L;
	
	private static final HttpStatus DEFAULT_STATUS_CODE = HttpStatus.BAD_REQUEST;

	private Set<String> messages;

	private HttpStatus statusCode;
	
	public BusinessException(String message, HttpStatus statusCode) {
		
		super(message);
		
		Set<String> messages = new HashSet<String>();
		
		messages.add(message);

		setMessages(messages);
		
		setStatusCode(statusCode);
		
		setStatusCode(DEFAULT_STATUS_CODE);
	}
	
	public BusinessException(Set<String> messages) {

		super(String.join(" | ", messages));

		setMessages(messages);
		
		setStatusCode(DEFAULT_STATUS_CODE);
				
	}
	
	public BusinessException(String message) {
		
		super(message);

		Set<String> messages = new HashSet<String>();
		
		messages.add(message);

		setMessages(messages);
		
		setStatusCode(DEFAULT_STATUS_CODE);
	}

	public BusinessException(Exception e) {
		
		super(e.getLocalizedMessage());

		Set<String> messages = new HashSet<String>();
		
		messages.add(e.getLocalizedMessage());

		setMessages(messages);
		
		setStatusCode(DEFAULT_STATUS_CODE);
	}

	public BusinessException(Exception e, HttpStatus statusCode) {
		
		super(e.getLocalizedMessage());

		Set<String> messages = new HashSet<String>();
		
		messages.add(e.getLocalizedMessage());
		
		setStatusCode(statusCode);
	}
	
	public BusinessException(Exception e, int statusCode) {
		
		super(e.getLocalizedMessage());

		Set<String> messages = new HashSet<String>();
		
		messages.add(e.getLocalizedMessage());
		
		setStatusCode(HttpStatus.resolve(statusCode));
	}

}
