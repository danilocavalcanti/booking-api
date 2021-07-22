/**
 * 
 */
package com.alten.bookingapi.validation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.model.Booking;
import com.alten.bookingapi.model.BookingRepository;

/**
 * @author Danilo Cavalcanti
 *
 */
@Component
public class BookingValidations {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;
	
	@Autowired
	private BookingRepository repository;
	
	/**
	 * Perform validations for a bookings creations
	 * 
	 * @param bookin
	 * @throws BusinessException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void validateCreation(Booking booking) throws BusinessException {
		
		validateLengthOfStay(booking);
		
		validateStartDate(booking);
		
		validateDatesAvailability(booking, false);
	}
	
	/**
	 * Perform validations for a bookings updates
	 * 
	 * @param booking
	 * @throws BusinessException 
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void validateUpdate(Booking booking) throws BusinessException {
		
		validateLengthOfStay(booking);
		
		validateStartDate(booking);
		
		validateDatesAvailability(booking, true);
	}
	
	/**
	 * Validate if the length of stay of the reservation is valid.
	 * maximumLengthOfStay - 1 considering the first day.
	 * 
	 * @param booking
	 * @throws BusinessException
	 */
	public void validateLengthOfStay(Booking booking) throws BusinessException {
		
		long lengthOfStay = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
		
		if (lengthOfStay > maximumLengthOfStay - 1) throw new BusinessException(Set.of("The maximum length of stay limit of " + maximumLengthOfStay + " day(s) has been surpassed"));
	}
	
	/**
	 * Validate if the start date is valid.
	 * Days between - 1 considering the first day.
	 * @param booking
	 * @throws BusinessException
	 */
	private void validateStartDate(Booking booking) throws BusinessException {
		
		long daysBetweenStart = ChronoUnit.DAYS.between(LocalDate.now(), booking.getStartDate());
		
		if (daysBetweenStart > maximumStartDate) throw new BusinessException(Set.of("The start date can't surpass " + maximumStartDate + " day(s) from the current date"));
		
		if (daysBetweenStart < minimumStartDate) throw new BusinessException(Set.of("The start date must be at least " + minimumStartDate + " day(s) after the current date"));
	}
	
	/**
	 * Validate if the room is available during the given period when creating or when updating an existing booking.
	 * 
	 * @param booking
	 * @throws BusinessException
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	private void validateDatesAvailability(Booking booking, boolean isUpdate) throws BusinessException {
		
		if (isUpdate) {
			
			if (repository.findByPeriod(booking.getId(), booking.getRoom().getId(), booking.getStartDate(), booking.getEndDate()).isPresent())
				throw new BusinessException("The room is unvailable for the given period");
			
		} else {
			
			if (repository.findByPeriod(booking.getRoom().getId(), booking.getStartDate(), booking.getEndDate()).isPresent())
				throw new BusinessException("The room is unvailable for the given period");
		}
	}

}
