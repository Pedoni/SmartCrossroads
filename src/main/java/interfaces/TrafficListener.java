package interfaces;

public interface TrafficListener {
    void onCarSpawned(String carId);

    void onCarRemoved(String carId);
}
