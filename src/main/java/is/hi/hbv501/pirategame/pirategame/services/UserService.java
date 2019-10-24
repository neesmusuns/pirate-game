package is.hi.hbv501.pirategame.pirategame.services;

import is.hi.hbv501.pirategame.pirategame.game.objects.User;
import is.hi.hbv501.pirategame.pirategame.repos.UserJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private
    UserJdbcRepository repository;

    public User findUserByCredentials(String username){
        return repository.findUserByCredentials(username);
    }
}
