package env;

import java.util.HashSet;
import java.util.Set;

import jason.asSyntax.Literal;
import jason.util.Pair;

public final class TrafficLightAgent extends TrafficAgent {
    private boolean isGreen;
    private Pair<Integer, Integer> position;
    private String name;

    public TrafficLightAgent(boolean isGreen, Pair<Integer, Integer> position, String name) {
        this.isGreen = isGreen;
        this.position = position;
        this.name = name;
    }

    public void setGreen(boolean isGreen) {
        this.isGreen = isGreen;
    }

    @Override
    Set<Literal> getPercepts() {
        final Set<Literal> set = new HashSet<>();
        final int x = position.getFirst();
        final int y = position.getSecond();
        set.add(Literal.parseLiteral(String.format("tl_position(%d, %d)", x, y)));
        set.add(Literal.parseLiteral(String.format("name(%s)", name)));
        set.add(Literal.parseLiteral(String.format("is_green(%s)", isGreen)));

        return set;
    }

}
