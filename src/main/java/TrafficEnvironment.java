import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.infra.local.RunLocalMAS;
import model.CarModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import interfaces.TrafficListener;
import java.awt.Dimension;
import java.awt.Toolkit;

public class TrafficEnvironment extends Environment {

    private static final Random RAND = new Random();
    private RunLocalMAS mas;

    private List<CarModel> cars;
    private int counter = 1;

    private double height;
    private double width;

    private List<TrafficListener> listeners = new ArrayList<>();

    public void addTrafficListener(TrafficListener listener) {
        listeners.add(listener);
    }

    public void setMAS(RunLocalMAS mas) {
        this.mas = mas;
    }

    @Override
    public void init(final String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        height = screenSize.getHeight();
        width = screenSize.getWidth();
        cars = new ArrayList<>();
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        return Collections.singletonList(
                Literal.parseLiteral(String.format("start_creation(%s, %s)", height, width)));
    }

    @Override
    public boolean executeAction(final String ag, final Structure action) {
        String actionName = action.getFunctor();
        switch (actionName) {
            case "spawn_car":
                double Xs = 0;
                double Ys = 0;
                try {
                    Xs = ((NumberTerm) action.getTerm(0)).solve();
                    Ys = ((NumberTerm) action.getTerm(1)).solve();
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                System.out.println("Spawning car at: (" + Xs + ", " + Ys + ")");
                notifyCarSpawned("carName");
                return true;
            default:
                return false;
        }
    }

    private void notifyCarSpawned(String carId) {
        for (TrafficListener listener : listeners) {
            listener.spawnCar(carId);
        }
    }

    private void notifyCarRemoved(String carId) {
        for (TrafficListener listener : listeners) {
            listener.removeCar(carId);
        }
    }
}
