package pl.pacinho.muonlinewebtrader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MuOnlineWebTraderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuOnlineWebTraderApplication.class, args);
	}

}