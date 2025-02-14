package interfaces;

public interface TrafficListener {
    void spawnTrafficLight(boolean isGreen, int trafficLightId, double initialX, double initialY);

    void spawnCar(int carId, double initialX, double initialY);

    void removeCar(int carId);
}
