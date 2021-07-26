# Author

	Danilo Tourinho Cavalcanti

# Booking API

This API was designed and developed to provide services for booking a room in Cancun Hotel.

**Stack:**
	
	- Java 8 (JDK)
	- Maven
	- Spring Boot 2.5.2
	- H2 embedded database

# Instructions

Root folder contains:

	 Swagger
	 Postman collections
	 Dockerfile
	 Source code

To start the application, first build with maven and then choose between java, maven or docker to run.

**Inside root folder:**

1 - Build the aplication using Maven:

	mvn clean install

2 - Run the application:

	Run with Docker: docker-compose up --build 
	Run with Java: java -jar target/api.war
	Run with Maven: mvn spring-boot:run

3 - Host and Base path:

	Running with docker: http://localhost:8080/api
	
	Running with Java or Maven: http://localhost:8080

# Internacionalization (i18n)
You can choose between English and French for business messages. By default, English is used.

	Usage:
	
	Query param "lang" = "en" or "fr". 
	
	Example: 
		French: http://localhost:8080/api/bookings?lang=fr
		English: http://localhost:8080/api/bookings OR http://localhost:8080/api/bookings?lang=en

# Notes

- The database model is the simplest possible to attend the project requirements;
- The association between Room and Booking is one-to-many because it is being considered that the hotel has only 1 room, otherwise it should be many-to-many;
- Security principles are being disregarded;
- Bidirectional one-to-many is being used because it's more efficient and for that, ideally Room class should have methods like addBooking and removeBooking to sync both sides;
- Booking status: ACTIVE, CANCELED (user requested the cancellation)
- Methods to check the dates availability are with ISOLATION = SERIALIZABLE. This ensures that the validation will be thread-safety in case of concurrency in bookings. The strategy can be applied in DB layer too.

# Requirements/Business Rules
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
