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

    private Goal bigGoal;
    private Goal smallGoal;

    private Scoreboard scoreboard;

    @FXML
    private void initialize() {

        scoreboard = createScoreboard();

        bigGoal = createGoal(bigCircle, 50, VerticalDirection.DOWN);
        smallGoal = createGoal(smallCircle, 100, VerticalDirection.UP);

        animator = new Animator(playground.getPrefHeight(), playground.getPrefWidth(), bigGoal, smallGoal);

        animator.play();
    }

    private Scoreboard createScoreboard() {
        val scoreboard = new Scoreboard();

        scoreboard.addScoreChangeObserver(event -> Platform.runLater(() -> playerScore.setText(event.getNewValue().toString())));
        scoreboard.addShotsChangeObserve(event -> Platform.runLater(() -> playerShots.setText(event.getNewValue().toString())));

        return scoreboard;
    }

    private Goal createGoal(Circle drawableCircle, int speed, VerticalDirection speedDirection) {
        Goal goal = new Goal(
                new CircleShape(
                        drawableCircle.getRadius(),
                        new Point(drawableCircle.getLayoutX(), drawableCircle.getLayoutY())
                ),
                speed,
                speedDirection
        );

        goal.addObserver(event -> Platform.runLater(() -> {
            val center = event.getNewValue();
            drawableCircle.setLayoutX(center.getX());
            drawableCircle.setLayoutY(center.getY());
        }));

        return goal;
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

        val bigGoalCollisionDetector = new CollisionDetector(arrow, bigGoal);

        bigGoalCollisionDetector.addCollisionDetectObserver(event -> scoreboard.addScore(1));

        val smallGoalCollisionDetector = new CollisionDetector(arrow, smallGoal);

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
