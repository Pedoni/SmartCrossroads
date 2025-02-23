package model.view_elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.LightColor;

public final class Tile {

    private final int posX;
    private final int posY;
    private final double x;
    private final double y;
    private final double size;
    private LightColor color;
    private TrafficLight trafficLight;

    @Override
    public boolean equals(Object obj) {
        Tile other = (Tile)obj;
        return this.posX == other.getPosX() && this.posY == other.getPosY();
    }
    @Override
    public int hashCode() {
        return posX * 31 + posY;
    }

    public Tile(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.size = 50;
        this.x = posX * size;
        this.y = posY * size;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
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

    @Override
    public String toString() {
        return String.format("Tile(%d, %d)", posX, posY);
    }
}
