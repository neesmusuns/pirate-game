package is.hi.hbv501.pirategame.pirategame.game.objects;

public class User {
    /**
     * Username of user
     */
    private String username;

    /**
     * Password of user
     */
    private String password;

    /**
     * The current SessionID of the user, set upon client connection
     */
    private String SessionID;

    /**
     * A list of key presses. Can be parsed as input
     */
    private String keyPresses;

    /**
     * ID reference to the user's player object
     */
    private long PlayerObjectID;

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(){

    }

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public String getKeyPresses() {
        return keyPresses;
    }

    public void setKeyPresses(String keys){
        this.keyPresses = keys;
    }

    public void clearKeyPresses() {
        this.keyPresses = "";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
