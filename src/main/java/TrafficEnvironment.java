import jason.NoValueException;
import jason.asSemantics.Agent;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import utils.LightColor;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import interfaces.TrafficListener;

public class TrafficEnvironment extends Environment {
    private TrafficListener listener = null;

    public void addTrafficListener(TrafficListener listener) {
        this.listener = listener;
    }

    public void notifyAnimationFinished(int carId) {
        Literal goal = Literal.parseLiteral("path(car_" + carId + ")");
        try {
            Agent agent = getEnvironmentInfraTier().getRuntimeServices().getAgentSnapshot("car_" + carId);
            agent.getTS().getC().addAchvGoal(goal, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(final String[] args) {
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        return Collections.emptyList();
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
