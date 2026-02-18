package se.sundsvall.myrepresentative;

import org.springframework.cloud.openfeign.EnableFeignClients;
import se.sundsvall.dept44.ServiceApplication;

import static org.springframework.boot.SpringApplication.run;

@ServiceApplication
@EnableFeignClients
public class MyRepresentatives {
	public static void main(String[] args) {
		run(MyRepresentatives.class, args);
	}

}
