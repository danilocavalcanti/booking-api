/**
 * 
 */
package com.alten.bookingapi.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.alten.bookingapi.body.response.ErrorResponseBody;
import com.alten.bookingapi.exception.BookingAPIException;

/**
 * @author Danilo Cavalcanti
 *
 */
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { BookingAPIException.class })
	protected ResponseEntity<ErrorResponseBody> handleConflict(BookingAPIException exception) {
		
		return ResponseEntity.status(exception.getStatusCode()).body(new ErrorResponseBody(exception.getMessages()));
	}

}
