package nl.tudelft.sem.room;

import nl.tudelft.sem.room.communication.ReservationCommunication;
import nl.tudelft.sem.room.communication.UserCommunication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomApplication.class, args);

		//sleep a second
		long current = System.currentTimeMillis();
		while (System.currentTimeMillis() - current < 1000) {
			System.out.print("");
		}

		System.out.println(ReservationCommunication.getHi());
		System.out.println(UserCommunication.getHi());
	}

}
