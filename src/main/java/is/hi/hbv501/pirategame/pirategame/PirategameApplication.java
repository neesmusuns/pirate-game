package is.hi.hbv501.pirategame.pirategame;

import is.hi.hbv501.pirategame.pirategame.game.GameManager;
import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.repos.GameJdbcRepository;
import is.hi.hbv501.pirategame.pirategame.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PirategameApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private
	GameService service;

	public static void main(String[] args) {

		SpringApplication.run(PirategameApplication.class, args);
		GameManager.Initialize();
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("GameObject ID 0 -> {}", service.findGameObjectById(0));
		logger.info("GameObject ID 1 -> {}", service.findGameObjectById(1));

		GameObject go = service.findGameObjectById(2);
		logger.info("-> {}", go.getID());
	}
}
