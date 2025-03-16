package env;

import java.util.HashSet;
import java.util.Set;

import jason.asSyntax.Literal;

public final class TrafficLightAgent extends TrafficAgent {
    private boolean isGreen;
    private int x;
    private int y;
    private String name;

    public TrafficLightAgent(boolean isGreen, int x, int y, String name) {
        this.isGreen = isGreen;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void setGreen(boolean isGreen) {
        this.isGreen = isGreen;
    }

    @Override
    Set<Literal> getPercepts() {
        final Set<Literal> set = new HashSet<>();
        set.add(Literal.parseLiteral(String.format("tl_position(%d, %d)", x, y)));
        set.add(Literal.parseLiteral(String.format("name(%s)", name)));
        set.add(Literal.parseLiteral(String.format("is_green(%s)", isGreen)));

        return set;
    }

}
