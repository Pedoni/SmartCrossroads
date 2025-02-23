import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.util.Pair;
import model.view_elements.Tile;
import utils.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import interfaces.TrafficListener;

public class TrafficEnvironment extends Environment {
    private TrafficListener listener = null;
    private Map<String, Set<Literal>> percepts = new ConcurrentHashMap<>();

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

    public void notifyAnimationFinished(int carId, int posX, int posY, Direction dir) {
        Tile tile = new Tile(posX, posY);

        var points = Utils.getDirections().get(new Pair<>(tile, dir));
        if (points.size() > 0) {
            int index = new Random().nextInt(points.size());
            var tileDir = points.get(index);
            var target = tileDir.getFirst();
            var newDir = tileDir.getSecond();

            Literal oldDirection = Literal.parseLiteral("direction(_)");
            removePercept("car_" + carId, oldDirection);

            Literal directionBelief = Literal.parseLiteral(
                    String.format("direction(%d)", newDir.ordinal()));
            addPercept("car_" + carId, directionBelief);

            Literal oldTarget = Literal.parseLiteral("target(_, _)");
            removePercept("car_" + carId, oldTarget);

            Literal targetBelief = Literal.parseLiteral(
                    String.format("target(%d, %d)", target.getPosX(), target.getPosY()));
            addPercept("car_" + carId, targetBelief);
        } else {
            Literal oldDirection = Literal.parseLiteral("direction(_)");
            removePercept("car_" + carId, oldDirection);
            Literal oldTarget = Literal.parseLiteral("target(_, _)");
            removePercept("car_" + carId, oldTarget);
            addPercept("car_" + carId, Literal.parseLiteral("target(-1, -1)"));
        }
    }

    @Override
    public void init(final String[] args) {
    }

    @Override
    public Set<Literal> getPercepts(String agName) {
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
                Direction dir = null;
                if (posX == 0) {
                    dir = Direction.WEST;
                }
                if (posX == Constants.COLUMNS - 1) {
                    dir = Direction.EAST;
                }
                if (posY == 0) {
                    dir = Direction.NORTH;
                }
                if (posY == Constants.ROWS - 1) {
                    dir = Direction.SOUTH;
                }
                addPercept(name, Literal.parseLiteral(String.format("direction(%d)", dir.ordinal())));
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
