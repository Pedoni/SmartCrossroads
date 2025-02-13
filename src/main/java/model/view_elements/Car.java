package model.view_elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Car {
    private double x;
    private double y;
    private double width;
    private double height;
    private double angle = 0;
    private final Image carImage;
    private static final double MAX_SPEED = 2;
    private double speed = MAX_SPEED;
    private static final double SCALE_FACTOR = 0.05;

    public Car(int type, double x, double y) {
        this.carImage = new Image("file:src/main/resources/it/unibo/smartcrossroads/car" + type + "_s.png");
        this.width = carImage.getWidth() * SCALE_FACTOR;
        this.height = carImage.getHeight() * SCALE_FACTOR;
        this.x = x - width / 2;
        this.y = y - height / 2;
        ;
    }

    public void move(double newX, double newY) {
        double dx = newX - x - width / 2;
        double dy = newY - y - height / 2;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > speed) {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
            angle = Math.toDegrees(Math.atan2(dy, dx));
        } else {
            x = newX - width / 2;
            y = newY - height / 2;
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
