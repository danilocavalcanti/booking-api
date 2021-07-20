/**
 * 
 */
package com.alten.bookingapi;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.body.response.BookingResponseBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.service.BookingService;
import com.alten.bookingapi.util.DateUtil;

/**
 * @author Danilo Cavalcanti
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookingTests extends BookingApiApplicationTests {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;
	
	@Autowired
	private BookingService service;
	
	private static BookingRequestBody request;
	
	@BeforeAll
	void before() {
		
		LocalDate startDate = LocalDate.now().plusDays(minimumStartDate);
		
		LocalDate endDate = startDate.plusDays(maximumLengthOfStay);
		
		String startDateString = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String endDateString = endDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		request = new BookingRequestBody(1L, 1, startDateString, endDateString);
	}
	
	@Test
	void create() {
		
		assertDoesNotThrow(() -> service.create(request));
	}
	
	@Test
	@Transactional
	void getAll() throws GenericException {
		
		ResponseEntity<List<BookingResponseBody>> response = service.getAll();
		
		assertEquals(1, response.getBody().size());
	}
	
	@Test
	@Transactional
	void getOne() throws GenericException, BusinessException {
		
		ResponseEntity<BookingResponseBody> response = service.get(1L);
		
		assertEquals(1L, response.getBody().getId());
	}
	
	@Test
	@Transactional
	void update() throws GenericException, BusinessException {
		
		request.setUserId(0L);
		
		ResponseEntity<BookingResponseBody> response = service.update(1L, request);
		
		assertEquals(0L, response.getBody().getUser().getId());
	}

}
