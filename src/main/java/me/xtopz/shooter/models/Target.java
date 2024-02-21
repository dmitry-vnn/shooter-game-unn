package me.xtopz.shooter.models;

import javafx.geometry.VerticalDirection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import me.xtopz.shooter.models.geometry.CircleShape;
import me.xtopz.shooter.models.geometry.Point;
import me.xtopz.shooter.observer.GenericPropertyChangeEvent;
import me.xtopz.shooter.observer.GenericPropertyChangeListener;

import java.beans.PropertyChangeSupport;

@Getter
public class Target {

    @Getter(value = AccessLevel.NONE)
    private final PropertyChangeSupport propertyChangeSupport;

    private final CircleShape circleShape;
    private final int pixelsPerSecondSpeed;

    private VerticalDirection speedDirection;

    public Target(CircleShape circleShape, int pixelsPerSecondSpeed, VerticalDirection speedDirection) {
        this.circleShape = circleShape;

        this.pixelsPerSecondSpeed = pixelsPerSecondSpeed;
        this.speedDirection = speedDirection;

        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addObserver(GenericPropertyChangeListener<Point> observer) {
        propertyChangeSupport.addPropertyChangeListener(observer);
    }

   public void setCircleShapeCenter(Point center) {
       val oldCenter = circleShape.getCenter();

       circleShape.setCenter(center);

       propertyChangeSupport.firePropertyChange(new GenericPropertyChangeEvent<>(
               this, "center", oldCenter, center
       ));
   }

    public void revertSpeedDirection() {
        speedDirection = speedDirection == VerticalDirection.UP ?
                VerticalDirection.DOWN : VerticalDirection.UP;
    }
}
