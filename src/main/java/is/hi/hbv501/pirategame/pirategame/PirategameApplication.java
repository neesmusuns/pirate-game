package is.hi.hbv501.pirategame.pirategame;

import is.hi.hbv501.pirategame.pirategame.game.GameManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PirategameApplication {

	public static void main(String[] args) {

		SpringApplication.run(PirategameApplication.class, args);
		GameManager.Initialize();
	}

}
