package me.xtopz.shooter.models;

import me.xtopz.shooter.observer.GenericPropertyChangeEvent;
import me.xtopz.shooter.observer.GenericPropertyChangeListener;

import java.beans.PropertyChangeSupport;

public class Scoreboard {

    private int shots;
    private int score;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public void addScore(int count) {
        score += count;
        notifyScoreChangeObservers();
    }

    public void incrementShots() {
        shots++;
        notifyShotsChangeObservers();
    }

    public void reset() {
        score = 0;
        shots = 0;

        notifyScoreChangeObservers();
        notifyShotsChangeObservers();
    }

    public void addScoreChangeObserver(GenericPropertyChangeListener<Integer> observer) {
        propertyChangeSupport.addPropertyChangeListener("score", observer);
    }

    public void addShotsChangeObserve(GenericPropertyChangeListener<Integer> observer) {
        propertyChangeSupport.addPropertyChangeListener("shots", observer);
    }

    private void notifyScoreChangeObservers() {
        propertyChangeSupport.firePropertyChange(
                new GenericPropertyChangeEvent<>(
                        this, "score", null, score)
        );
    }

    private void notifyShotsChangeObservers() {
        propertyChangeSupport.firePropertyChange(
                new GenericPropertyChangeEvent<>(
                this, "shots", null, shots)
        );
    }


}
