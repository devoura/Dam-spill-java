package network;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import logic.MoveLogic;
import model.*;
import java.io.Serializable;
import java.util.Optional;

/**
 * Created by Webby Debby on 11/20/2016.
 */
public class Game {

	private JavafxRoot root;
    private boolean isServer = false;
    private NetworkConnection connection;
    private Boolean yourTurn;
    private Boolean canCapture = false;
    //Game er en singleton
    private static Game game = null;
    private String username;
    private Tile highLightTile;
    private String ipAddress;
    private final Optional<ButtonType> localResult;

    private Game() {

        /**
         * Her instansierer vi alle dialogene som må svares for
         * å finne ut hvilket brukernavn du vil ha, om du vil drifte/koble til
         * serveren/klienten på localhost eller ipaddresse.
         */
        Optional<ButtonType> serverResult = Dialogs.showServerDialog();
        localResult = Dialogs.showLocalDialog();
        Optional<String> loginResult = Dialogs.showUsernameDialog();
        if (serverResult.get() == Dialogs.getServerButton()) {
            isServer = true;
            if (localResult.get() == Dialogs.getNetButton()){
                Dialogs.showServerIpDialog();
            }
        }else if(localResult.get() == Dialogs.getNetButton()){
            ipAddress = new TextInputDialog("Input IPaddress").showAndWait().get();
        }else ipAddress = "127.0.0.1";
        connection = isServer ? createServer() : createClient();
        try {
            connection.startConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (loginResult.isPresent() && connection != null){
            username = loginResult.get();
            yourTurn = isServer;
        }else if (!loginResult.isPresent() && connection != null) {
            username = "John Doe";
        }else {
            System.out.println("The connection has failed");
        }

    }

    public static Game getGame() {

        //lazy loading
        if (game == null){
            //threadsafe
            synchronized (Game.class){
                game = new Game();
            }
        }
        return game;

    }

    private Server createServer() {

        System.out.println("I am a server");
        //Platform.runLater() gjør at servertråden syncer opp med Javafx tråden
        return new Server(data -> Platform.runLater(() -> consumeData(data)), localResult.get() != Dialogs.getNetButton());

    }

    private Client createClient() {

        System.out.println("I am a client");
        return new Client(ipAddress, data -> Platform.runLater(() -> consumeData(data)));

    }

    /**
     * Denne metoden blir brukt her som et "consumer" objekt.
     * data vi sender med send() metoden blir brukt av denne metoden
     * som befinner seg på mottakersiden men ble definert på senderens
     * side.
     * Det er som om vi tar data, stapper det inn i denne metoden,
     * så sender vi hele metoden med dataen som ett objekt til andre siden av
     * forbindelsen hvor den så "consumer" dataen med funskjonen.
     */
    private void consumeData(Serializable data) {

        if(data.getClass().equals(MovePacket.class)){
            MovePacket movePacket = (MovePacket) data;
            if (highLightTile != null){root.getChildren().remove(highLightTile);}
            highLightTile = new Tile(movePacket.getNewX(), movePacket.getNewY());
            root.getChildren().add(highLightTile);
            if(movePacket.getX1() == 0) {
                movePacket.movePiece();
            }
            //Hvis x1 ikke er lik 0 så betyr det at vi har koordinatene til en brikke
            //som skal kapres og vi må bruke metoden capturePiece()
            else {
                movePacket.capturePiece();
            }
            yourTurn = true;
            canCapture = Board.getBoard().getPieceGroup().getChildren()
                    .stream()
                    .filter(e -> MoveLogic.getML().canCaptureAdapter(e))
                    .count() != 0;
        }
        //Strings sent via chatten consumes her
        else if(data.getClass().equals(String.class)){
            ChatBox.messages.appendText(data.toString() + "\n");
        }
        //artig loop for å legge til navn på begge spillerne
        //i både serverens instans og klientens.
        //starter i klienten, sendes til serveren som sender
        //tilbake pga isServer er true men den slutter der
        //fordi klientens isServer er false
        else if (data.getClass().equals(UsernamePacket.class)){
            root.getChildren().add(new UsernamePane((UsernamePacket) data));
            if (isServer) try {
                connection.send(new UsernamePacket(username, true));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            //hvis data er av en klasse som ikke er fanget opp
            //av funskjonen over blir klassen printet ut
            System.out.println(data.getClass());
        }
    }

    public void setStage(Stage primaryStage){
        primaryStage.setScene(new Scene(root = new JavafxRoot()));
        primaryStage.setTitle("Webby Debby Dam");
        primaryStage.show();
    }
    
    public String getUsername(){
    	return username;
    }

    public boolean isServer() {
		return isServer;
	}

	public NetworkConnection getConnection() {
		return connection;
	}

	public Boolean yourTurn() {
		return yourTurn;
	}
	
	public void setYourTurn(Boolean yourTurn){
		this.yourTurn = yourTurn;
	}

	public Boolean canCapture() {
		return canCapture;
	}

	public void redWin() {
        if(isServer)Dialogs.showLoserAlert(); else Dialogs.showWinnerAlert();
    }

    public void whiteWin() {
        if (isServer)Dialogs.showWinnerAlert(); else Dialogs.showLoserAlert();
    }

	public void setCanCapture(Boolean canCapture) {
		this.canCapture = canCapture;
	}

}
