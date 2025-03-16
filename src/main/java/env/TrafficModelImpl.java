package env;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jason.asSyntax.Literal;
import utils.Direction;
import utils.LightColor;

public class TrafficModelImpl implements TrafficModel {

    private final Map<String, TrafficAgent> agents = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void insertAgent(String name, TrafficAgent agemt) {
        agents.putIfAbsent(name, agemt);
    }

    @Override
    public void removeAgent(String name) {
        agents.remove(name);
    }

    @Override
    public boolean containsAgent(String name) {
        return agents.containsKey(name);
    }

    @Override
    public Set<String> getAllAgents() {
        return agents.keySet();
    }

    @Override
    public Set<Literal> getPercepts(String agent) {
        if (!agents.containsKey(agent)) {
            return Collections.emptySet();
        }
        return agents.get(agent).getPercepts();
    }

    @Override
    public void moveCar(String name, int x, int y, int dir) {
        Direction direction = Direction.values()[dir];
        CarAgent agent = (CarAgent) agents.get(name);
        agent.setX(x);
        agent.setY(y);
        agent.setDirection(direction);
        agent.calculateTarget();
    }

    @Override
    public void updateTrafficLight(String name, LightColor color) {
        TrafficLightAgent agent = (TrafficLightAgent) agents.get(name);
        agent.setGreen(color.equals(LightColor.GREEN));
    }

    @Override
    public void nextLight() {
        CreatorAgent agent = (CreatorAgent) agents.get("creator");
        agent.increaseTrafficLights();
    }

    @Override
    public void nextCar() {
        CreatorAgent agent = (CreatorAgent) agents.get("creator");
        agent.increaseCars();
    }

    @Override
    public void addTarget(String name, int posX, int posY) {
        CarAgent agent = (CarAgent) agents.get(name);
        agent.setTargetX(posX);
        agent.setTargetY(posY);
    }

    @Override
    public void removeTarget(String name) {
        System.out.println("Removing target");
        CarAgent agent = (CarAgent) agents.get(name);
        agent.setTargetX(null);
        agent.setTargetY(null);
    }

    @Override
    public void calculateTarget(String name) {
        CarAgent agent = (CarAgent) agents.get(name);
        agent.calculateTarget();
    }

}
