package it.unibo.smartcrossroads.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Car {
    private double x;
    private double y;
    private double width;
    private double height;
    private double angle = 0;
    private final Image carImage;
    private final List<Point> path;
    private int currentPointIndex = 0;
    private static final double SPEED = 2;
    private static final double SCALE_FACTOR = 0.05;

    public Car(int type, List<Point> path) {
        this.x = path.get(0).x();
        this.y = path.get(0).y();
        this.carImage = new Image("file:src/main/resources/it/unibo/smartcrossroads/car" + type + ".png");
        this.width = carImage.getWidth() * SCALE_FACTOR;
        this.height = carImage.getHeight() * SCALE_FACTOR;
        this.path = path;
        // this.path = generateAlignedPath(path, getHeight());
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void move() {
        if (currentPointIndex < path.size()) {
            Point targetPoint = path.get(currentPointIndex);
            double dx = targetPoint.x() - x;
            double dy = targetPoint.y() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            if (distance > SPEED) {
                double moveX = (dx / distance) * SPEED;
                double moveY = (dy / distance) * SPEED;
                x += moveX;
                y += moveY;
                angle = Math.toDegrees(Math.atan2(dy, dx));
            } else {
                x = targetPoint.x();
                y = targetPoint.y();
                currentPointIndex++;
                if (currentPointIndex < path.size()) {
                    Point nextTarget = path.get(currentPointIndex);
                    double nextDx = nextTarget.x() - x;
                    double nextDy = nextTarget.y() - y;
                    angle = Math.toDegrees(Math.atan2(nextDy, nextDx));
                }
            }
        }
    }

    public void draw(GraphicsContext gc) {
        double carWidth = getWidth();
        double carHeight = getHeight();

        gc.save();
        gc.translate(x + carWidth / 2, y + carHeight / 2);
        gc.rotate(angle);
        gc.drawImage(carImage, -carWidth / 2, -carHeight / 2, carWidth, carHeight);
        gc.restore();
    }
}
