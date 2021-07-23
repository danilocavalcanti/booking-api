/**
 * 
 */
package com.alten.bookingapi;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.alten.bookingapi.body.response.BookingResponseBody;
import com.alten.bookingapi.body.response.SuccessResponseBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.model.BookingStatus;
import com.alten.bookingapi.service.BookingService;
import com.alten.bookingapi.util.DateUtil;

/**
 * @author Danilo Cavalcanti
 *
 */
public class CrudUnitTests extends BookingUnitTests {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;

	@Autowired
	private BookingService service;
	
	private ResponseEntity<SuccessResponseBody<BookingResponseBody>> booking;
	
	private Long createdId;
	
	@Test
	@Order(1)
	void create() {
		
		assertDoesNotThrow(() -> {
			
			booking = service.create(getRequest());
		});
		
		assertNotNull(booking);
		
		assertNotNull(booking.getBody());
		
		assertEquals(booking.getBody().getResult().getStatus(), BookingStatus.ACTIVE);
		
		assertEquals(booking.getStatusCode(), HttpStatus.CREATED);
	}
	
	@Test
	@Transactional
	@Order(2)
	void getAll() throws GenericException {
		
		ResponseEntity<SuccessResponseBody<List<BookingResponseBody>>> response = service.getAll();
		
		assertNotNull(response);
		
		assertNotNull(response.getBody());
		
		assertEquals(1, response.getBody().getResult().size());
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
		createdId = response.getBody().getResult().stream().findAny().get().getId();
	}
	
	@Test
	@Transactional
	@Order(3)
	void getOne() throws GenericException, BusinessException {
		
		ResponseEntity<SuccessResponseBody<BookingResponseBody>> response = service.get(createdId);
		
		assertNotNull(response);
		
		assertNotNull(response.getBody());
		
		assertEquals(createdId, response.getBody().getResult().getId());
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@Transactional
	@Order(4)
	void update() throws GenericException, BusinessException {
		
		LocalDate startDate = LocalDate.now().plusDays(minimumStartDate + maximumLengthOfStay);
		
		LocalDate endDate = startDate.plusDays(maximumLengthOfStay - 1);
		
		String startDateString = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String endDateString = endDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		getRequest().setUserId(0L);
		
		getRequest().setStartDate(startDateString);
		
		getRequest().setEndDate(endDateString);
		
		ResponseEntity<SuccessResponseBody<BookingResponseBody>> response = service.update(createdId, getRequest());
		
		assertNotNull(response);
		
		assertNotNull(response.getBody());
		
		assertEquals(0L, response.getBody().getResult().getUser().getId());
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@Transactional
	@Order(5)
	void cancel() throws GenericException, BusinessException {
		
		ResponseEntity<?> response = service.cancel(createdId);
		
		ResponseEntity<SuccessResponseBody<BookingResponseBody>> booking = service.get(createdId);
		
		assertNotNull(booking);
		
		assertNotNull(booking.getBody());
		
		assertEquals(booking.getBody().getResult().getStatus(), BookingStatus.CANCELED);
		
		assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
	}

}
