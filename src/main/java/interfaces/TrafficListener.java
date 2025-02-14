package interfaces;

import utils.LightColor;

public interface TrafficListener {
    void spawnTrafficLight(boolean isGreen, int trafficLightId, double initialX, double initialY);

    void updateTrafficLight(int trafficLightId, LightColor color);

    void spawnCar(int carId, double initialX, double initialY);

    void removeCar(int carId);
}
