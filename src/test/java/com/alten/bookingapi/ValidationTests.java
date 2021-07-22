/**
 * 
 */
package com.alten.bookingapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.service.BookingService;
import com.alten.bookingapi.util.DateUtil;

/**
 * @author Danilo Cavalcanti
 *
 */
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ValidationTests extends BookingApiApplicationTests {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;
	
	@Autowired
	private BookingService service;
	
	@BeforeAll
	void before() throws GenericException, BusinessException {
		
		LocalDate startDate = LocalDate.now().plusDays(minimumStartDate);
		
		LocalDate endDate = startDate.plusDays(maximumLengthOfStay);
		
		String startDateString = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String endDateString = endDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		service.create(new BookingRequestBody(1L, 1, startDateString, endDateString));
	}
	
	@Test
	@Order(1)
	void lengthOfStayTooLong() {
		
		LocalDate startDate = LocalDate.now();
		
		LocalDate endDate = startDate.plusDays(maximumLengthOfStay + 1);
		
		String startDateString = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String endDateString = endDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		BookingRequestBody booking = new BookingRequestBody(1L, 1, startDateString, endDateString);
		
		BusinessException exception = assertThrows(BusinessException.class, () -> service.create(booking));
		
		assertEquals(exception.getLocalizedMessage(), "The maximum length of stay limit of " + maximumLengthOfStay + " day(s) has been surpassed");
	}
	
	@Test
	@Order(2)
	void startDateTooEarly() {
		
		LocalDate startDate = LocalDate.now().plusDays(maximumStartDate + 1);
		
		LocalDate endDate = startDate.plusDays(1);
		
		String endDateString = endDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String startDateString = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		BookingRequestBody booking = new BookingRequestBody(1L, 1, startDateString, endDateString);
		
		BusinessException exception = assertThrows(BusinessException.class, () -> service.create(booking));
		
		assertEquals(exception.getLocalizedMessage(), "The start date can't surpass " + maximumStartDate + " day(s) from the current date");
	}
	
	@Test
	@Order(3)
	void startDateTooSoon() {
		
		LocalDate startDate = LocalDate.now();
		
		LocalDate endDate = startDate.plusDays(1);
		
		String endDateString = DateUtil.toStringDate(endDate);
		
		String startDateString = DateUtil.toStringDate(startDate);
		
		BookingRequestBody booking = new BookingRequestBody(1L, 1, startDateString, endDateString);
		
		BusinessException exception = assertThrows(BusinessException.class, () -> service.create(booking));
		
		assertEquals(exception.getLocalizedMessage(), "The start date must be at least " + minimumStartDate + " day(s) after the current date");
	}
	
	@Test
	@Order(4)
	void roomAlreadyBookedForTheGivenPeriod() {
		
	}

}
