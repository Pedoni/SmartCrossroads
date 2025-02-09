package it.unibo.smartcrossroads.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import it.unibo.smartcrossroads.utils.Utils;

public class Car {
    private double x;
    private double y;
    private double width;
    private double height;
    private double angle = 0;
    private final Image carImage;
    private LinkedPoint currentPoint;
    private LinkedPoint targetPoint;
    private static final double MAX_SPEED = 2;
    private double speed = MAX_SPEED;
    private static final double SCALE_FACTOR = 0.05;

    public Car(int type, LinkedPoint initialPoint) {
        this.x = initialPoint.getX();
        this.y = initialPoint.getY();
        this.carImage = new Image("file:src/main/resources/it/unibo/smartcrossroads/car" + type + ".png");
        this.width = carImage.getWidth() * SCALE_FACTOR;
        this.height = carImage.getHeight() * SCALE_FACTOR;
        this.currentPoint = initialPoint;
        selectNewTarget();
    }

    private void selectNewTarget() {
        if (!currentPoint.getDestinations().isEmpty()) {
            String destination = currentPoint.getRandomDestination();
            targetPoint = Utils.map.get(destination);
        }
    }

    public void move() {
        if (targetPoint == null)
            return;

        double dx = targetPoint.getX() - x;
        double dy = targetPoint.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > speed) {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
            angle = Math.toDegrees(Math.atan2(dy, dx));
        } else {
            x = targetPoint.getX();
            y = targetPoint.getY();
            currentPoint = targetPoint;
            selectNewTarget();
        }
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(x + width / 2, y + height / 2);
        gc.rotate(angle);
        gc.drawImage(carImage, -width / 2, -height / 2, width, height);
        gc.restore();
    }
}
