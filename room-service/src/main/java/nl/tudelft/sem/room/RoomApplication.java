package nl.tudelft.sem.room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RoomApplication {

	/**
	 * Main application for the room service.
	 *
	 * @param args the supplied command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(RoomApplication.class, args);
	}

}


