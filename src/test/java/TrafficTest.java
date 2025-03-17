import org.junit.jupiter.api.Test;
import env.CarAgent;
import env.TrafficLightAgent;
import env.TrafficModel;
import env.TrafficModelImpl;
import jason.util.Pair;
import utils.Direction;
import utils.LightColor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

public class TrafficTest {
    private TrafficModel model;

    @BeforeEach
    public void setup() {
        model = new TrafficModelImpl();
    }

    @Test
    public void testInitialSize() {
        assertEquals(0, model.getAllAgents().size());
    }

    @Test
    public void testAddCTrafficLightAndChangeColor() {
        final String name = "traffic_light_1";
        model.insertAgent(name, new TrafficLightAgent(false, new Pair<>(4, 4), name));
        assertEquals(1, model.getAllAgents().size());
        TrafficLightAgent tl = (TrafficLightAgent) model.getAgent(name);
        assertFalse(tl.isGreen());
        model.updateTrafficLight(name, LightColor.GREEN);
        assertTrue(tl.isGreen());
    }

    @Test
    public void testAddCarAndMove() {
        final String name = "car_1";
        model.insertAgent(name, new CarAgent(new Pair<>(0, 4), name, Direction.WEST));
        assertEquals(1, model.getAllAgents().size());
        model.calculateTarget(name);
        final CarAgent car = (CarAgent) model.getAgent(name);
        Pair<Integer, Integer> target = car.getTarget();
        model.moveCar(name, target, Direction.WEST.ordinal());
        assertEquals(target, car.getPosition());
    }

}
