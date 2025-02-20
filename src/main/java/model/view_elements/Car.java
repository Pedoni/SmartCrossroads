package model.view_elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Constants;
import utils.Utils;

public class Car {
    private int id;
    private double x;
    private double y;
    private int posX;
    private int posY;
    private double width;
    private double height;
    private double angle;
    private final Image carImage;
    private double speed;

    public Car(int id, int type, int posX, int posY) {
        this.id = id;
        this.carImage = Utils.carImages.get(type);
        this.width = carImage.getWidth() * Constants.CAR_SCALE_FACTOR;
        this.height = carImage.getHeight() * Constants.CAR_SCALE_FACTOR;
        this.posX = posX;
        this.posY = posY;
        this.x = posX * 50;
        this.y = posY * 50;
        this.angle = 0;
        this.speed = Constants.MAX_SPEED;
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

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public double getSpeed() {
        return this.speed;
    }

    public void move(int posX, int posY) {
        double dx = posX * 50 - this.x;
        double dy = posY * 50 - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > speed) {
            this.x += (dx / distance) * speed;
            this.y += (dy / distance) * speed;
            this.angle = Math.toDegrees(Math.atan2(dy, dx));
        } else {
            this.x = posX * 50;
            this.y = posY * 50;
        }
        this.posX = (int) (this.x / 50);
        this.posY = (int) (this.y / 50);
    }

    public void setPosition(int posX, int posY) {
        this.x = posX * 50;
        this.y = posY * 50;
        this.posX = posX;
        this.posY = posY;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(x + width / 2, y + height / 2);
        gc.rotate(angle);
        gc.drawImage(carImage, -width / 2, -height / 2, width, height);
        gc.restore();
    }
}
