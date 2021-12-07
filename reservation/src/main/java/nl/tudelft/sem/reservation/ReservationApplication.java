package nl.tudelft.sem.reservation;

import nl.tudelft.sem.reservation.communication.RoomCommunication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);

		System.out.println(RoomCommunication.getBuildingOpeningHours(23));
	}

}
