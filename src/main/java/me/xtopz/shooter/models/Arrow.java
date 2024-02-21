package me.xtopz.shooter.models;

import lombok.AccessLevel;
import lombok.Getter;
import me.xtopz.shooter.models.geometry.LineShape;
import me.xtopz.shooter.models.geometry.Point;
import me.xtopz.shooter.observer.GenericPropertyChangeEvent;
import me.xtopz.shooter.observer.GenericPropertyChangeListener;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

@Getter
public class Arrow {

    @Getter(value = AccessLevel.NONE)
    private final PropertyChangeSupport propertyChangeSupport;

    private final LineShape lineShape;
    private final int pixelsPerSecondSpeed;


    public Arrow(LineShape lineShape, int pixelsPerSecondSpeed) {
        this.lineShape = lineShape;
        this.pixelsPerSecondSpeed = pixelsPerSecondSpeed;

        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void move(Point point) {
        lineShape.move(point);
        notifyMoveObservers();
    }

    public void destroy() {
        notifyDestroyObservers();
    }

    private void notifyDestroyObservers() {
        propertyChangeSupport.firePropertyChange("destroy", null, null);
    }

    private void notifyMoveObservers() {
        propertyChangeSupport.firePropertyChange(new GenericPropertyChangeEvent<>(
                this, "lineShape", null, lineShape
        ));
    }

    public void addMoveObserver(GenericPropertyChangeListener<LineShape> lineShapeChangeListener) {
        propertyChangeSupport.addPropertyChangeListener("lineShape", lineShapeChangeListener);
    }

    public void addDestroyObserver(PropertyChangeListener destroyListener) {
        propertyChangeSupport.addPropertyChangeListener("destroy", destroyListener);
    }



}
