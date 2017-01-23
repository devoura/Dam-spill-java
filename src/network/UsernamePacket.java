package network;

import java.io.Serializable;

/**
 * Created by Webby Debby on 11/26/2016.
 */
//Trenger denne klassen fordi en vanlig String
//ville blitt tolket som en melding fra chatten
public class UsernamePacket implements Serializable {
    private final Boolean isWhite;
    private final String username;

    public UsernamePacket(String username, Boolean isWhite){
        this.username = username;
        this.isWhite = isWhite;
    }

    public String getUsername() {
        return username;
    }
    
    public Boolean isWhite(){
    	return isWhite;
    }

}
