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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.body.response.BookingResponseBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.model.BookingRepository;
import com.alten.bookingapi.model.BookingStatus;
import com.alten.bookingapi.service.BookingService;
import com.alten.bookingapi.util.DateUtil;

/**
 * @author Danilo Cavalcanti
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class BookingTests extends BookingApiApplicationTests {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;
	
	@Autowired
	private BookingService service;
	
	@Autowired
	private BookingRepository repository;
	
	private static BookingRequestBody request;
	
	private ResponseEntity<BookingResponseBody> booking;
	
	private Long createdId;
	
	@BeforeAll
	void before() {
		
		LocalDate startDate = LocalDate.now().plusDays(minimumStartDate);
		
		LocalDate endDate = startDate.plusDays(maximumLengthOfStay - 1);
		
		String startDateString = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String endDateString = endDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		request = new BookingRequestBody(1L, 1, startDateString, endDateString);
	}
	
	@AfterAll
	void after() {
		
		repository.deleteById(1L);
	}
	
	@Test
	@Order(1)
	void create() {
		
		assertDoesNotThrow(() -> {
			
			booking = service.create(request);
		});
		
		assertNotNull(booking);
		
		assertNotNull(booking.getBody());
		
		assertEquals(booking.getBody().getStatus(), BookingStatus.ACTIVE);
		
		assertEquals(booking.getStatusCode(), HttpStatus.CREATED);
	}
	
	@Test
	@Transactional
	@Order(2)
	void getAll() throws GenericException {
		
		ResponseEntity<List<BookingResponseBody>> response = service.getAll();
		
		assertNotNull(response);
		
		assertNotNull(response.getBody());
		
		assertEquals(1, response.getBody().size());
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		
		createdId = response.getBody().stream().findAny().get().getId();
	}
	
	@Test
	@Transactional
	@Order(3)
	void getOne() throws GenericException, BusinessException {
		
		ResponseEntity<BookingResponseBody> response = service.get(createdId);
		
		assertNotNull(response);
		
		assertNotNull(response.getBody());
		
		assertEquals(createdId, response.getBody().getId());
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@Transactional
	@Order(4)
	void update() throws GenericException, BusinessException {
		
		request.setUserId(0L);
		
		ResponseEntity<BookingResponseBody> response = service.update(createdId, request);
		
		assertNotNull(response);
		
		assertNotNull(response.getBody());
		
		assertEquals(0L, response.getBody().getUser().getId());
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	@Transactional
	@Order(5)
	void cancel() throws GenericException, BusinessException {
		
		ResponseEntity<?> response = service.cancel(createdId);
		
		ResponseEntity<BookingResponseBody> booking = service.get(createdId);
		
		assertNotNull(booking);
		
		assertNotNull(booking.getBody());
		
		assertEquals(booking.getBody().getStatus(), BookingStatus.CANCELED);
		
		assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT);
	}

}
