package env;

import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import utils.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import interfaces.TrafficListener;

public class TrafficEnvironment extends Environment {
    private TrafficListener listener = null;
    private final Map<String, Set<Literal>> percepts = new ConcurrentHashMap<>();

    public void addTrafficListener(TrafficListener listener) {
        this.listener = listener;
    }

    public void addPercept(String agName, Literal percept) {
        percepts.computeIfAbsent(agName, _ -> ConcurrentHashMap.newKeySet()).add(percept);
        informAgsEnvironmentChanged();
    }

    @Override
    public boolean removePercept(String agName, Literal percept) {
        if (percepts.containsKey(agName)) {
            Set<Literal> agentPercepts = percepts.get(agName);
            synchronized (agentPercepts) {
                Iterator<Literal> iter = agentPercepts.iterator();
                boolean removed = false;
                while (iter.hasNext()) {
                    Literal p = iter.next();
                    if (p.getFunctor().equals(percept.getFunctor())) {
                        iter.remove();
                        removed = true;
                    }
                }
                if (removed) {
                    informAgsEnvironmentChanged();
                }
                return removed;
            }
        }
        return false;
    }

    public void notifyAnimationFinished(int carId, int posX, int posY, Direction dir) {
        Literal findTarget = Literal.parseLiteral(String.format("find_target(%d, %d, %d)", posX, posY, dir.ordinal()));
        removePercept("car_" + carId, findTarget);
        addPercept("car_" + carId, findTarget);
    }

    @Override
    public void init(final String[] args) {
    }

    @Override
    public Set<Literal> getPercepts(String agName) {
        return new HashSet<>(percepts.getOrDefault(agName, Collections.emptySet()));
    }

    @Override
    public boolean executeAction(final String ag, final Structure action) {
        String actionName = action.getFunctor();
        boolean isGreen = false;
        int posX = 0;
        int posY = 0;
        int counter = 0;
        String color = "";
        String name = "";
        switch (actionName) {
            case Actions.SPAWN_CAR:
                try {
                    posX = (int) ((NumberTerm) action.getTerm(0)).solve();
                    posY = (int) ((NumberTerm) action.getTerm(1)).solve();
                    name = action.getTerm(2).toString();
                    counter = Integer.parseInt(name.substring(4));
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                notifyCarSpawned(counter, posX, posY);
                return true;
            case Actions.SPAWN_TRAFFIC_LIGHT:
                try {
                    isGreen = Objects.equals(action.getTerm(0).toString(), "true");
                    posX = (int) ((NumberTerm) action.getTerm(1)).solve();
                    posY = (int) ((NumberTerm) action.getTerm(2)).solve();
                    name = action.getTerm(3).toString();
                    counter = Integer.parseInt(name.substring(14));
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                notifyTrafficLightSpawned(isGreen, counter, posX, posY);
                return true;
            case Actions.UPDATE_TRAFFIC_LIGHT:
                color = action.getTerm(0).toString();
                LightColor lightColor = switch (color) {
                    case "green" -> LightColor.GREEN;
                    case "red" -> LightColor.RED;
                    default -> LightColor.YELLOW;
                };
                name = action.getTerm(1).toString();
                counter = Integer.parseInt(name.substring(14));
                notifyTrafficLightUpdate(counter, lightColor);
                return true;
            case Actions.MOVE_CAR:
                int dire = 0;
                try {
                    posX = (int) ((NumberTerm) action.getTerm(0)).solve();
                    posY = (int) ((NumberTerm) action.getTerm(1)).solve();
                    name = action.getTerm(2).toString();
                    dire = (int) ((NumberTerm) action.getTerm(3)).solve();
                    counter = Integer.parseInt(name.substring(4));
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                notifyCarMoved(counter, posX, posY, dire);
                return true;
            case Actions.REMOVE_CAR:
                name = action.getTerm(0).toString();
                counter = Integer.parseInt(name.substring(4));
                notifyCarRemoved(counter);
                return true;
            default:
                return false;
        }
    }

    private void notifyTrafficLightSpawned(boolean isGreen, int trafficLightId, int initialX, int initialY) {
        if (this.listener != null) {
            listener.spawnTrafficLight(isGreen, trafficLightId, initialX, initialY);
        }
    }

    private void notifyTrafficLightUpdate(int trafficLightId, LightColor lightColor) {
        if (this.listener != null) {
            listener.updateTrafficLight(trafficLightId, lightColor);
        }
    }

    private void notifyCarSpawned(int carId, int posX, int posY) {
        if (this.listener != null) {
            listener.spawnCar(carId, posX, posY);
        }
    }

    private void notifyCarMoved(int carId, int posX, int posY, int dir) {
        if (this.listener != null) {
            listener.moveCar(carId, posX, posY, dir);
        }
    }

    private void notifyCarRemoved(int carId) {
        if (this.listener != null) {
            listener.removeCar(carId);
        }
    }

}
