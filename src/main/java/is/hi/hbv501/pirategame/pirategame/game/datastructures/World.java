package is.hi.hbv501.pirategame.pirategame.game.datastructures;

import is.hi.hbv501.pirategame.pirategame.PirategameApplication;
import is.hi.hbv501.pirategame.pirategame.game.objects.Shop;
import is.hi.hbv501.pirategame.pirategame.game.objects.Tile;
import is.hi.hbv501.pirategame.pirategame.services.GameService;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

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
     */
    public void generateWorld(String textFile, int worldIndex) {

        Scanner sc = GetTextFileScanner(textFile);
        Scanner counterSc = GetTextFileScanner(textFile);

        int len = 0;
        while (counterSc.hasNextLine()) {
            len++;
            counterSc.nextLine();
        }

        String[] lines = new String[len];
        int counter = 0;
        while(sc.hasNextLine()){
            lines[counter] = sc.nextLine();
            counter++;
        }

        int x = lines[0].length(), y = lines.length;
        tiles = new Tile[x][y];
        width = x;
        height = y;


        for(int j = 0; j < lines.length ; j++){
            String[] line = lines[j].split("");
            for(int i = 0; i < line.length; i++){
                String tileChar = line[i];
                Tile t = new Tile(gameService, worldIndex);
                t.setScale(new Vector2(tileScale, tileScale));

                switch (tileChar) {
                    case ".":
                        t.setLand(true);
                        t.setSprite("beach1");
                        break;
                    case "*":
                        t.setLand(false);
                        t.setSprite("sea1");
                        break;
                    case "-":
                        t.setLand(true);
                        t.setSprite("beach1");
                        break;
                    case "s":
                        t = new Shop(gameService, worldIndex);
                        t.setLand(true);
                        t.setPassable(false);
                        t.setZIndex(1);
                        break;
                    case "p":
                        t.setLayer("land");
                        t.setLand(true);
                        t.setSprite("pier");
                        t.setZIndex(1);
                        break;
                    case "0":
                        t.setLand(true);
                        t.setSprite("underwater1");
                        break;
                    case "2":
                        t.setLand(false);
                        t.setSprite("underwater2");
                        break;
                    default:
                        t.setLand(true);
                        t.setSprite("beach1");
                        break;
                }

                t.setPosition(new Vector2(tileSize.getX()*i*tileScale, tileSize.getY()*j*tileScale));
                tiles[i][j] = t;
            }
        }

        /*
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
        */

    }

    private Scanner GetTextFileScanner(String textFile){
        ClassPathResource resource = new ClassPathResource(textFile);
        File file = null;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert file != null;
        FileReader fr = null;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert fr != null;

        return new Scanner(fr);
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
