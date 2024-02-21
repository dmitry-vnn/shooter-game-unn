package me.xtopz.shooter.observer;

import java.beans.PropertyChangeEvent;

public class GenericPropertyChangeEvent<T> extends PropertyChangeEvent {
    /**
     * Constructs a new {@code PropertyChangeEvent}.
     *
     * @param source       the bean that fired the event
     * @param propertyName the programmatic name of the property that was changed
     * @param oldValue     the old value of the property
     * @param newValue     the new value of the property
     * @throws IllegalArgumentException if {@code source} is {@code null}
     */
    public GenericPropertyChangeEvent(Object source, String propertyName, T oldValue, T newValue) {
        super(source, propertyName, oldValue, newValue);
    }

    @Override
    public T getNewValue() {
        return (T) super.getNewValue();
    }

    @Override
    public T getOldValue() {
        return (T) super.getOldValue();
    }
}
