package model;

import java.util.List;
import java.util.Random;

public class LinkedPoint {
    private final double x;
    private final double y;
    private final List<String> destinations;
    private static final Random RANDOM = new Random();

    public LinkedPoint(double x, double y, List<String> destinations) {
        this.x = x;
        this.y = y;
        this.destinations = List.copyOf(destinations); // Copia immutabile per sicurezza
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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
