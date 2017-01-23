import javafx.application.Application;
import javafx.stage.Stage;
import network.Game;

/**
 * Created by Webby Debby on 11/18/2016.
 */

//Javafx m� bruke et entry point som extender Application
//Javafx har en egen tr�d som h�ndterer bl.a grafiske elementer som instansieres her med start metoden
public class EntryPoint extends Application{

    @Override
    public void start(Stage primaryStage) {
        Game.getGame().setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
