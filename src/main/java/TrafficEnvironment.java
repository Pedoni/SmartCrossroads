import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.infra.local.RunLocalMAS;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class TrafficEnvironment extends Environment {

    private static final Random RAND = new Random();
    private RunLocalMAS mas;

    // action literals
    public static final Literal hotAir = Literal.parseLiteral("spray_air(hot)");
    public static final Literal coldAir = Literal.parseLiteral("spray_air(cold)");
    public static final Literal spawnCar = Literal.parseLiteral("spawn_car(1)");
    public static final Literal killCar = Literal.parseLiteral("kill_car(1)");

    private double temperature;
    private int carIndex;

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
        return true;
    }
}
