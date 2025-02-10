import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.stdlib.print;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class TrafficEnvironment extends Environment {

    private static final Random RAND = new Random();
    private TrafficApplication application;

    // action literals
    public static final Literal hotAir = Literal.parseLiteral("spray_air(hot)");
    public static final Literal coldAir = Literal.parseLiteral("spray_air(cold)");

    private double temperature;

    public TrafficEnvironment(TrafficApplication application) {
        this.application = application;
    }

    @Override
    public void init(final String[] args) {
        application = new TrafficApplication();

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
        boolean result = true;
        if (RAND.nextDouble() < FAILURE_PROBABILITY) {
            result = false;
        } else if (action.equals(hotAir)) {
            temperature += 0.1;
        } else if (action.equals(coldAir)) {
            temperature -= 0.1;
        } else {
            RuntimeException e = new IllegalArgumentException("Cannot handle action: " + action);
            throw e;
        }
        try {
            Thread.sleep(500L); // Slowdown the system
        } catch (InterruptedException ignored) {
        }
        return result;
    }
}
