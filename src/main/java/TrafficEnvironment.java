import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.infra.local.RunLocalMAS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import interfaces.TrafficListener;

public class TrafficEnvironment extends Environment {

    private static final Random RAND = new Random();
    private RunLocalMAS mas;

    // action literals
    public static final Literal spawnCar = Literal.parseLiteral("spawn_car(1)");
    public static final Literal killCar = Literal.parseLiteral("kill_car(1)");

    private List<TrafficListener> listeners = new ArrayList<>();

    private double temperature;
    private int carIndex;

    public void addTrafficListener(TrafficListener listener) {
        listeners.add(listener);
    }

    public void setMAS(RunLocalMAS mas) {
        this.mas = mas;
    }

    @Override
    public void init(final String[] args) {
        carIndex = 1;
        if (args.length >= 1) {
            temperature = Double.parseDouble(args[0]);
        } else {
            temperature = RAND.nextDouble() * 20 + 10;
        }
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        return Collections.singletonList(
                Literal.parseLiteral(String.format("temperature(%s)", temperature)));
    }

    private static final double FAILURE_PROBABILITY = 0.2;

    @Override
    public boolean executeAction(final String ag, final Structure action) {
        String actionName = action.getFunctor();
        System.out.println(actionName);
        if ("spawn_car".equals(actionName)) {
            String carName = action.getTerm(0).toString();
            notifyCarSpawned(carName);
            return true;
        }
        return true;
    }

    private void notifyCarSpawned(String carId) {
        for (TrafficListener listener : listeners) {
            listener.onCarSpawned(carId);
        }
    }

    private void notifyCarRemoved(String carId) {
        for (TrafficListener listener : listeners) {
            listener.onCarRemoved(carId);
        }
    }
}
