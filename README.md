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