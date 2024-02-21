package me.xtopz.shooter.models;

import lombok.val;
import me.xtopz.shooter.models.geometry.CircleShape;
import me.xtopz.shooter.models.geometry.LineShape;
import me.xtopz.shooter.models.geometry.Point;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class CollisionDetector {

    private PropertyChangeSupport propertyChangeSupport;

    private final Arrow arrow;
    private final Target target;

    private boolean isArrowIntersectsWithGoalOnce;

    public CollisionDetector(Arrow arrow, Target target) {

        this.arrow = arrow;
        this.target = target;

        propertyChangeSupport = new PropertyChangeSupport(this);

        arrow.addMoveObserver(event -> onArrowMoveListener(event.getNewValue()));
    }

    public void addCollisionDetectObserver(PropertyChangeListener observer) {
        propertyChangeSupport.addPropertyChangeListener(observer);
    }

    private void onArrowMoveListener(LineShape arrowPosition) {
        if (!isArrowIntersectsWithGoalOnce && isIntersects(target.getCircleShape(), arrowPosition)) {
            isArrowIntersectsWithGoalOnce = true;
            notifyObservers();
        }
    }

    private void notifyObservers() {
        propertyChangeSupport.firePropertyChange("collision", null, null);
    }

    private boolean isIntersects(CircleShape circle, LineShape horizontalLine) {

        double y = horizontalLine.getStartPoint().getY();

        val circleCenter = circle.getCenter();

        double d = pow(circle.getRadius(), 2) - pow(y - circleCenter.getY(), 2);

        if (d < 0) {
            return false;
        }

        double squareRootD = sqrt(d);

        double x1 = circleCenter.getX() + squareRootD;
        double x2 = circleCenter.getX() - squareRootD;

        return horizontalLine.isInsidePoint(Point.of(x1, y)) ||
               horizontalLine.isInsidePoint(Point.of(x2, y));

    }
}
