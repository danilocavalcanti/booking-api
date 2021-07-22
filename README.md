# Booking API

This API was designed and developed to provide services for booking a room in Cancun Hotel.

# Instructions

To run the application, use the following command lines:

- Java: java 
- Maven: 

# Notes

- The database model is the simplest possible to attend the project requirements;
- The association between Room and Booking is one-to-many because it is being considered that the hotel has only 1 room, otherwise it should be many-to-many;
- Security principles are being disregarded;
- Bidirectional one-to-many is being used because it's more efficient and for that, ideally Room class should have methods like addBooking and removeBooking to sync both sides;
- Booking status: ACTIVE, CANCELED (user requested the cancellation)
- Methods to check the dates availability are with ISOLATION = SERIALIZABLE. This ensures that the validation will be thread-safety in case of concurrency in bookings. The strategy can be applied in DB layer too.

# Requirements
Post-Covid scenario:
People are now free to travel everywhere but because of the pandemic, a lot of hotels went
bankrupt. Some former famous travel places are left with only one hotel.
You’ve been given the responsibility to develop a booking API for the very last hotel in Cancun.
The requirements are:
- API will be maintained by the hotel’s IT department.
- As it’s the very last hotel, the quality of service must be 99.99 to 100% => no downtime
- For the purpose of the test, we assume the hotel has only one room available
- To give a chance to everyone to book the room, the stay can’t be longer than 3 days and
can’t be reserved more than 30 days in advance.
- All reservations start at least the next day of booking,
- To simplify the use case, a “DAY’ in the hotel room starts from 00:00 to 23:59:59.
- Every end-user can check the room availability, place a reservation, cancel it or modify it.
- To simplify the API is insecure.