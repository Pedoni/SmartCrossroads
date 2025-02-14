package model.view_elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Constants;

public class Car {
    private int id;
    private double x;
    private double y;
    private double width;
    private double height;
    private double angle;
    private final Image carImage;

    public Car(int id, int type, double x, double y) {
        this.id = id;
        this.carImage = new Image("file:src/main/resources/it/unibo/smartcrossroads/car" + type + "_s.png");
        this.width = carImage.getWidth() * Constants.CAR_SCALE_FACTOR;
        this.height = carImage.getHeight() * Constants.CAR_SCALE_FACTOR;
        this.x = x - width / 2;
        this.y = y - height / 2;
        this.angle = 0;
    }

    public int getId() {
        return this.id;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void move(double newX, double newY, double newAngle) {
        this.x = newX;
        this.y = newY;
        this.angle = newAngle;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(x + width / 2, y + height / 2);
        gc.rotate(angle);
        gc.drawImage(carImage, -width / 2, -height / 2, width, height);
        gc.restore();
    }
}
