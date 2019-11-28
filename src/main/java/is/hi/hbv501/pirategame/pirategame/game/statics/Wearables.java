package is.hi.hbv501.pirategame.pirategame.game.statics;

public class Wearables {
    public static String getHeadwear(int id){
        switch(id){
            case 1:
                return "parrot1";
            case 2:
                return "sword1";
            default:
                return "bubble";
        }
    }

    public static String getShirt(int id){
        switch(id){
            case 1:
                return "black_shirt";
            default:
                return "bubble";
        }
    }

    public static String getPants(int id){
        switch(id){
            case 1:
                return "black_shirt";
            default:
                return "bubble";
        }
    }

}
