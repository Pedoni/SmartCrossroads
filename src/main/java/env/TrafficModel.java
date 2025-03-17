package env;

import java.util.Map;
import java.util.Set;

import jason.asSyntax.Literal;
import jason.util.Pair;
import utils.LightColor;

//* Interface for the traffic model */
public interface TrafficModel {

    /** Returns all the agents */
    Map<String, TrafficAgent> getAllAgents();

    /** Inserts an agent in the general map of agents */
    void insertAgent(String name, TrafficAgent agent);

    /* Returns a traffic agent */
    TrafficAgent getAgent(String name);

    /** Removes an agent from the general map of agents */
    void removeAgent(String name);

    /** Returns all the percepts of a specific agent */
    Set<Literal> getPercepts(String agent);

    /** Moves a specific car agent */
    void moveCar(String name, Pair<Integer, Integer> position, int dire);

    /** Updates a specific traffic light */
    void updateTrafficLight(String name, LightColor color);

    /** Updates the traffic light counter of the creator agent */
    void nextLight();

    /** Updates the car counter of the creator agent */
    void nextCar();

    /** Calculates the next target for a specific car agent */
    void calculateTarget(String name);
}
