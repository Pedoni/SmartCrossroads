package model.view_elements;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.Configuration;
import utils.LightColor;

public final class Tile {

    private final int posX;
    private final int posY;
    private final double x;
    private final double y;
    private final double size;
    private LightColor color;
    private int trafficLightId;

    @Override
    public boolean equals(Object obj) {
        Tile other = (Tile) obj;
        return this.posX == other.getPosX() && this.posY == other.getPosY();
    }

    @Override
    public int hashCode() {
        return posX * 31 + posY;
    }

    public Tile(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.size = Configuration.TILE_SIZE;
        this.x = posX * size;
        this.y = posY * size;
        this.trafficLightId = -1;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }


    public void setColor(LightColor color) {
        this.color = color;
    }


    public int getTrafficLightId() {
        return this.trafficLightId;
    }

    public void setTrafficLightId(int trafficLightId) {
        this.trafficLightId = trafficLightId;
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
