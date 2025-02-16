import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.infra.local.RunLocalMAS;
import model.CarModel;
import model.LinkedPoint;
import utils.LightColor;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import interfaces.TrafficListener;
import java.awt.Dimension;
import java.awt.Toolkit;

public class TrafficEnvironment extends Environment {
    private List<CarModel> cars;
    private TrafficListener listener = null;
    private int height;
    private int width;

    public void addTrafficListener(TrafficListener listener) {
        this.listener = listener;
        moveCars();
    }

    @Override
    public void init(final String[] args) {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        height = (int) screenSize.getHeight() * 3 / 4;
        var totalWidth = (int) screenSize.getWidth() * 3 / 4;
        width = (int) totalWidth * 3 / 4;
        cars = Collections.synchronizedList(new ArrayList<>());
    }

    private void moveCars() {
        new Thread(() -> {
            while (true) {
                try {
                    synchronized (cars) {
                        Iterator<CarModel> iterator = cars.iterator();
                        while (iterator.hasNext()) {
                            CarModel c = iterator.next();
                            if (c.hasReachedFinalDestination()) {
                                iterator.remove();
                                if (this.listener != null) {
                                    listener.removeCar(c.getId());
                                }
                            } else {
                                c.move();
                                if (this.listener != null) {
                                    listener.moveCar(c.getId(), c.getX(), c.getY(), c.getAngle());
                                }
                            }
                        }
                    }
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();

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
        switch (actionName) {
            case "spawn_car":
                try {
                    posX = (int) ((NumberTerm) action.getTerm(0)).solve();
                    posY = (int) ((NumberTerm) action.getTerm(1)).solve();
                    String name = action.getTerm(2).toString();
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
                    String name = action.getTerm(3).toString();
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
                String name = action.getTerm(1).toString();
                counter = Integer.parseInt(name.substring(14));
                notifyTrafficLightUpdate(counter, lightColor);
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

}
