import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import utils.LightColor;
import utils.Utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import interfaces.TrafficListener;

public class TrafficEnvironment extends Environment {
    private TrafficListener listener = null;
    private Map<String, Set<Literal>> percepts = new HashMap<>();

    public void addTrafficListener(TrafficListener listener) {
        this.listener = listener;
    }

    public void addPercept(String agName, Literal percept) {
        percepts.computeIfAbsent(agName, _ -> new HashSet<>()).add(percept);
        informAgsEnvironmentChanged();
    }

    @Override
    public boolean removePercept(String agName, Literal percept) {
        if (percepts.containsKey(agName)) {
            Set<Literal> agentPercepts = percepts.get(agName);
            List<Literal> toRemove = agentPercepts.stream()
                    .filter(p -> p.getFunctor().equals(percept.getFunctor()))
                    .collect(Collectors.toList());
            boolean removed = agentPercepts.removeAll(toRemove);
            if (removed) {
                informAgsEnvironmentChanged();
            }
            return removed;
        }
        return false;
    }

    public void notifyAnimationFinished(int carId, int posX, int posY) {
        var point = Utils.map.values().stream()
                .filter(s -> s.getPosX() == posX && s.getPosY() == posY)
                .findFirst()
                .orElse(null);

        if (point == null)
            return;

        var points = point.getDestinations();
        Literal oldTarget = Literal.parseLiteral("target(_, _)");
        removePercept("car_" + carId, oldTarget);
        if (!points.isEmpty()) {
            int index = new Random().nextInt(points.size());
            var target = Utils.map.get(points.get(index));
            Literal targetBelief = Literal.parseLiteral(
                    String.format("target(%d, %d)", target.getPosX(), target.getPosY()));
            addPercept("car_" + carId, targetBelief);
        } else {
            Literal targetEnd = Literal.parseLiteral("target(-1, -1)");
            addPercept("car_" + carId, targetEnd);
        }
    }

    @Override
    public void init(final String[] args) {
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        return percepts.getOrDefault(agName, Collections.emptySet());
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
            case "spawn_car":
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
            case "spawn_traffic_light":
                try {
                    isGreen = action.getTerm(0).toString() == "true";
                    posX = (int) ((NumberTerm) action.getTerm(1)).solve();
                    posY = (int) ((NumberTerm) action.getTerm(2)).solve();
                    name = action.getTerm(3).toString();
                    counter = Integer.parseInt(name.substring(14));
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                notifyTrafficLightSpawned(isGreen, counter, posX, posY);
                return true;
            case "update_traffic_light":
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
            case "move_car":
                try {
                    posX = (int) ((NumberTerm) action.getTerm(0)).solve();
                    posY = (int) ((NumberTerm) action.getTerm(1)).solve();
                    name = action.getTerm(2).toString();
                    counter = Integer.parseInt(name.substring(4));
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                notifyCarMoved(counter, posX, posY);
                return true;
            case "remove_car":
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

    private void notifyCarMoved(int carId, int posX, int posY) {
        if (this.listener != null) {
            listener.moveCar(carId, posX, posY);
        }
    }

    private void notifyCarRemoved(int carId) {
        if (this.listener != null) {
            listener.removeCar(carId);
        }
    }

}
