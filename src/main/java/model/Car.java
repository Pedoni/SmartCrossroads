package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Configuration;
import utils.Constants;
import utils.Utils;

public class Car {
    private final int id;
    private double x;
    private double y;
    private final double width;
    private final double height;
    private double angle;
    private final Image carImage;
    private final double speed;
    private final int size;

    public Car(int id, int type, int posX, int posY) {
        this.id = id;
        this.carImage = Utils.getCarImages().get(type);
        this.width = Configuration.TILE_SIZE;
        this.height = Configuration.TILE_SIZE;
        this.x = posX * Configuration.TILE_SIZE;
        this.y = posY * Configuration.TILE_SIZE;
        this.angle = 0;
        this.speed = Constants.MAX_SPEED;
        this.size = Configuration.TILE_SIZE;
    }

    public int getId() {
        return this.id;
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
        return distance == 0;
    }

    public void setPosition(int posX, int posY) {
        this.x = posX * size;
        this.y = posY * size;
    }

    public void draw(GraphicsContext gc) {
        gc.save();
        gc.translate(x + width / 2, y + height / 2);
        gc.rotate(angle);
        gc.drawImage(carImage, -width / 2, -height / 2, width, height);
        gc.restore();
    }
}
