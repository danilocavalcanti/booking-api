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
import org.springframework.web.bind.annotation.RestController;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.body.response.BookingResponseBody;
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
	
	@Autowired
	private BookingService service;
	
	@GetMapping
	public ResponseEntity<List<BookingResponseBody>> getAll() throws GenericException {
		
		return service.getAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<BookingResponseBody> get(@PathVariable Long id) throws GenericException, BusinessException {
		
		return service.get(id);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) throws GenericException, BusinessException {
		
		return service.cancel(id);
	}
	
	@PostMapping
	public ResponseEntity<BookingResponseBody> create(@RequestBody BookingRequestBody booking) throws GenericException, BusinessException {
		
		return service.create(booking);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<BookingResponseBody> update(@PathVariable Long id, @RequestBody BookingRequestBody booking) throws GenericException, BusinessException {
		
		return service.update(id, booking);
	}

}
