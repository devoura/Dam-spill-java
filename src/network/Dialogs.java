package network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

/**
 * Created by Webby Debby on 12/3/2016.
 */

class Dialogs {
	
	private static ButtonType serverButton;
	private static ButtonType netButton;
	
	static Optional<ButtonType> showServerDialog() {
		
		Alert serverAlert = new Alert(Alert.AlertType.CONFIRMATION);
        serverButton = new ButtonType("Server");
        ButtonType clientButton = new ButtonType("Client");
        serverAlert.setTitle("Server || Client");
        serverAlert.setContentText("Do you want to be the server or client?");
        serverAlert.getButtonTypes().setAll(serverButton,clientButton);
        return serverAlert.showAndWait();
	
	}
	
	static Optional<ButtonType> showLocalDialog(){
		
		Alert localAlert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType localButton = new ButtonType("Localhost");
        netButton = new ButtonType("Internet");
        localAlert.setTitle("Local || IP");
        localAlert.setContentText("Do you want to play on localhost or over the net?");
        localAlert.getButtonTypes().setAll(localButton, netButton);
        return localAlert.showAndWait();
	
	}
	
	static Optional<String> showUsernameDialog(){
		
	TextInputDialog login = new TextInputDialog();
    login.setContentText("What is your name?");
    login.setTitle("Login");
    return login.showAndWait();
	
}

	static void showServerIpDialog(){
	
	Alert ipAlert = new Alert(Alert.AlertType.INFORMATION);
    try {
		ipAlert.setContentText("Clients will need this number to connect over the internet: " + InetAddress.getLocalHost().toString());
	} catch (UnknownHostException e) {
		e.printStackTrace();
	}
    ipAlert.showAndWait();

}

	static void showLoserAlert(){
	
	Alert loserAlert = new Alert(Alert.AlertType.INFORMATION);
    loserAlert.setHeaderText("You lost!");
    loserAlert.setContentText("Next time try not being stupid.");
    loserAlert.setTitle("Loser.");
    loserAlert.showAndWait();
    Platform.exit();

}

	static void showWinnerAlert(){
	
	Alert winnerAlert = new Alert(Alert.AlertType.INFORMATION);
    winnerAlert.setHeaderText("You won!");
    winnerAlert.setContentText("You must be some kind of genius!");
    winnerAlert.setTitle("Winner!");
    winnerAlert.showAndWait();
    Platform.exit();

}

	static ButtonType getServerButton() {
		return serverButton;
	}

	static ButtonType getNetButton() {
		return netButton;
	}

}
