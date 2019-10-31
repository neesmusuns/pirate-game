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
            return jdbcTemplate.queryForObject("select * from USERS where username=?",
                    new Object[]{username},
                    new BeanPropertyRowMapper<>(User.class));
        }
        catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    public int insertUser(User user) {
        return jdbcTemplate.update("insert into USERS (username, password, money," +
                                                          " headwear, shirt, pants, boat," +
                                                          " drinks, treasure) " + "values(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                user.getUsername(), user.getPassword(), user.getMoney(), user.getHeadwear(),
                user.getShirt(), user.getPants(), user.getBoat(), user.getDrinks(), user.getTreasure());
    }

    public int updateUser(User user) {
        return jdbcTemplate.update("update USERS set money = ?," +
                                                        " headwear = ?, shirt = ?, pants = ?, boat = ?," +
                                                        " drinks = ?, treasure = ?" + "where username = ?",
                user.getMoney(), user.getHeadwear(), user.getShirt(), user.getPants(),
                user.getBoat(), user.getDrinks(), user.getTreasure(), user.getUsername());
    }
}
