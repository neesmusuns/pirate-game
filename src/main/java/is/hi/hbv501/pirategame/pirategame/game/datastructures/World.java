package is.hi.hbv501.pirategame.pirategame.game.datastructures;

import is.hi.hbv501.pirategame.pirategame.game.objects.Shop;
import is.hi.hbv501.pirategame.pirategame.game.objects.Tile;
import is.hi.hbv501.pirategame.pirategame.game.util.OpenSimplexNoise;
import is.hi.hbv501.pirategame.pirategame.game.util.SquareGradient;
import is.hi.hbv501.pirategame.pirategame.services.GameService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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

    private GameService gameService;

    public World(GameService gameService){
        this.gameService = gameService;
    }

    /**
     * Generates a world
     * @param x amount of tiles in the x-dimension
     * @param y amount of tiles in the y-dimension
     */
    public void generateWorld(int x, int y) throws UnsupportedEncodingException {
        InputStream in =
                getClass().getResourceAsStream("/data/world.txt");
        Reader fr = new InputStreamReader(in, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(fr);

        br.lines().forEach(l -> {
            String[] line = l.split("");

        });
        width = x;
        height = y;
        tiles = new Tile[x][y];

        for(int i = 0; i < x; i++){
            for(int j = 0; j < y; j++){
                OpenSimplexNoise noise = new OpenSimplexNoise(seed);
                double[][] squareGradient = SquareGradient.Generate(x, y);
                double n = Math.abs(noise.eval(i, j)) + 0.6 - squareGradient[i][j];
                Tile t = new Tile(gameService);
                t.setScale(new Vector2(tileScale, tileScale));

                if(n > 0.7){
                    t.setLand(true);
                    t.setSprite("beach1");
                }else{
                    t.setLand(false);
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
                        tiles[i][j].setSprite("sea1");
                    }
                }
            }
        }



        Shop shop = new Shop(gameService);
        shop.setLand(true);
        shop.setPosition(new Vector2(tileSize.getX()*30*tileScale, tileSize.getY()*30*tileScale));
        shop.setPassable(false);
        shop.setZIndex(1);
        tiles[30][30] = shop;


        tiles[36][18].setLayer("land");
        tiles[36][18].setLand(true);
        tiles[36][18].setSprite("pier");
        tiles[36][18].setZIndex(1);

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
