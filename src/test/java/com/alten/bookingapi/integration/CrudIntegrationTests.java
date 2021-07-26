/**
 * 
 */
package com.alten.bookingapi.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.alten.bookingapi.model.Booking;
import com.alten.bookingapi.model.BookingStatus;
import com.alten.bookingapi.model.Room;
import com.alten.bookingapi.model.User;
import com.alten.bookingapi.util.DateUtil;

/**
 * @author Danilo Cavalcanti
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class CrudIntegrationTests extends BookingIntegrationTests {
	
	@Value("${booking.maximum-length-of-stay}")
	private long maximumLengthOfStay;
	
	@Value("${booking.maximum-start-date-days-range}")
	private long maximumStartDate;
	
	@Value("${booking.minimum-start-date-days-range}")
	private long minimumStartDate;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@Order(1)
	void create() throws Exception {
		
		Mockito.when(repository.save(Mockito.any(Booking.class))).thenReturn(mockedActiveBooking);

		mockMvc.perform(post("/bookings")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"roomId\": " + getRequest().getRoomId() 
						+ ", \"userId\": " + getRequest().getUserId()
						+ ", \"startDate\": \"" + getRequest().getStartDate() + "\""
						+ ", \"endDate\": \"" + getRequest().getEndDate() + "\"}"))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.result.id", is(1)))
				.andExpect(jsonPath("$.result.status", is(BookingStatus.ACTIVE.toString())));
	}
	
	@Test
	@Order(2)
	void getAll() throws Exception {
		
		List<Booking> expected = new ArrayList<Booking>();
		
		expected.add(mockedActiveBooking);
		
		Mockito.when(repository.findAll()).thenReturn(expected);
		
		mockMvc.perform(get("/bookings")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", hasSize(1)))
				.andExpect(jsonPath("$.result[0].id", is(1)));
	}
	
	@Test
	@Order(3)
	void getOne() throws Exception {
		
		Mockito.when(repository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(mockedActiveBooking));
		
		mockMvc.perform(get("/bookings/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.id", is(1)));
		
	}
	
	@Test
	@Order(4)
	void update() throws Exception {
		
		LocalDate newStartDate = LocalDate.now().plusDays(minimumStartDate + maximumLengthOfStay);
		
		LocalDate newEndDate = newStartDate.plusDays(maximumLengthOfStay - 1);
		
		String newStartDateString = newStartDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String newEndDateString = newEndDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		Mockito.when(repository.save(Mockito.any(Booking.class)))
			.thenReturn(new Booking(1L, new Room(1), new User(1L), newStartDate, newEndDate, BookingStatus.ACTIVE));
		
		Mockito.when(repository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(new Booking(1L, new Room(1), new User(1L), newStartDate, newEndDate, BookingStatus.ACTIVE)));
		
		mockMvc.perform(patch("/bookings/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{"
						+ "\"roomId\": " + getRequest().getRoomId() 
						+ ", \"userId\": " + getRequest().getUserId()
						+ ", \"startDate\": \"" + newStartDateString + "\""
						+ ", \"endDate\": \"" + newEndDateString + "\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.id", is(1)))
				.andExpect(jsonPath("$.result.startDate", is(newStartDateString)))
				.andExpect(jsonPath("$.result.endDate", is(newEndDateString)));
		
	}
	
	@Test
	@Order(5)
	void cancel() throws Exception {
		
		Mockito.when(repository.save(Mockito.any(Booking.class)))
		.thenReturn(new Booking(1L, new Room(1), new User(1L), null, null, BookingStatus.ACTIVE));
		
		Mockito.when(repository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(new Booking(1L, new Room(1), new User(1L), null, null, BookingStatus.CANCELED)));
		
		mockMvc.perform(delete("/bookings/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		mockMvc.perform(get("/bookings/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result.id", is(1)))
				.andExpect(jsonPath("$.result.status", is(BookingStatus.CANCELED.toString())));
	}
	
	@Test
	@Order(6)
	void checkAvailability() throws Exception {
		
		LocalDate newStartDate = LocalDate.now().plusDays(minimumStartDate + 2 * maximumLengthOfStay);
		
		LocalDate newEndDate = newStartDate.plusDays(maximumLengthOfStay - 1);
		
		String newStartDateString = newStartDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String newEndDateString = newEndDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		Mockito.when(repository.findByPeriod(Mockito.any(Integer.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
		.thenReturn(Optional.ofNullable(null));
		
		mockMvc.perform(get("/bookings/availabilities?startDate=" + newStartDateString + "&endDate=" + newEndDateString + "&roomId=1"))
				.andExpect(status().isOk());
	}
	
	@Test
	@Order(6)
	void checkNoAvailability() throws Exception {
		
		LocalDate newStartDate = LocalDate.now().plusDays(minimumStartDate + 2 * maximumLengthOfStay);
		
		LocalDate newEndDate = newStartDate.plusDays(maximumLengthOfStay - 1);
		
		String newStartDateString = newStartDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		String newEndDateString = newEndDate.format(DateTimeFormatter.ofPattern(DateUtil.DATE_PATTERN));
		
		List<Booking> expected = new ArrayList<Booking>();
		
		expected.add(new Booking());
		
		Mockito.when(repository.findByPeriod(Mockito.any(Integer.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
		.thenReturn(Optional.of(expected));
		
		mockMvc.perform(get("/bookings/availabilities?startDate=" + newStartDateString + "&endDate=" + newEndDateString + "&roomId=1"))
				.andExpect(status().isConflict());
	}

}
