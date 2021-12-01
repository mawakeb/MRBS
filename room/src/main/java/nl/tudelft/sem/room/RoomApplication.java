package nl.tudelft.sem.room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class RoomApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomApplication.class, args);

		//sleep a second
		long current = System.currentTimeMillis();
		int i = 0;
		while (System.currentTimeMillis() - current < 1000) {
			i++;
		}

		System.out.println(Communication.getHi());
	}

}
