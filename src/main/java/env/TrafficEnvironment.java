package env;

import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import utils.*;

import java.util.*;

import interfaces.TrafficListener;

public class TrafficEnvironment extends Environment {
    private TrafficListener view = null;
    private TrafficModel model;

    public void addTrafficListener(TrafficListener listener) {
        this.view = listener;
    }

    public void notifyAnimationFinished(int carId, int posX, int posY, Direction dir) {
        this.model.calculateTarget("car_" + carId);
        informAgsEnvironmentChanged();
    }

    @Override
    public void init(final String[] args) {
        model = new TrafficModelImpl();
        model.insertAgent("creator", new CreatorAgent(1, 0));
    }

    @Override
    public Set<Literal> getPercepts(String agName) {
        return this.model.getPercepts(agName);
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
            case Actions.NEXT_LIGHT:
                this.model.nextLight();
                return true;
            case Actions.NEXT_CAR:
                this.model.nextCar();
                return true;
            case Actions.SPAWN_CAR:
                int direction = 0;
                try {
                    posX = (int) ((NumberTerm) action.getTerm(0)).solve();
                    posY = (int) ((NumberTerm) action.getTerm(1)).solve();
                    name = action.getTerm(2).toString();
                    counter = Integer.parseInt(name.substring(4));
                    direction = (int) ((NumberTerm) action.getTerm(3)).solve();
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                this.model.insertAgent(name, new CarAgent(posX, posY, name, Direction.values()[direction]));
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
                this.model.insertAgent(name, new TrafficLightAgent(isGreen, posX, posY, name));
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
                this.model.updateTrafficLight(name, lightColor);
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
                this.model.moveCar(name, posX, posY, dire);
                notifyCarMoved(counter, posX, posY, dire);
                return true;
            case Actions.REMOVE_CAR:
                name = action.getTerm(0).toString();
                counter = Integer.parseInt(name.substring(4));
                this.model.removeAgent(name);
                notifyCarRemoved(counter);
                return true;
            default:
                return false;
        }
    }

    private void notifyTrafficLightSpawned(boolean isGreen, int trafficLightId, int initialX, int initialY) {
        if (this.view != null) {
            view.spawnTrafficLight(isGreen, trafficLightId, initialX, initialY);
        }
    }

    private void notifyTrafficLightUpdate(int trafficLightId, LightColor lightColor) {
        if (this.view != null) {
            view.updateTrafficLight(trafficLightId, lightColor);
        }
    }

    private void notifyCarSpawned(int carId, int posX, int posY) {
        if (this.view != null) {
            view.spawnCar(carId, posX, posY);
        }
    }

    private void notifyCarMoved(int carId, int posX, int posY, int dir) {
        if (this.view != null) {
            view.moveCar(carId, posX, posY, dir);
        }
    }

    private void notifyCarRemoved(int carId) {
        if (this.view != null) {
            view.removeCar(carId);
        }
    }

}
