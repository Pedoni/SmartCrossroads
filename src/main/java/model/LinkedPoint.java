package model;

import java.util.List;
import java.util.Random;

public class LinkedPoint {
    private final int posX;
    private final int posY;
    private final List<String> destinations;
    private static final Random RANDOM = new Random();

    public LinkedPoint(int posX, int posY, List<String> destinations) {
        this.posX = posX;
        this.posY = posY;
        this.destinations = List.copyOf(destinations);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public String getDestination(int index) {
        if (index < 0 || index >= destinations.size()) {
            throw new IndexOutOfBoundsException("Index fuori dai limiti");
        }
        return destinations.get(index);
    }

    public String getRandomDestination() {
        if (destinations.isEmpty()) {
            throw new IllegalStateException("Nessuna destinazione disponibile");
        }
        return destinations.get(RANDOM.nextInt(destinations.size()));
    }
}
