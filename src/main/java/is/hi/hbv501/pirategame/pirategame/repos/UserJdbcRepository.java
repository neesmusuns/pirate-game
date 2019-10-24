package is.hi.hbv501.pirategame.pirategame.repos;

import is.hi.hbv501.pirategame.pirategame.game.objects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository {
    @Autowired
    private
    JdbcTemplate jdbcTemplate;

    public User findUserByCredentials(String username) {
        try {
            User user = jdbcTemplate.queryForObject("select * from USERS where username=?",
                    new Object[]{username},
                    new BeanPropertyRowMapper<>(User.class));

            return user;
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    public int insertUser(User user){
      return 0;
    }
}
