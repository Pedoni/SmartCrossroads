package interfaces;

import utils.LightColor;

public interface TrafficListener {
    void spawnTrafficLight(boolean isGreen, int trafficLightId, double initialX, double initialY);

    void updateTrafficLight(int trafficLightId, LightColor color);

    void spawnCar(int carId, double initialX, double initialY);

    void moveCar(int carId, double newX, double newY, double angle);

    void removeCar(int carId);
}
