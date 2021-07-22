/**
 * 
 */
package com.alten.bookingapi.body.request;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.alten.bookingapi.exception.BusinessException;
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

	public static void validate(BookingRequestBody booking) throws BusinessException {

		Set<String> messages = new HashSet<>();

		if (Objects.isNull(booking)) {

			messages.add("No information was provided");

			throw new BusinessException(messages);
		}

		if (Objects.isNull(booking.getUserId()))
			messages.add("User required");

		if (Objects.isNull(booking.getRoomId()))
			messages.add("Room required");

		if (Objects.isNull(booking.getStartDate()))
			messages.add("Start date required");

		if (!DateUtil.isValid(booking.getStartDate()))
			messages.add("Start date must be valid");

		if (Objects.isNull(booking.getEndDate()))
			messages.add("End date required");

		if (!DateUtil.isValid(booking.getEndDate()))
			messages.add("End date must be valid");

		if (messages.size() > 0)
			throw new BusinessException(messages);
	}
}
