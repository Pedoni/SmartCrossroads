package env;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jason.asSyntax.Literal;
import jason.util.Pair;
import utils.Direction;
import utils.LightColor;

public class TrafficModelImpl implements TrafficModel {

    private final Map<String, TrafficAgent> agents = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void insertAgent(String name, TrafficAgent agent) {
        synchronized (agents) {
            agents.putIfAbsent(name, agent);
        }
    }

    @Override
    public void removeAgent(String name) {
        synchronized (agents) {
            agents.remove(name);
        }
    }

    @Override
    public boolean containsAgent(String name) {
        synchronized (agents) {
            return agents.containsKey(name);
        }
    }

    @Override
    public Set<String> getAllAgents() {
        synchronized (agents) {
            return agents.keySet();
        }
    }

    @Override
    public Set<Literal> getPercepts(String agent) {
        synchronized (agents) {
            if (!agents.containsKey(agent)) {
                return Collections.emptySet();
            }
            return agents.get(agent).getPercepts();
        }
    }

    @Override
    public void moveCar(String name, int x, int y, int dir) {
        synchronized (agents) {
            Direction direction = Direction.values()[dir];
            CarAgent agent = (CarAgent) agents.get(name);
            agent.setPosition(new Pair<>(x, y));
            agent.setDirection(direction);
        }
    }

    @Override
    public void updateTrafficLight(String name, LightColor color) {
        synchronized (agents) {
            TrafficLightAgent agent = (TrafficLightAgent) agents.get(name);
            agent.setGreen(color.equals(LightColor.GREEN));
        }
    }

    @Override
    public void nextLight() {
        synchronized (agents) {
            CreatorAgent agent = (CreatorAgent) agents.get("creator");
            agent.increaseTrafficLights();
        }
    }

    @Override
    public void nextCar() {
        synchronized (agents) {
            CreatorAgent agent = (CreatorAgent) agents.get("creator");
            agent.increaseCars();
        }
    }

    @Override
    public void addTarget(String name, int posX, int posY) {
        synchronized (agents) {
            CarAgent agent = (CarAgent) agents.get(name);
            agent.setTarget(new Pair<>(posX, posY));
        }
    }

    @Override
    public void removeTarget(String name) {
        synchronized (agents) {
            CarAgent agent = (CarAgent) agents.get(name);
            agent.setTarget(null);
        }
    }

    @Override
    public void calculateTarget(String name) {
        synchronized (agents) {
            CarAgent agent = (CarAgent) agents.get(name);
            agent.calculateTarget();
        }
    }

}
