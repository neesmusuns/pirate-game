package is.hi.hbv501.pirategame.pirategame.game.datastructures;

/**
 * A simple data structure representing a point in 2D space.
 */
public class Vector2 {
    private double x;
    private double y;

    /**
     * A simple data structure representing a point in 2D space.
     * @param x The x point in 2D space
     * @param y The y point in 2D space
     */
    public Vector2(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * A simple data structure representing a point in 2D space.<br>
     * Default constructor sets x and y to 0
     */
    public Vector2(){
        this.x = 0;
        this.y = 0;
    }

    /**
     * Calculate distance between this vector and another vector
     * @param other Other vector
     */
    public double Distance(Vector2 other){
        return Math.sqrt(Math.pow(2, Math.abs(this.x - other.x)) +
                Math.pow(2, Math.abs(this.y - other.y)));
    }

    /**
     * Calculate distance between two vectors
     * @param a First vector
     * @param b Second vector
     */
    public static double Distance(Vector2 a, Vector2 b){
        return Math.sqrt(Math.pow(2, Math.abs(a.x - b.x)) +
                Math.pow(2, Math.abs(a.y - b.y)));
    }

    /**
     * Translates the position of the current vector to the desired vector.
     * @param vector Vector to be translated to
     */
    public void Translate(Vector2 vector){
        this.x = vector.x;
        this.y = vector.y;
    }

    /**
     * Translates the position of the current vector to the desired vector.
     * @param x X-coordinate of new vector
     * @param y Y-coordinate of new vector
     */
    public void Translate(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
