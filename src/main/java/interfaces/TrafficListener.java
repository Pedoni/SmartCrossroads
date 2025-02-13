package interfaces;

public interface TrafficListener {
    void spawnCar(int carId, double initialX, double initialY);

    void removeCar(int carId);
}
