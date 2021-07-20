/**
 * 
 */
package com.alten.bookingapi.body.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alten.bookingapi.model.Booking;
import com.alten.bookingapi.model.Room;
import com.alten.bookingapi.model.User;
import com.alten.bookingapi.util.DateUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Danilo Cavalcanti
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseBody {
	
	private Long id;

	private UserBodyResponse user;
	
	private RoomBodyResponse room;
	
	private String startDate;
	
	private String endDate;
	
	public static List<BookingResponseBody> parse(List<Booking> bookingEntities) {
		
		if (Objects.isNull(bookingEntities) || bookingEntities.isEmpty()) return null;
		
		List<BookingResponseBody> bookings = new ArrayList<BookingResponseBody>();
		
		bookingEntities.forEach(entity -> bookings.add(parse(entity)));
		
		return bookings;
	}
	
	public static BookingResponseBody parse(Booking bookingEntity) {
		
		if (Objects.isNull(bookingEntity)) return null;
		
		BookingResponseBody booking = new BookingResponseBody();
		
		booking.setUser(UserBodyResponse.parse(bookingEntity.getUser()));
		
		booking.setRoom(RoomBodyResponse.parse(bookingEntity.getRoom()));
		
		booking.setId(bookingEntity.getId());
		
		booking.setStartDate(DateUtil.toStringDate(bookingEntity.getStartDate()));
		
		booking.setEndDate(DateUtil.toStringDate(bookingEntity.getEndDate()));
		
		return booking;
	}
	
	@Data
	public static class RoomBodyResponse {
		
		private Integer id;
		
		private Integer number;

		private String name;
		
		private BigDecimal price;
		
		private static RoomBodyResponse parse(Room roomEntity) {
			
			if (Objects.isNull(roomEntity)) return null;
			
			RoomBodyResponse room = new RoomBodyResponse();
			
			room.setId(roomEntity.getId());
			
			room.setName(roomEntity.getName());
			
			room.setNumber(roomEntity.getNumber());
			
			room.setPrice(roomEntity.getPrice());
			
			return room;
		}
	}
	
	@Data
	public static class UserBodyResponse {
		
		private Long id;
		
		private String name;
		
		private static UserBodyResponse parse(User userEntity) {
			
			if (Objects.isNull(userEntity)) return null;
			
			UserBodyResponse user = new UserBodyResponse();
			
			user.setId(userEntity.getId());
			
			user.setName(userEntity.getName());
			
			return user;
		}
	}
}
