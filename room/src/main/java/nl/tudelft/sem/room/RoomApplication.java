package nl.tudelft.sem.room;

import nl.tudelft.sem.room.communication.Communication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomApplication.class, args);

		//sleep a second
		long current = System.currentTimeMillis();
		while (System.currentTimeMillis() - current < 1000) {
			System.out.print(".");
		}

		System.out.println(Communication.getHi());
	}

}
