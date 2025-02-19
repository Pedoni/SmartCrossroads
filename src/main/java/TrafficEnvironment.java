import jason.NoValueException;
import jason.architecture.AgArch;
import jason.asSemantics.Agent;
import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import model.CarModel;
import model.LinkedPoint;
import utils.LightColor;
import utils.Utils;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import interfaces.TrafficListener;

public class TrafficEnvironment extends Environment {
    private List<CarModel> cars;
    private TrafficListener listener = null;

    public void addTrafficListener(TrafficListener listener) {
        this.listener = listener;
    }

    public void notifyAnimationFinished(int carId) {
        Literal goal = Literal.parseLiteral("path");
        System.out.println("Time env notify: " + new Date());
        try {
            Agent agent = getEnvironmentInfraTier().getRuntimeServices().getAgentSnapshot("car_" + carId);
            agent.getTS().getC().addAchvGoal(goal, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public void clearAnimationFinished(int carId) {

    }

    @Override
    public void init(final String[] args) {
        cars = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        return Collections.singletonList(
                Literal.parseLiteral(String.format("start_creation")));
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
                System.out.println("[ENV] Inizio movecar " + new Date());
                try {
                    posX = (int) ((NumberTerm) action.getTerm(0)).solve();
                    posY = (int) ((NumberTerm) action.getTerm(1)).solve();
                    name = action.getTerm(2).toString();
                    counter = Integer.parseInt(name.substring(4));
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                System.out.println("[ENV] Pre notify " + new Date());
                notifyCarMoved(counter, posX, posY);
                System.out.println("[ENV] post notify " + new Date());
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

    private void addCar(int carId, int posX, int posY) {
        try {
            var startPoints = Utils.map.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().startsWith("s") && entry.getKey().endsWith("a"))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            System.out.println("POS X: " + posX);
            System.out.println("POS Y: " + posY);
            LinkedPoint point = startPoints.values().stream()
                    .filter(s -> s.getPosX() == posX && s.getPosY() == posY)
                    .findFirst()
                    .get();
            int randomType = new Random().nextInt(3) + 1;
            cars.add(new CarModel(carId, randomType, point));
        } catch (Exception e) {
            System.out.println("ERRORE ENV: " + e);
        }
    }

    private void notifyCarSpawned(int carId, int posX, int posY) {
        addCar(carId, posX, posY);
        if (this.listener != null) {
            listener.spawnCar(carId, posX, posY);
        }
    }

    private void notifyCarMoved(int carId, int posX, int posY) {
        if (this.listener != null) {
            listener.moveCar(carId, posX, posY);
        }
    }

}
