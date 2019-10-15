package is.hi.hbv501.pirategame.pirategame.game.objects;

public class User {
    private String SessionID;
    private String keyPresses;

    public User(String SessionID){
        this.SessionID = SessionID;
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
}
