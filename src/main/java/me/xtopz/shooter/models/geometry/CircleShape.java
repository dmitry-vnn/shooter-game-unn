package me.xtopz.shooter.models.geometry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class CircleShape {

    private final double radius;

    @Setter
    private Point center;

    @Override
    public String toString() {
        return "CircleShape{" +
                "radius=" + radius +
                ", center=" + center +
                '}';
    }
}
