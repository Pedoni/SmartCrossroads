package env;

import java.util.Set;

import jason.asSyntax.Literal;
import utils.LightColor;

public interface TrafficModel {

    /** inserts an agent in the general map of agents */
    void insertAgent(String name, TrafficAgent agent);

    /** removes an agent from the general map of agents */
    void removeAgent(String name);

    /** get all the percepts of a specific agent */
    Set<Literal> getPercepts(String agent);

    /** moves a specific car agent */
    void moveCar(String name, int x, int y, int dire);

    /** updates a specific traffic light */
    void updateTrafficLight(String name, LightColor color);

    /** updates the traffic light counter of the creator agent */
    void nextLight();

    /** updates the car counter of the creator agent */
    void nextCar();

    /** calculates the next target for a specific car agent */
    void calculateTarget(String name);
}
