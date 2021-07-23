package com.alten.bookingapi.integration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.i18n.MessageBundle;
import com.alten.bookingapi.model.Booking;
import com.alten.bookingapi.model.BookingRepository;
import com.alten.bookingapi.model.BookingStatus;
import com.alten.bookingapi.model.Room;
import com.alten.bookingapi.model.User;
import com.alten.bookingapi.service.impl.BookingServiceImpl;
import com.alten.bookingapi.util.DateUtil;
import com.alten.bookingapi.validation.BookingValidations;

@WebMvcTest
@Import(value = {BookingServiceImpl.class, BookingValidations.class, MessageBundle.class})
public class BookingIntegrationTests {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;

	@MockBean
	protected BookingRepository repository;
	
	private BookingRequestBody request;
	
	protected Booking mockedActiveBooking;
	
	@BeforeAll
	void before() throws GenericException, BusinessException {
		
		LocalDate startDate = LocalDate.now().plusDays(minimumStartDate);
		
		LocalDate endDate = startDate.plusDays(maximumLengthOfStay - 1);
		
		String startDateString = startDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String endDateString = endDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		setRequest(new BookingRequestBody(1L, 1, startDateString, endDateString));
		
		mockedActiveBooking = new Booking(1L, new Room(1), new User(1L), startDate, endDate, BookingStatus.ACTIVE);
	}
	
	public BookingRequestBody getRequest() {
		return request;
	}

	public void setRequest(BookingRequestBody request) {
		this.request = request;
	}

}
