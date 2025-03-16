package env;

import java.util.HashSet;
import java.util.Set;

import jason.asSyntax.Literal;

public final class CreatorAgent extends TrafficAgent {

    private int cars;

    private int trafficLights;

    public CreatorAgent(int cars, int trafficLights) {
        this.cars = cars;
        this.trafficLights = trafficLights;
    }

    public void increaseTrafficLights() {
        this.trafficLights++;
    }

    public void increaseCars() {
        this.cars++;
    }

    @Override
    Set<Literal> getPercepts() {
        final Set<Literal> set = new HashSet<>();
        set.add(Literal.parseLiteral(String.format("cars(%d)", cars)));
        set.add(Literal.parseLiteral(String.format("lights(%s)", trafficLights)));

        return set;
    }
}
