package is.hi.hbv501.pirategame.pirategame.services;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import is.hi.hbv501.pirategame.pirategame.repos.GameJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    @Autowired
    private
    GameJdbcRepository repository;

    public GameObject findGameObjectById(long id){
        return repository.findGameObjectById(id);
    }
}
