package model;

import java.util.List;
import java.util.Map;

public class LinkedPoint {
    private final int posX;
    private final int posY;
    private final Map<String, List<String>> destinations;

    public LinkedPoint(int posX, int posY, Map<String, List<String>> destinations) {
        this.posX = posX;
        this.posY = posY;
        this.destinations = Map.copyOf(destinations);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Map<String, List<String>> getDestinations() {
        return destinations;
    }
}
