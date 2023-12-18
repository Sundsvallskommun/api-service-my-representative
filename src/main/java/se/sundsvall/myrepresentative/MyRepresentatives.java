package se.sundsvall.myrepresentative;

import static org.springframework.boot.SpringApplication.run;

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
		run(MyRepresentatives.class, args);
	}

}
