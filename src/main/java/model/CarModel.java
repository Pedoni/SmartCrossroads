package model;

import javafx.scene.image.Image;
import utils.Constants;
import utils.Utils;

public class CarModel {
    private int id;
    private double x;
    private double y;
    private double width;
    private double height;
    private double angle;
    private double speed;

    private final Image carImage;
    private LinkedPoint currentPoint;
    private LinkedPoint targetPoint;

    public CarModel(int id, int type, LinkedPoint initialPoint) {
        this.id = id;
        this.carImage = Utils.carImages.get(type);
        this.width = carImage.getWidth() * Constants.CAR_SCALE_FACTOR;
        this.height = carImage.getHeight() * Constants.CAR_SCALE_FACTOR;
        this.x = initialPoint.getPosX() * 50;
        this.y = initialPoint.getPosY() * 50;
        this.currentPoint = initialPoint;
        this.angle = 0;
        this.speed = Constants.MAX_SPEED;
        selectNewTarget();
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

    public double getAngle() {
        return this.angle;
    }

    private void selectNewTarget() {
        if (!this.currentPoint.getDestinations().isEmpty()) {
            this.targetPoint = Utils.map.get(currentPoint.getRandomDestination());
        }
    }

    public boolean hasReachedFinalDestination() {
        return this.currentPoint.getDestinations().isEmpty();
    }

    public void move() {
        if (targetPoint == null)
            return;

        double dx = targetPoint.getPosX() * 50 - this.x;
        double dy = targetPoint.getPosY() * 50 - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > speed) {
            this.x += (dx / distance) * speed;
            this.y += (dy / distance) * speed;
            this.angle = Math.toDegrees(Math.atan2(dy, dx));
        } else {
            this.x = targetPoint.getPosX() * 50;
            this.y = targetPoint.getPosY() * 50;
            this.currentPoint = this.targetPoint;
            selectNewTarget();
        }
    }

}
