package is.hi.hbv501.pirategame.pirategame.game.datastructures;

import is.hi.hbv501.pirategame.pirategame.game.objects.Tile;
import is.hi.hbv501.pirategame.pirategame.game.util.OpenSimplexNoise;
import is.hi.hbv501.pirategame.pirategame.game.util.SquareGradient;

public class World {

    /**
     * The default tile size for all world tiles
     */
    private Vector2 tileSize = new Vector2(20,20);

    /**
     * The base x and y scale for the tiles
     */
    private float tileScale = 2;

    /**
     * A 2D array containing all world tiles
     */
    private Tile[][] tiles;

    /**
     * The seed for the world's generation algorithm
     */
    private int seed = 2;

    private int width;

    private int height;

    /**
     * Generates a world
     * @param x amount of tiles in the x-dimension
     * @param y amount of tiles in the y-dimension
     */
    public void generateWorld(int x, int y){
        width = x;
        height = y;
        tiles = new Tile[x][y];

        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                OpenSimplexNoise noise = new OpenSimplexNoise(seed);
                double[][] squareGradient = SquareGradient.Generate(x, y);
                double n = Math.abs(noise.eval(i, j)) + 0.6 - squareGradient[i][j];
                Tile t = new Tile();
                t.setScale(new Vector2(tileScale, tileScale));

                if(n > 0.7){
                    t.setLand(true);
                    t.setPassable(true);
                    t.setSprite("beach1");
                }else{
                    t.setLand(false);
                    t.setPassable(true);
                    t.setSprite("sea1");
                }

                t.setPosition(new Vector2(tileSize.getX()*i*tileScale, tileSize.getY()*j*tileScale));

                tiles[i][j] = t;

            }
        }

        int iterations = 2;
        //Patch up holes
        for(int n = 0; n < iterations; n++) {
            for (int i = 1; i < x - 1; i++) {
                for (int j = 1; j < y - 1; j++) {
                    int borderingLandTiles = 0;
                    if (tiles[i - 1][j].isLand())
                        borderingLandTiles++;
                    if(tiles[i + 1][j].isLand())
                        borderingLandTiles++;
                    if(tiles[i][j + 1].isLand())
                        borderingLandTiles++;
                    if(tiles[i][j - 1].isLand())
                        borderingLandTiles++;

                    if (borderingLandTiles >= 3) {
                        tiles[i][j].setLand(true);
                        tiles[i][j].setPassable(true);
                        tiles[i][j].setSprite("beach1");
                    }
                }
            }
        }

        //Remove single land square
        for(int i = 1; i < x-1; i++){
            for(int j = 1; j < y-1; j++) {
                if(tiles[i][j].isLand()) {
                    boolean shouldStay = true;
                    if (!tiles[i - 1][j].isLand() && !tiles[i + 1][j].isLand() &&
                        !tiles[i][j + 1].isLand() && !tiles[i][j - 1].isLand())
                        shouldStay = false;

                    if (!shouldStay) {
                        tiles[i][j].setLand(false);
                        tiles[i][j].setPassable(true);
                        tiles[i][j].setSprite("sea1");
                    }
                }
            }
        }
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
