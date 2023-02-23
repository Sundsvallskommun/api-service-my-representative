package se.sundsvall.myrepresentative;

import org.springframework.boot.SpringApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import se.sundsvall.dept44.ServiceApplication;

@ServiceApplication
@EnableFeignClients
@EnableCaching
@EnableScheduling
public class MyRepresentatives {
	public static void main(String[] args) {
		SpringApplication.run(MyRepresentatives.class, args);
	}

}
