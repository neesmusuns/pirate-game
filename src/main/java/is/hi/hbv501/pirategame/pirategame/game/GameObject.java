package is.hi.hbv501.pirategame.pirategame.game;

import is.hi.hbv501.pirategame.pirategame.game.datastructures.Vector2;

public class GameObject {
    /**
     * The game object's position in 2D space.
     */
    private Vector2 position = new Vector2();

    /**
     * The scale of the game object as a Vector2
     */
    private Vector2 scale = new Vector2(1, 1);

    /**
     * The path to the game object's sprite
     */
    private String sprite;

    /**
     * Update is called once every frame.
     */
    void Update(){

    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public String getSprite() {
        return sprite;
    }

    public void setSprite(String sprite) {
        this.sprite = sprite;
    }
}
