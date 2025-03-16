package env;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jason.asSyntax.Literal;
import jason.util.Pair;
import utils.Direction;
import utils.LightColor;

public final class TrafficModelImpl implements TrafficModel {

    private final Map<String, TrafficAgent> agents = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Map<String, TrafficAgent> getAllAgents() {
        return agents;
    }

    @Override
    public void insertAgent(String name, TrafficAgent agent) {
        synchronized (agents) {
            agents.putIfAbsent(name, agent);
        }
    }

    @Override
    public TrafficAgent getAgent(String name) {
        synchronized (agents) {
            return agents.get(name);
        }
    }

    @Override
    public void removeAgent(String name) {
        synchronized (agents) {
            agents.remove(name);
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
    public void moveCar(String name, Pair<Integer, Integer> position, int dir) {
        synchronized (agents) {
            Direction direction = Direction.values()[dir];
            CarAgent agent = (CarAgent) agents.get(name);
            agent.setPosition(position);
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
    public void calculateTarget(String name) {
        synchronized (agents) {
            CarAgent agent = (CarAgent) agents.get(name);
            agent.calculateTarget();
        }
    }

}
