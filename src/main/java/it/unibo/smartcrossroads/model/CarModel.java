package it.unibo.smartcrossroads.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class CarModel {
    private double x;
    private double y;
    private boolean movingRight = true;
    private final Image carImage;

    public CarModel(double x, double y, int type) {
        this.x = x;
        this.y = y;
        this.carImage = new Image("file:src/main/resources/it/unibo/smartcrossroads/car" + type + ".png");
    }

    public void move(int WIDTH) {
        if (movingRight) {
            x += 2;
            if (x > WIDTH - 20) {
                movingRight = false;
            }
        } else {
            x -= 2;
            if (x < 0) {
                movingRight = true;
            }
        }
    }

    public void draw(GraphicsContext gc) {
        double originalWidth = carImage.getWidth();
        double originalHeight = carImage.getHeight();
        double scaleFactor = 0.05;
        double carWidth = originalWidth * scaleFactor;
        double carHeight = originalHeight * scaleFactor;

        gc.save();
        if (!movingRight) {
            gc.translate(x + carWidth, y);
            gc.scale(-1, 1);
            gc.drawImage(carImage, 0, 0, carWidth, carHeight);
        } else {
            gc.drawImage(carImage, x, y, carWidth, carHeight);
        }
        gc.restore();
    }
}
