/**
 * 
 */
package com.alten.bookingapi.body.response;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alten.bookingapi.model.Booking;
import com.alten.bookingapi.model.BookingStatus;
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
	
	private BookingStatus status;
	
	public static List<BookingResponseBody> parse(List<Booking> entities) {
		
		if (Objects.isNull(entities) || entities.isEmpty()) return null;
		
		List<BookingResponseBody> bookings = new ArrayList<BookingResponseBody>();
		
		entities.forEach(entity -> bookings.add(parse(entity)));
		
		return bookings;
	}
	
	public static BookingResponseBody parse(Booking entity) {
		
		if (Objects.isNull(entity)) return null;
		
		BookingResponseBody booking = new BookingResponseBody();
		
		booking.setUser(UserBodyResponse.parse(entity.getUser()));
		
		booking.setRoom(RoomBodyResponse.parse(entity.getRoom()));
		
		booking.setId(entity.getId());
		
		booking.setStartDate(DateUtil.toStringDate(entity.getStartDate()));
		
		booking.setEndDate(DateUtil.toStringDate(entity.getEndDate()));
		
		booking.setStatus(entity.getStatus());
		
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
