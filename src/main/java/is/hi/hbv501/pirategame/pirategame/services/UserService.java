package is.hi.hbv501.pirategame.pirategame.services;

import is.hi.hbv501.pirategame.pirategame.game.objects.User;
import is.hi.hbv501.pirategame.pirategame.repos.UserJdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final
    UserJdbcRepository repository;

    public UserService(UserJdbcRepository repository) {
        this.repository = repository;
    }

    public User findUserByCredentials(String username){
        return repository.findUserByCredentials(username);
    }

    public void insertUser(User user){
        if(findUserByCredentials(user.getUsername()) != null)
            repository.updateUser(user);
        else
            repository.insertUser(user);
    }
}
