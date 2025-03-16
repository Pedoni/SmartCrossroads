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

    public boolean isGreen() {
        return isGreen;
    }

    public void setGreen(boolean isGreen) {
        this.isGreen = isGreen;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
