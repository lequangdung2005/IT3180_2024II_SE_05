package com.hust.ittnk68.cnpm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Server {

	// @EventListener({ ContextClosedEvent.class })
	// void exitGracefully() {
	// 	
	// }

	public static void main(String[] args) {
		try {
			SpringApplication.run(Server.class, args);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
