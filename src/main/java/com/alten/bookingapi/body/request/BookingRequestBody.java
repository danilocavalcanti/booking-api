/**
 * 
 */
package com.alten.bookingapi.body.request;

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
public class BookingRequestBody {

	private Long userId;

	private Integer roomId;

	private String startDate;

	private String endDate;

	public Booking toEntity() {

		User user = new User(this.getUserId());

		Room room = new Room(this.getRoomId());

		return new Booking(null, room, user, DateUtil.toLocalDate(this.getStartDate()),
				DateUtil.toLocalDate(this.getEndDate()), null);
	}
	
}
