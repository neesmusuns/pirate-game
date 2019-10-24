package is.hi.hbv501.pirategame.pirategame;

import is.hi.hbv501.pirategame.pirategame.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PirategameApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public static void main(String[] args) {
		SpringApplication.run(PirategameApplication.class, args);
		GameService.Initialize();
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
