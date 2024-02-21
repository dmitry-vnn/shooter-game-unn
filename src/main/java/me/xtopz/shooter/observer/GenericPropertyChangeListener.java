package me.xtopz.shooter.observer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public interface GenericPropertyChangeListener<T> extends PropertyChangeListener {

    void propertyChange(GenericPropertyChangeEvent<T> event);

    @Override
    default void propertyChange(PropertyChangeEvent event) {
        propertyChange((GenericPropertyChangeEvent<T>) event);
    }
}
