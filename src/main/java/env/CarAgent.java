package env;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import jason.asSyntax.Literal;
import jason.util.Pair;
import ui.Tile;
import utils.Direction;
import utils.Utils;

public class CarAgent extends TrafficAgent {
    private int x;
    private int y;
    private Integer targetX;
    private Integer targetY;
    private Direction direction;
    private String name;

    public CarAgent(int x, int y, String name, Direction direction) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.direction = direction;
        calculateTarget();
    }

    public void calculateTarget() {
        Tile tile = new Tile(x, y);

        var points = Utils.getDirections().get(new Pair<>(tile, this.direction));
        if (points != null && !points.isEmpty()) {
            int index = new Random().nextInt(points.size());
            var tileDir = points.get(index);
            var target = tileDir.getFirst();
            var newDir = tileDir.getSecond();
            setDirection(newDir);
            setTargetX(target.getPosX());
            setTargetY(target.getPosY());
        } else {
            setTargetX(-1);
            setTargetY(-1);
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getTargetX() {
        return this.targetX;
    }

    public int getTargetY() {
        return this.targetY;
    }

    public void setTargetX(Integer x) {
        this.targetX = x;
    }

    public void setTargetY(Integer y) {
        this.targetY = y;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Set<Literal> getPercepts() {
        final Set<Literal> set = new HashSet<>();
        set.add(Literal.parseLiteral(String.format("position(%d, %d)", x, y)));
        set.add(Literal.parseLiteral(String.format("name(%s)", name)));
        if (direction != null) {
            set.add(Literal.parseLiteral(String.format("direction(%d)", direction.ordinal())));
        }
        if (targetX != null && targetY != null) {
            set.add(Literal.parseLiteral(String.format("target(%d, %d)", targetX, targetY)));
        }

        return set;
    }
}
