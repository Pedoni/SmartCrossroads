package interfaces;

import utils.LightColor;

public interface TrafficListener {
    void spawnTrafficLight(boolean isGreen, int trafficLightId, int initialX, int initialY);

    void updateTrafficLight(int trafficLightId, LightColor color);

    void spawnCar(int carId, int initialX, int initialY);

    void moveCar(int carId, int posX, int posY, int dir);

    void removeCar(int carId);
}
