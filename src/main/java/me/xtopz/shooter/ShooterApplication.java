package me.xtopz.shooter;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ShooterApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private VBox mainContainer;
    private HBox bodyContainer;
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        initPrimaryStage(primaryStage);

        GameLoop gameLoop = new GameLoop(canvas.getGraphicsContext2D());
        gameLoop.start();

        primaryStage.show();
    }

    private void initPrimaryStage(Stage primaryStage) {
        Label topLabel = new Label("Hello, JavaFX!");

        mainContainer = new VBox();
        bodyContainer = new HBox();

        canvas = new Canvas(300, 300);

        canvas.getGraphicsContext2D().setFill(Color.BLUE);

        mainContainer.getChildren().add(topLabel);
        mainContainer.getChildren().add(bodyContainer);
        bodyContainer.getChildren().add(new Rectangle(5, 5, 30, 30));
        bodyContainer.getChildren().add(canvas);
        bodyContainer.getChildren().add(new Rectangle(5, 5, 30, 30));


        Scene scene = new Scene(mainContainer);

        primaryStage.setTitle("Simple JavaFX Application");
        primaryStage.setScene(scene);
    }

    private class GameLoop extends AnimationTimer {

        private final GraphicsContext graphics;
        private long startGameLoopTime;

        public GameLoop(GraphicsContext graphicsContext2D) {
            this.graphics = graphicsContext2D;

            graphics.setFill(Color.BLUE);
        }

        @Override
        public void start() {
            startGameLoopTime = System.nanoTime();
            super.start();
        }

        @Override
        public void handle(long currentNanoTimes) {
            var deltaTimeSeconds = (currentNanoTimes - startGameLoopTime) / 1E9;

            double c = ((int)(deltaTimeSeconds) % 255) / 255d;

            graphics.setFill(Color.color(c, c, c));
            graphics.rect(0,0,100, 100);
        }
    }

}
