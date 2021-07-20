/**
 * 
 */
package com.alten.bookingapi.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.alten.bookingapi.body.request.BookingRequestBody;
import com.alten.bookingapi.body.response.BookingResponseBody;
import com.alten.bookingapi.exception.BusinessException;
import com.alten.bookingapi.exception.GenericException;

/**
 * @author Danilo Cavalcanti
 *
 */
public interface BookingService {
	
	/**
	 * Retrieve all the bookings.
	 * 
	 * @return
	 * @throws GenericException
	 */
	public ResponseEntity<List<BookingResponseBody>> getAll() throws GenericException;
	
	/**
	 * Retrieve one specific booking.
	 * 
	 * @param id
	 * @return
	 * @throws BusinessException
	 * @throws GenericException
	 */
	public ResponseEntity<BookingResponseBody> get(Long id) throws GenericException, BusinessException; 
	
	/**
	 * Delete one specific booking.
	 * 
	 * @param id
	 * @return
	 * @throws GenericException
	 * @throws BusinessException 
	 */
	public ResponseEntity<?> cancel(Long id) throws GenericException, BusinessException; 
	
	/**
	 * Create a new booking.
	 * 
	 * @return
	 * @throws GenericException
	 * @throws BusinessException 
	 */
	public ResponseEntity<BookingResponseBody> create(BookingRequestBody booking) throws GenericException, BusinessException; 
	
	/**
	 * Reschedule an existing booking.
	 * 
	 * @return
	 * @throws GenericException
	 * @throws BusinessException 
	 */
	public ResponseEntity<BookingResponseBody> update(Long id, BookingRequestBody booking) throws GenericException, BusinessException; 

}
