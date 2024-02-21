package me.xtopz.shooter.models;

import javafx.geometry.VerticalDirection;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.xtopz.shooter.models.geometry.Point;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor
public class Animator {

    private ExecutorService executorService = Executors.newSingleThreadExecutor();


    private final double playgroundHeight;
    private final double playgroundWidth;

    private final Goal bigGoal;
    private final Goal smallGoal;

    private boolean animationInProcess;

    private long millisSinceAnimationStart;
    //private long deltaMillisSinceArrowAnimationStart;

    private Map<Long, Arrow> arrows = new HashMap<>();


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

                val destroyedArrowsIds = new HashSet<Long>();

                arrows.forEach((animationMillisSinceArrowStart, arrow) -> {
                    val millisSinceArrowAnimationStart = millisSinceAnimationStart - animationMillisSinceArrowStart;
                    if (millisSinceArrowAnimationStart % (1000 / arrow.getPixelsPerSecondSpeed()) == 0) {
                        if (canMoveArrow(arrow)) {
                            shiftArrowToOnePixel(arrow);
                        } else {
                            arrow.destroy();
                            destroyedArrowsIds.add(animationMillisSinceArrowStart);
                        }
                    }
                });

                destroyedArrowsIds.forEach(id -> arrows.remove(id));

                try {
                    Thread.sleep(1);
                    millisSinceAnimationStart += 1;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    private boolean canMoveArrow(Arrow arrow) {
        val lineShape = arrow.getLineShape();
        return (pointInPlayground(lineShape.getStartPoint()) && pointInPlayground(lineShape.getEndPoint()));
    }

    private void shiftArrowToOnePixel(Arrow arrow) {
        arrow.move(Point.of(1, 0));
    }

    public void pause() {
        animationInProcess = false;
    }

    public void animateArrow(Arrow arrow) {
        arrows.put(millisSinceAnimationStart, arrow);
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
