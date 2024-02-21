package me.xtopz.shooter.models.geometry;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Point {

    private final double x;
    private final double y;

    public static Point of(double x, double y) {
        return new Point(x, y);
    }

    public Point add(Point point) {
        return add(point.x, point.y);
    }

    public Point add(double dx, double dy) {
        return new Point(x + dx, y + dy);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
