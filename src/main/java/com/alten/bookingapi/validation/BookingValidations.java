/**
 * 
 */
package com.alten.bookingapi.validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.i18n.MessageBundle;
import com.alten.bookingapi.model.Booking;
import com.alten.bookingapi.model.BookingRepository;
import com.alten.bookingapi.util.DateUtil;

import lombok.extern.log4j.Log4j2;

/**
 * @author Danilo Cavalcanti
 *
 */
@Component
@Log4j2
public class BookingValidations {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;
	
	@Autowired
	private BookingRepository repository;
	
	@Autowired
	private MessageBundle messages;
	
	/**
	 * Perform validations for a bookings creations
	 * 
	 * @param bookin
	 * @throws BusinessException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void validateBooking(Booking booking) throws BusinessException {
		
		log.info("Validating booking creation...");
		
		validateLengthOfStay(booking.getStartDate(), booking.getEndDate());
		
		validateStartDate(booking.getStartDate());
		
		validateDatesAvailability(booking.getRoom().getId(), booking.getStartDate(), booking.getEndDate());
	}
	
	/**
	 * Validate request
	 * 
	 * @param booking
	 * @throws BusinessException 
	 */
	public void validateRequest(BookingRequestBody booking) throws BusinessException {
		
			Set<String> bodyMessages = new HashSet<>();

			if (Objects.isNull(booking)) {

				bodyMessages.add(messages.get("0008"));

				throw new BusinessException(bodyMessages);
			}

			if (Objects.isNull(booking.getUserId()))
				bodyMessages.add(messages.get("0009"));

			if (Objects.isNull(booking.getRoomId()))
				bodyMessages.add(messages.get("0010"));

			if (Objects.isNull(booking.getStartDate()))
				bodyMessages.add(messages.get("0011"));

			if (!DateUtil.isValid(booking.getStartDate()))
				bodyMessages.add(messages.get("0012"));

			if (Objects.isNull(booking.getEndDate()))
				bodyMessages.add(messages.get("0013"));

			if (!DateUtil.isValid(booking.getEndDate()))
				bodyMessages.add(messages.get("0014"));
			
			LocalDate startDate = DateUtil.toLocalDate(booking.getStartDate());
			
			LocalDate endDate = DateUtil.toLocalDate(booking.getEndDate());
			
			if (startDate.isEqual(endDate)) throw new BusinessException(messages.get("0007"));
			
			if (endDate.isBefore(startDate)) throw new BusinessException(messages.get("0015"));
			
			if (bodyMessages.size() > 0)
				throw new BusinessException(bodyMessages);
	}
	
	/**
	 * Validate the dates
	 * 
	 * @param startDate
	 * @throws BusinessException 
	 */
	public void validateDates(String startDateStr, String endDateStr) throws BusinessException {
		
		Set<String> bodyMessages = new HashSet<>();
		
		if (!DateUtil.isValid(startDateStr)) throw new BusinessException(messages.get("0007"));
		
		if (!DateUtil.isValid(startDateStr)) throw new BusinessException(messages.get("0007"));
		
		if (Objects.isNull(startDateStr))
			bodyMessages.add(messages.get("0011"));

		if (!DateUtil.isValid(startDateStr))
			bodyMessages.add(messages.get("0012"));

		if (Objects.isNull(endDateStr))
			bodyMessages.add(messages.get("0013"));

		if (!DateUtil.isValid(endDateStr))
			bodyMessages.add(messages.get("0014"));
		
		LocalDate startDate = DateUtil.toLocalDate(startDateStr);
		
		LocalDate endDate = DateUtil.toLocalDate(endDateStr);
		
		if (startDate.isEqual(endDate)) throw new BusinessException(messages.get("0007"));
		
		if (endDate.isBefore(startDate)) throw new BusinessException(messages.get("0015"));
		
		validateLengthOfStay(startDate, endDate);
		
		validateStartDate(startDate);
	}
	
	/**
	 * Validate if the length of stay of the reservation is valid.
	 * maximumLengthOfStay - 1 considering the first day.
	 * 
	 * @param booking
	 * @throws BusinessException
	 */
	public void validateLengthOfStay(LocalDate startDate, LocalDate endDate) throws BusinessException {
		
		log.info("Validating booking length of stay...");
		
		long lengthOfStay = ChronoUnit.DAYS.between(startDate, endDate);
		
		if (lengthOfStay > maximumLengthOfStay - 1) throw new BusinessException(String.format(messages.get("0001"), maximumLengthOfStay));
	}
	
	/**
	 * Validate if the start date is valid.
	 * Days between - 1 considering the first day.
	 * @throws BusinessException
	 */
	private void validateStartDate(LocalDate startDate) throws BusinessException {
		
		log.info("Validating booking start date...");
		
		long daysBetweenStart = ChronoUnit.DAYS.between(LocalDate.now(), startDate);
		
		if (daysBetweenStart > maximumStartDate) throw new BusinessException(String.format(messages.get("0002"), maximumStartDate));
		
		if (daysBetweenStart < minimumStartDate) throw new BusinessException(String.format(messages.get("0003"), minimumStartDate));
	}
	
	/**
	 * Validate if the room is available during the given period when creating or when updating an existing booking.
	 * 
	 * @param booking
	 * @throws BusinessException
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	private void validateDatesAvailability(Integer roomId, LocalDate startDate, LocalDate endDate) throws BusinessException {
		
		log.info("Validating booking dates availability...");
		
			Optional<List<Booking>> result = repository.findByPeriod(roomId, startDate, endDate);
			
			if (result.isPresent() && result.get().size() > 0)
				throw new BusinessException(messages.get("0004"), HttpStatus.CONFLICT);
	}

}
