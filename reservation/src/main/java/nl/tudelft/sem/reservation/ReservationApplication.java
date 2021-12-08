package nl.tudelft.sem.reservation;

import nl.tudelft.sem.reservation.communication.RoomCommunication;
import nl.tudelft.sem.reservation.entity.Reservation;
import nl.tudelft.sem.reservation.exception.InvalidReservationException;
import nl.tudelft.sem.reservation.validators.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);

		Reservation reservation = new Reservation(1, 5, 23,
				LocalDateTime.of(2021, 12, 23, 12, 00, 00),
				LocalDateTime.of(2021, 12, 23, 14, 00, 00),
				"SEM lecture");

		Validator handler = new CheckAvailabilityValidator();
		handler.setNext(new EmployeesCannotReserveMoreThanTwoWeeksInAdvance());
		handler.setNext(new EmployeesMakeEditCancelReservationForThemselves());
		handler.setNext(new EmployeesOneReservationDuringTimeSlot());
		handler.setNext(new SecretariesCanOnlyReserveEditForTheirResearchMembers());

		try {
			boolean isValid = handler.handle(reservation);
			System.out.print("Reservation status = " + isValid);
		} catch (InvalidReservationException e) {
			e.printStackTrace();
		}
	}

}
