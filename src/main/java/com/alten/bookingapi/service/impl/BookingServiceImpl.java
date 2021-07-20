/**
 * 
 */
package com.alten.bookingapi.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.body.response.BookingResponseBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.model.Booking;
import com.alten.bookingapi.model.BookingRepository;
import com.alten.bookingapi.model.BookingStatus;
import com.alten.bookingapi.service.BookingService;

import lombok.extern.log4j.Log4j2;

/**
 * @author Danilo Cavalcanti
 *
 */
@Service
@Log4j2
public class BookingServiceImpl implements BookingService {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;
	
	@Autowired
	private BookingRepository repository;

	@Override
	public ResponseEntity<List<BookingResponseBody>> getAll() throws GenericException {
		
		log.info("Retrieving all bookings...");
		
		try {
			
			List<Booking> bookings = repository.findAll();
			
			return ResponseEntity.ok(BookingResponseBody.parse(bookings));
			
		} catch (Exception e) {
			
			log.error("An error has occurred when retrieving all bookings", e);
			
			throw new GenericException(e);
		}
	}

	@Override
	public ResponseEntity<BookingResponseBody> get(Long id) throws GenericException, BusinessException {
		
		log.info("Retrieving booking with id " + id);
		
		if (Objects.isNull(id)) throw new BusinessException("Id must not be null");
		
		try {
			
			Booking booking = repository.findById(id).orElse(null);
			
			return ResponseEntity.ok(BookingResponseBody.parse(booking));
			
		} catch (Exception e) {
			
			log.error("An error has occurred when retrieving booking with id " + id, e);
			
			throw new GenericException(e);
		}
	}

	@Override
	public ResponseEntity<?> cancel(Long id) throws GenericException, BusinessException {
		
		log.info("Canceling booking with id " + id);

		if (Objects.isNull(id)) throw new BusinessException("Id must not be null");
		
		try {
			
			Booking booking = repository.findById(id).orElseThrow(() -> new BusinessException("Booking with id " + id + " not found"));
			
			booking.setStatus(BookingStatus.CANCELED);
			
			repository.save(booking);
			
			return (ResponseEntity<?>) ResponseEntity.noContent();
			
		} catch (BusinessException e) {
			
			log.error("A business error has occurred when canceling booking with id " + id, e);
			
			throw e;
		
		} catch (Exception e) {
			
			log.error("An error has occurred when canceling booking with id " + id, e);
			
			throw new GenericException(e);
		}
	}

	@Override
	public ResponseEntity<BookingResponseBody> create(BookingRequestBody booking) throws GenericException, BusinessException {
		
		log.info("Creating a new booking...");

		try {

			BookingRequestBody.validate(booking);
			
			Booking entity = booking.toEntity();
			
			validateLengthOfStay(entity);
			
			validateStartDate(entity);
			
			entity.setStatus(BookingStatus.ACTIVE);
			
			Booking newBooking = repository.save(entity);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(BookingResponseBody.parse(newBooking));
			
		} catch (BusinessException e) {
			
			log.error("A business error has occurred when creating a new booking", e);
			
			throw e;
		
		} catch (Exception e) {
			
			log.error("An error has occurred when creating a new booking", e);
			
			throw new GenericException(e);
		}
	}
	
	@Override
	public ResponseEntity<BookingResponseBody> update(Long id, BookingRequestBody booking) throws GenericException, BusinessException {
		
		log.info("Updating booking with id " + id);

		if (Objects.isNull(id)) throw new BusinessException("Id must not be null");
		
		try {

			BookingRequestBody.validate(booking);
			
			Booking entity = booking.toEntity();
			
			validateLengthOfStay(entity);
			
			validateStartDate(entity);
			
			entity.setId(id);
			
			Booking newBooking = repository.save(entity);
			
			return ResponseEntity.ok(BookingResponseBody.parse(newBooking));
			
		} catch (BusinessException e) {
			
			log.error("A business error has occurred when updating booking with id " + id, e);
			
			throw e;
		
		} catch (Exception e) {
			
			log.error("An error has occurred when updating booking with id " + id, e);
			
			throw new GenericException(e);
		}
	}
	
	/**
	 * Check if the length of stay of the reservation is valid.
	 * 
	 */
	private void validateLengthOfStay(Booking booking) throws BusinessException {
		
		long lengthOfStay = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
		
		if (lengthOfStay > maximumLengthOfStay) throw new BusinessException(Set.of("The maximum length of stay limit of " + maximumLengthOfStay + " day(s) has been surpassed"));
	}
	
	/**
	 * Check if the start date is valid.
	 * 
	 */
	private void validateStartDate(Booking booking) throws BusinessException {
		
		long daysBetweenStart = ChronoUnit.DAYS.between(LocalDate.now(), booking.getStartDate());
		
		if (daysBetweenStart > maximumStartDate) throw new BusinessException(Set.of("The start date can't surpass " + maximumStartDate + " day(s) from the current date"));
		
		if (daysBetweenStart < minimumStartDate) throw new BusinessException(Set.of("The start date must be at least " + minimumStartDate + " day(s) after the current date"));
	}
}
