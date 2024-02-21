package me.xtopz.shooter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ShooterApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/game-view.fxml"));

        primaryStage.setScene(new Scene(fxmlLoader.load()));
        primaryStage.setTitle("Shooter game");
        primaryStage.setResizable(false);

        primaryStage.show();
    }


}
