package is.hi.hbv501.pirategame.pirategame.game;

import java.util.List;

public class GameManager {

    private List<GameObject> gameObjects;

    public void Update(){
        gameObjects.forEach(GameObject::Update);
    }
}
