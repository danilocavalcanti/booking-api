package com.alten.bookingapi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.body.response.BookingResponseBody;
import com.alten.bookingapi.body.response.SuccessResponseBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.model.BookingRepository;
import com.alten.bookingapi.service.BookingService;
import com.alten.bookingapi.util.DateUtil;

import lombok.Getter;
import lombok.Setter;

@SpringBootTest
@Getter
@Setter
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class BookingUnitTests {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;

	@Autowired
	protected BookingRepository repository;
	
	@Autowired
	private BookingService service;
	
	private BookingRequestBody request;
	
	private Long createdId;
	
	@BeforeAll
	void before() throws GenericException, BusinessException {
		
		repository.deleteAll();
		
		LocalDate startDate = LocalDate.now().plusDays(minimumStartDate);
		
		LocalDate endDate = startDate.plusDays(maximumLengthOfStay - 1);
		
		String startDateString = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String endDateString = endDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		setRequest(new BookingRequestBody(1L, 1, startDateString, endDateString));
	}
	
	protected void createBooking() throws GenericException, BusinessException {
		
		ResponseEntity<SuccessResponseBody<BookingResponseBody>> b = service.create(getRequest());
		
		setCreatedId(b.getBody().getResult().getId());
	}

}
