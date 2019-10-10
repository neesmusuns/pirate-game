package is.hi.hbv501.pirategame.pirategame.repos;

import is.hi.hbv501.pirategame.pirategame.game.GameObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class GameJdbcRepository {
    @Autowired
    private
    JdbcTemplate jdbcTemplate;

    public GameObject findGameObjectById(long id) {
        return jdbcTemplate.queryForObject("select * from GAME_OBJECTS where id=?", new Object[] { id },
                new BeanPropertyRowMapper<>(GameObject.class));
    }

    public int insertGameObject(GameObject gameObject){
        return jdbcTemplate.update("insert into GAME_OBJECTS (positionX, positionY, scaleX, scaleY, sprite) "
                                      + "values(?, ?, ?, ?, ?)",
                gameObject.getPosition().getX(), gameObject.getPosition().getY(),
                gameObject.getScale().getX(), gameObject.getScale().getY(),
                gameObject.getSprite());

    }
}
