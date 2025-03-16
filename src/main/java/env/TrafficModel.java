package env;

import java.util.Set;

import jason.asSyntax.Literal;
import utils.LightColor;

public interface TrafficModel {

    void insertAgent(String name, TrafficAgent agent);

    void removeAgent(String name);

    boolean containsAgent(String name);

    Set<String> getAllAgents();

    Set<Literal> getPercepts(String agent);

    void moveCar(String name, int x, int y, int dire);

    void updateTrafficLight(String name, LightColor color);

    void nextLight();

    void nextCar();

    void addTarget(String name, int posX, int posY);

    void removeTarget(String name);

    void calculateTarget(String name);
}
