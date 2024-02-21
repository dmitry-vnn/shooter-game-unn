package me.xtopz.shooter.models.geometry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

@AllArgsConstructor
@Getter
public class LineShape {

    private Point startPoint;
    private Point endPoint;

    public void move(Point point) {
        startPoint = startPoint.add(point);
        endPoint = endPoint.add(point);
    }

    public boolean isInsidePoint(Point point) {
        val x = point.getX();
        val y = point.getY();

        return x >= startPoint.getX() && x <= endPoint.getX() &&
               y >= startPoint.getY() && y <= endPoint.getY();
    }

    @Override
    public String toString() {
        return "LineShape{" +
                "startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                '}';
    }
}
