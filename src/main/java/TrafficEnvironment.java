import jason.NoValueException;
import jason.asSyntax.Literal;
import jason.asSyntax.NumberTerm;
import jason.asSyntax.StringTerm;
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

    private int height;
    private int width;

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
        height = (int) screenSize.getHeight() * 3 / 4;
        var totalWidth = (int) screenSize.getWidth() * 3 / 4;
        width = (int) totalWidth * 3 / 4;
        cars = new ArrayList<>();
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        return Collections.singletonList(
                Literal.parseLiteral(String.format("start_creation(%s, %s)", width, height)));
    }

    @Override
    public boolean executeAction(final String ag, final Structure action) {
        String actionName = action.getFunctor();
        var array = action.getTermsArray();

        switch (actionName) {
            case "spawn_car":
                double Xs = 0;
                double Ys = 0;
                int counter = 0;
                try {
                    Xs = ((NumberTerm) action.getTerm(0)).solve();
                    Ys = ((NumberTerm) action.getTerm(1)).solve();
                    String name = action.getTerm(2).toString();
                    System.out.println(name);
                    counter = Integer.parseInt(name.substring(4));
                } catch (NoValueException e) {
                    e.printStackTrace();
                }
                System.out.println("[ENV] Spawning car " + counter + " at: (" + Xs + ", " + Ys + ")");
                notifyCarSpawned(counter, Xs, Ys);
                return true;
            default:
                return false;
        }
    }

    private void notifyCarSpawned(int carId, double initialX, double initialY) {
        for (TrafficListener listener : listeners) {
            listener.spawnCar(carId, initialX, initialY);
        }
    }

    private void notifyCarRemoved(int carId) {
        for (TrafficListener listener : listeners) {
            listener.removeCar(carId);
        }
    }
}
