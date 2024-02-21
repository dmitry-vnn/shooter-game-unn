package me.xtopz.shooter.models;

import javafx.geometry.VerticalDirection;
import lombok.val;
import me.xtopz.shooter.models.geometry.Point;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Animator {

    private final double playgroundHeight;
    private final double playgroundWidth;

    private final Goal bigGoal;
    private final Goal smallGoal;
    private final ExecutorService executorService;

    private boolean animationInProcess;

    private long millisSinceAnimationStart = 0;
    private long deltaMillisSinceArrowAnimationStart = 0;

    private Arrow arrow;

    public Animator(double playgroundHeight, double playgroundWidth,
                    Goal bigGoal, Goal smallGoal
    ) {

        this.playgroundHeight = playgroundHeight;
        this.playgroundWidth = playgroundWidth;

        this.bigGoal = bigGoal;
        this.smallGoal = smallGoal;

        executorService = Executors.newSingleThreadExecutor();
        animationInProcess = false;
    }

    public void play() {
        if (animationInProcess) {
            throw new IllegalStateException("animation is running already");
        }

        animationInProcess = true;
        executorService.submit(() -> {

            while (animationInProcess) {
                if (millisSinceAnimationStart % (1000 / bigGoal.getPixelsPerSecondSpeed()) == 0) {
                    shiftGoalToOnePixel(bigGoal);
                }

                if (millisSinceAnimationStart % (1000 / smallGoal.getPixelsPerSecondSpeed()) == 0) {
                    shiftGoalToOnePixel(smallGoal);
                }

                if (arrow != null) {
                    val millisSinceArrowAnimationStart = millisSinceAnimationStart - deltaMillisSinceArrowAnimationStart;
                    if (millisSinceArrowAnimationStart % (1000 / arrow.getPixelsPerSecondSpeed()) == 0) {
                        shiftArrowToOnePixel();
                    }
                }

                try {
                    Thread.sleep(1);
                    millisSinceAnimationStart += 1;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    private void shiftArrowToOnePixel() {
        val lineShape = arrow.getLineShape();
        if (pointInPlayground(lineShape.getStartPoint()) && pointInPlayground(lineShape.getEndPoint())) {
            arrow.move(Point.of(1, 0));
        } else {
            arrow.destroy();
            arrow = null;
        }
    }

    public void pause() {
        //CollisionDetector.instance.isIntersects(
        //    bigGoal.getCircleShape(),
        //    arrow.getLineShape()
        //);
        animationInProcess = false;
    }

    public void animateArrow(Arrow arrow) {
        if (this.arrow != null) {
            throw new IllegalStateException("arrow is animated already");
        }

        this.arrow = arrow;
        deltaMillisSinceArrowAnimationStart = millisSinceAnimationStart;
    }

    private void shiftGoalToOnePixel(Goal goal) {
        val center = goal.getCircleShape().getCenter();

        val dy = goal.getSpeedDirection() == VerticalDirection.DOWN ? 1 : -1;

        var probablyNewCenter = center.add(0, dy);

        val r = goal.getCircleShape().getRadius();

        if (!(pointInPlayground(probablyNewCenter.add(0, r)) &&
                pointInPlayground(probablyNewCenter.add(0, -r)))) {
            probablyNewCenter = center.add(0, -dy);
            goal.revertSpeedDirection();
        }

        goal.setCircleShapeCenter(probablyNewCenter);

    }

    private boolean pointInPlayground(Point point) {
        return point.getX() >= 0 && point.getX() < playgroundWidth &&
                point.getY() >= 0 && point.getY() < playgroundHeight;
    }
}
