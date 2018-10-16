package gDrive.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUp {

	@PostConstruct
	public void afterApplicationStartUp() {
		System.out.println("************* Application execution started *************");

	}
}
