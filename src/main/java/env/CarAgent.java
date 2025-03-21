package env;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import jason.asSyntax.Literal;
import jason.util.Pair;
import ui.Tile;
import utils.Direction;
import utils.Utils;

public final class CarAgent extends TrafficAgent {
    private Pair<Integer, Integer> position;
    private Pair<Integer, Integer> target;
    private Direction direction;
    private final String name;
    private List<Pair<Integer, Integer>> trafficLights;

    public CarAgent(Pair<Integer, Integer> position, String name, Direction direction) {
        this.position = position;
        this.name = name;
        this.direction = direction;
        this.trafficLights = Arrays.asList(
            new Pair<>(5, 2),
            new Pair<>(6, 5),
            new Pair<>(4, 4),
            new Pair<>(7, 3),
            new Pair<>(10, 2),
            new Pair<>(11, 5),
            new Pair<>(9, 4),
            new Pair<>(12, 3),
            new Pair<>(10, 7),
            new Pair<>(11, 10),
            new Pair<>(9, 9),
            new Pair<>(12, 8),
            new Pair<>(5, 7),
            new Pair<>(6, 10),
            new Pair<>(4, 9),
            new Pair<>(7, 8)
        );
        calculateTarget();
    }

    public void calculateTarget() {
        final int x = position.getFirst();
        final int y = position.getSecond();
        Tile tile = new Tile(x, y);

        var points = Utils.getDirections().get(new Pair<>(tile, this.direction));
        if (points != null && !points.isEmpty()) {
            int index = new Random().nextInt(points.size());
            var tileDir = points.get(index);
            var target = tileDir.getFirst();
            var newDir = tileDir.getSecond();
            setDirection(newDir);
            setTarget(new Pair<>(target.getPosX(), target.getPosY()));
        } else {
            setTarget(new Pair<>(-1, -1));
        }
    }

    public Pair<Integer, Integer> getPosition() {
        return position;
    }

    public void setPosition(Pair<Integer, Integer> position) {
        this.position = position;
    }

    public Pair<Integer, Integer> getTarget() {
        return target;
    }

    public void setTarget(Pair<Integer, Integer> target) {
        this.target = target;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Set<Literal> getPercepts() {
        final Set<Literal> set = new HashSet<>();
        final int x = position.getFirst();
        final int y = position.getSecond();
        set.add(Literal.parseLiteral(String.format("position(%d, %d)", x, y)));
        set.add(Literal.parseLiteral(String.format("name(%s)", name)));
        if (direction != null) {
            set.add(Literal.parseLiteral(String.format("direction(%d)", direction.ordinal())));
        }
        if (target != null) {
            final int targetX = target.getFirst();
            final int targetY = target.getSecond();
            set.add(Literal.parseLiteral(String.format("target(%d, %d)", targetX, targetY)));
        }

        for (int i = 0; i < trafficLights.size(); i++) {
            var tl = trafficLights.get(i);
            set.add(Literal.parseLiteral(String.format("tl(%d, %d, %d)", i, tl.getFirst(), tl.getSecond())));
        }

        return set;
    }
}
