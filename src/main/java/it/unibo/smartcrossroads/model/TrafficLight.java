package it.unibo.smartcrossroads.model;

public class TrafficLight {
    public enum LightColor {
        RED, YELLOW, GREEN
    }

    private LightColor color;
    private int timer;
    private double x;
    private double y;

    public TrafficLight(boolean startGreen, double x, double y) {
        this.color = startGreen ? LightColor.GREEN : LightColor.RED;
        this.timer = 0;
        this.x = x;
        this.y = y;
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
                if (timer >= 5) { // Dopo 6 secondi, diventa giallo
                    color = LightColor.YELLOW;
                    timer = 0;
                }
            }
            case YELLOW -> {
                if (timer >= 1) { // Dopo 2 secondi, diventa rosso
                    color = LightColor.RED;
                    timer = 0;
                }
            }
            case RED -> {
                if (timer >= 6) { // Dopo 4 secondi, diventa verde
                    color = LightColor.GREEN;
                    timer = 0;
                }
            }
        }
    }

    public LightColor getColor() {
        return color;
    }
}
