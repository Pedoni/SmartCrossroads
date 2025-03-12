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
    private int size;

    public Car(int id, int type, int posX, int posY, int size) {
        this.id = id;
        this.carImage = Utils.getCarImages().get(type);
        this.width = carImage.getWidth() * Constants.CAR_SCALE_FACTOR;
        this.height = carImage.getHeight() * Constants.CAR_SCALE_FACTOR;
        this.posX = posX;
        this.posY = posY;
        this.x = posX * size;
        this.y = posY * size;
        this.angle = 0;
        this.speed = Constants.MAX_SPEED;
        this.size = size;
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

    public boolean move(int posX, int posY) {
        double dx = posX * size - this.x;
        double dy = posY * size - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > speed) {
            this.x += (dx / distance) * speed;
            this.y += (dy / distance) * speed;
            this.angle = Math.toDegrees(Math.atan2(dy, dx));
        } else {
            this.x = posX * size;
            this.y = posY * size;
        }
        this.posX = (int) (this.x / size);
        this.posY = (int) (this.y / size);
        return distance == 0;
    }

    public void setPosition(int posX, int posY) {
        this.x = posX * size;
        this.y = posY * size;
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
