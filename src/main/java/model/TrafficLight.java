package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class TrafficLight {
    public enum LightColor {
        RED, YELLOW, GREEN
    }

    public enum RoadPosition {
        UP, DOWN, LEFT, RIGHT
    }

    private LightColor color;
    private int timer;
    private double x;
    private double y;
    private double width;
    private double height;
    private static final double SCALE_FACTOR = 0.15;
    private Image red = new Image("file:src/main/resources/it/unibo/smartcrossroads/red.png");
    private Image green = new Image("file:src/main/resources/it/unibo/smartcrossroads/green.png");
    private Image yellow = new Image("file:src/main/resources/it/unibo/smartcrossroads/yellow.png");
    private RoadPosition position;

    public TrafficLight(boolean startGreen, double x, double y, RoadPosition position) {
        this.color = startGreen ? LightColor.GREEN : LightColor.RED;
        this.timer = 0;
        this.x = x;
        this.y = y;
        this.width = red.getWidth() * SCALE_FACTOR;
        this.height = red.getHeight() * SCALE_FACTOR;
        this.position = position;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public void updateLight() {
        timer++;
        switch (color) {
            case GREEN -> {
                if (timer >= 5) {
                    color = LightColor.YELLOW;
                    timer = 0;
                }
            }
            case YELLOW -> {
                if (timer >= 1) {
                    color = LightColor.RED;
                    timer = 0;
                }
            }
            case RED -> {
                if (timer >= 6) {
                    color = LightColor.GREEN;
                    timer = 0;
                }
            }
        }
    }

    public LightColor getColor() {
        return color;
    }

    public void draw(GraphicsContext gc) {
        gc.save();

        // Centro dell'immagine
        double centerX = x + width / 2;
        double centerY = y + height / 2;

        // Determina l'angolo di rotazione in base alla posizione
        double angle = switch (position) {
            case UP -> 0;
            case DOWN -> 180;
            case LEFT -> -90;
            case RIGHT -> 90;
        };

        // Trasla, ruota e poi disegna
        gc.translate(centerX, centerY);
        gc.rotate(angle);
        gc.drawImage(getImageByColor(), -width / 2, -height / 2, width, height);

        gc.restore();
    }

    // Metodo di utilità per ottenere l'immagine corretta
    private Image getImageByColor() {
        return switch (color) {
            case RED -> red;
            case GREEN -> green;
            case YELLOW -> yellow;
        };
    }

}
