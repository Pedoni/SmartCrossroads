package model.view_elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.LightColor;

public class Tile {
    private int posX;
    private int posY;
    private double x;
    private double y;
    private double size;
    private LightColor color;
    private TrafficLight trafficLight;

    public Tile(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.size = 50;
        this.x = posX * size;
        this.y = posY * size;
    }

    public double getPosX() {
        return this.posX;
    }

    public double getPosY() {
        return this.posY;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void setColor(LightColor color) {
        this.color = color;
    }

    public LightColor getColor() {
        return color;
    }

    public TrafficLight getTrafficLight() {
        return this.trafficLight;
    }

    public void setTrafficLight(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    public void draw(GraphicsContext gc) {
        if (this.color != null) {
            Color fillColor = switch (color) {
                case RED -> Color.rgb(255, 0, 0, 0.25);
                case GREEN -> Color.rgb(0, 255, 0, 0.25);
                case YELLOW -> Color.rgb(255, 255, 0, 0.25);
            };
            gc.setFill(fillColor);
            gc.fillRect(x, y, size, size);
        }

    }
}
