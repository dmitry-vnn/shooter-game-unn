package me.xtopz.shooter.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VerticalDirection;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import lombok.val;
import me.xtopz.shooter.models.*;
import me.xtopz.shooter.models.geometry.CircleShape;
import me.xtopz.shooter.models.geometry.LineShape;
import me.xtopz.shooter.models.geometry.Point;

public class GameWindowController {

    @FXML
    private Label playerScore, playerShots;

    @FXML
    private Circle bigCircle, smallCircle;

    @FXML
    private Pane playground;

    private Animator animator;

    private Target bigTarget;
    private Target smallTarget;

    private Scoreboard scoreboard;

    @FXML
    private void initialize() {

        scoreboard = createScoreboard();

        bigTarget = createGoal(bigCircle, 50, VerticalDirection.DOWN);
        smallTarget = createGoal(smallCircle, 100, VerticalDirection.UP);

        animator = new Animator(playground.getPrefHeight(), playground.getPrefWidth(), bigTarget, smallTarget);

        animator.play();
    }

    private Scoreboard createScoreboard() {
        val scoreboard = new Scoreboard();

        scoreboard.addScoreChangeObserver(event -> Platform.runLater(() -> playerScore.setText(event.getNewValue().toString())));
        scoreboard.addShotsChangeObserve(event -> Platform.runLater(() -> playerShots.setText(event.getNewValue().toString())));

        return scoreboard;
    }

    private Target createGoal(Circle drawableCircle, int speed, VerticalDirection speedDirection) {
        Target target = new Target(
                new CircleShape(
                        drawableCircle.getRadius(),
                        new Point(drawableCircle.getLayoutX(), drawableCircle.getLayoutY())
                ),
                speed,
                speedDirection
        );

        target.addObserver(event -> Platform.runLater(() -> {
            val center = event.getNewValue();
            drawableCircle.setLayoutX(center.getX());
            drawableCircle.setLayoutY(center.getY());
        }));

        return target;
    }


    @FXML
    public void onPlayButtonClick() {
        animator.play();
    }

    @FXML
    public void onPauseButtonClick() {
        animator.pause();
    }

    @FXML
    public void onShootButtonClick() {
        val arrow = createArrow(10, 250);

        val bigGoalCollisionDetector = new CollisionDetector(arrow, bigTarget);

        bigGoalCollisionDetector.addCollisionDetectObserver(event -> scoreboard.addScore(1));

        val smallGoalCollisionDetector = new CollisionDetector(arrow, smallTarget);

        smallGoalCollisionDetector.addCollisionDetectObserver(evt -> scoreboard.addScore(2));

        scoreboard.incrementShots();

        animator.animateArrow(arrow);
    }

    private Arrow createArrow(int width, int speed) {
        val startPoint = Point.of(0, playground.getPrefHeight() / 2);
        val endPoint = Point.of(width, startPoint.getY());

        val drawableLine = new Line(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
        playground.getChildren().add(drawableLine);

        val arrow = new Arrow(new LineShape(startPoint, endPoint), speed);

        arrow.addMoveObserver(event -> {
            Platform.runLater(() -> {
                val newLineShape = event.getNewValue();

                drawableLine.setStartX(newLineShape.getStartPoint().getX());
                drawableLine.setStartY(newLineShape.getStartPoint().getY());

                drawableLine.setEndX(newLineShape.getEndPoint().getX());
                drawableLine.setEndY(newLineShape.getEndPoint().getY());
            });
        });

        arrow.addDestroyObserver(event -> {
            Platform.runLater(() -> playground.getChildren().remove(drawableLine));
        });

        return arrow;
    }
}
