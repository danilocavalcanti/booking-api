/**
 * 
 */
package com.alten.bookingapi.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.body.response.BookingResponseBody;
import com.alten.bookingapi.body.response.SuccessResponseBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;
import com.alten.bookingapi.i18n.MessageBundle;
import com.alten.bookingapi.model.Booking;
import com.alten.bookingapi.model.BookingRepository;
import com.alten.bookingapi.model.BookingStatus;
import com.alten.bookingapi.model.RoomRepository;
import com.alten.bookingapi.model.UserRepository;
import com.alten.bookingapi.service.BookingService;
import com.alten.bookingapi.util.DateUtil;
import com.alten.bookingapi.validation.BookingValidations;

import lombok.extern.log4j.Log4j2;

/**
 * @author Danilo Cavalcanti
 *
 */
@Service
@Log4j2
public class BookingServiceImpl implements BookingService {
	
	@Autowired
	private BookingRepository repository;
	
	@Autowired
	private BookingValidations validations;
	
	@Autowired
	private MessageBundle messages;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoomRepository roomRepository;

	@Override
	public ResponseEntity<SuccessResponseBody<List<BookingResponseBody>>> getAll() throws GenericException {
		
		log.info("Retrieving all bookings...");
		
		try {
			
			List<Booking> bookings = repository.findAll();
			
			return ResponseEntity.ok(new SuccessResponseBody<List<BookingResponseBody>>().create(BookingResponseBody.parse(bookings)));
			
		} catch (Exception e) {
			
			log.error("An error has occurred when retrieving all bookings", e);
			
			throw new GenericException(e);
		}
	}

	@Override
	public ResponseEntity<SuccessResponseBody<BookingResponseBody>> get(Long id) throws GenericException, BusinessException {
		
		log.info("Retrieving booking with id " + id);
		
		try {
		
			if (Objects.isNull(id)) throw new BusinessException(messages.get("0005"));
			
			Booking booking = repository.findById(id).orElse(null);
			
			return ResponseEntity.ok(new SuccessResponseBody<BookingResponseBody>().create(BookingResponseBody.parse(booking)));
			
		} catch (BusinessException e) {
			
			log.warn("A business error has occurred when getting booking with id " + id, e);
			
			throw e;
		
		} catch (Exception e) {
			
			log.error("An error has occurred when retrieving booking with id " + id, e);
			
			throw new GenericException(e);
		}
	}

	@Override
	public ResponseEntity<?> cancel(Long id) throws GenericException, BusinessException {
		
		log.info("Canceling booking with id " + id);

		try {
		
			if (Objects.isNull(id)) throw new BusinessException(messages.get("0005"));
			
			Booking booking = repository.findById(id).orElseThrow(() -> new BusinessException(String.format(messages.get("0006"), id)));
			
			booking.setStatus(BookingStatus.CANCELED);
			
			repository.save(booking);
			
			return ResponseEntity.noContent().build();
			
		} catch (BusinessException e) {
			
			log.warn("A business error has occurred when canceling booking with id " + id, e);
			
			throw e;
		
		} catch (Exception e) {
			
			log.error("An error has occurred when canceling booking with id " + id, e);
			
			throw new GenericException(e);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
	public ResponseEntity<SuccessResponseBody<BookingResponseBody>> create(BookingRequestBody booking) throws GenericException, BusinessException {
		
		log.info("Creating a new booking...");

		try {

			validations.validateRequest(booking);
			
			Booking entity = booking.toEntity();
			
			validations.validateBooking(entity);
			
			entity.setStatus(BookingStatus.ACTIVE);
			
			Booking newBooking = repository.save(entity);
			
			newBooking.setRoom(roomRepository.findById(newBooking.getRoom().getId()).orElse(null));
			
			newBooking.setUser(userRepository.findById(newBooking.getUser().getId()).orElse(null));
			
			return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponseBody<BookingResponseBody>().create(BookingResponseBody.parse(newBooking)));
			
		} catch (BusinessException e) {
			
			log.warn("A business error has occurred when creating a new booking", e);
			
			throw e;
		
		} catch (Exception e) {
			
			log.error("An error has occurred when creating a new booking", e);
			
			throw new GenericException(e);
		}
	}
	
	@Override
	public ResponseEntity<SuccessResponseBody<BookingResponseBody>> update(Long id, BookingRequestBody booking) throws GenericException, BusinessException {
		
		log.info("Updating booking with id " + id);

		try {
		
			if (Objects.isNull(id)) throw new BusinessException(messages.get("0005"));

			validations.validateRequest(booking);
			
			Booking entity = booking.toEntity();
			
			entity.setId(id);

			validations.validateBooking(entity);
			
			Booking newBooking = repository.save(entity);
			
			newBooking.setStatus(BookingStatus.ACTIVE);
			
			return ResponseEntity.ok(new SuccessResponseBody<BookingResponseBody>().create(BookingResponseBody.parse(newBooking)));
			
		} catch (BusinessException e) {
			
			log.warn("A business error has occurred when updating booking with id " + id, e);
			
			throw e;
		
		} catch (Exception e) {
			
			log.error("An error has occurred when updating booking with id " + id, e);
			
			throw new GenericException(e);
		}
	}

	@Override
	public ResponseEntity<?> checkDatesAvailability(String startDateStr, String endDateStr, Integer roomId) throws GenericException, BusinessException {
		
		log.info("Checking dates availability - startDate: " + startDateStr + " - endDate: " + endDateStr + " - roomId: " + roomId);
		
		try {
			
			if (Objects.isNull(roomId)) throw new BusinessException(messages.get("0010"));

			validations.validateDates(startDateStr, endDateStr);
			
			LocalDate startDate = DateUtil.toLocalDate(startDateStr);
			
			LocalDate endDate = DateUtil.toLocalDate(endDateStr);
			
			Optional<List<Booking>> result = repository.findByPeriod(roomId, startDate, endDate);
			
			HttpStatus statusCode = (result.isPresent() && result.get().size() > 0) ? HttpStatus.CONFLICT : HttpStatus.OK;
			
			return ResponseEntity.status(statusCode).build();
			
		} catch (BusinessException e) {
			
			log.warn("A business error has occurred when checking dates availability", e);
			
			throw e;
			
		} catch (Exception e) {
			
			log.error("An error has occurred when checking dates availability", e);
			
			throw new GenericException(e);
		}
	}
}
