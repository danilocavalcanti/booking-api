package com.alten.bookingapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.body.response.BookingResponseBody;
import com.alten.bookingapi.body.response.SuccessResponseBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.service.BookingService;

/**
 * @author Danilo Cavalcanti
 *
 */
@RestController
@RequestMapping("/bookings")
public class BookingController {
	
	private static final String CONSUMES = "application/json";
	
	private static final String PRODUCES = "application/json";
	
	@Autowired
	private BookingService service;
	
	@RequestMapping(method = RequestMethod.HEAD)
	public ResponseEntity<?> checkAvailability(@RequestParam String startDate, @RequestParam String endDate, @RequestParam Integer roomId) throws GenericException, BusinessException {
		
		return service.checkDatesAvailability(startDate, endDate, roomId);
	}
	
	@GetMapping(produces = PRODUCES)
	public ResponseEntity<SuccessResponseBody<List<BookingResponseBody>>> getAll() throws GenericException {
		
		return service.getAll();
	}
	
	@GetMapping(value = "/{id}", produces = PRODUCES)
	public ResponseEntity<SuccessResponseBody<BookingResponseBody>> get(@PathVariable Long id) throws GenericException, BusinessException {
		
		return service.get(id);
	}
	
	@DeleteMapping(value = "/{id}", produces = PRODUCES)
	public ResponseEntity<?> delete(@PathVariable Long id) throws GenericException, BusinessException {
		
		return service.cancel(id);
	}
	
	@PostMapping(consumes = CONSUMES, produces = PRODUCES)
	public ResponseEntity<?> create(@RequestBody BookingRequestBody booking) throws GenericException, BusinessException {
		
		return service.create(booking);
	}
	
	@PatchMapping(value = "/{id}", consumes = CONSUMES, produces = PRODUCES)
	public ResponseEntity<SuccessResponseBody<BookingResponseBody>> update(@PathVariable Long id, @RequestBody BookingRequestBody booking) throws GenericException, BusinessException {
		
		return service.update(id, booking);
	}
	
}
