package model.view_elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Constants;
import utils.LightColor;
import utils.RoadPosition;

public class TrafficLight {
    private int id;
    private LightColor color;
    private double x;
    private double y;
    private double width;
    private double height;
    private Image red;
    private Image green;
    private Image yellow;
    private RoadPosition position;

    public TrafficLight(int id, boolean startGreen, double x, double y, RoadPosition position) {
        red = new Image("file:src/main/resources/it/unibo/smartcrossroads/red.png");
        green = new Image("file:src/main/resources/it/unibo/smartcrossroads/green.png");
        yellow = new Image("file:src/main/resources/it/unibo/smartcrossroads/yellow.png");
        this.id = id;
        this.color = startGreen ? LightColor.GREEN : LightColor.RED;
        this.x = x;
        this.y = y;
        this.width = red.getWidth() * Constants.TL_SCALE_FACTOR;
        this.height = red.getHeight() * Constants.TL_SCALE_FACTOR;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setColor(LightColor newColor) {
        this.color = newColor;
    }

    public LightColor getColor() {
        return color;
    }

    public void draw(GraphicsContext gc) {
        gc.save();

        double angle = switch (position) {
            case UP -> 0;
            case DOWN -> 180;
            case LEFT -> -90;
            case RIGHT -> 90;
        };

        gc.translate(x, y);
        gc.rotate(angle);
        gc.drawImage(getImageByColor(), -width / 2, -height / 2, width, height);

        gc.restore();
    }

    private Image getImageByColor() {
        return switch (color) {
            case RED -> red;
            case GREEN -> green;
            case YELLOW -> yellow;
        };
    }

}
