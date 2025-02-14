
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import interfaces.TrafficListener;
import jason.infra.local.RunLocalMAS;
import model.*;
import model.TrafficLight.RoadPosition;
import utils.*;

public class Launcher extends Application implements TrafficListener {

    private List<CarModel> cars;
    private List<TrafficLight> trafficLights;

    private TrafficEnvironment environment;
    private Stage primaryStage;
    private int APP_WIDTH;
    private int APP_HEIGHT;
    private int GRAPHIC_WIDTH;
    private int SIDEBAR_WIDTH;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();

        this.APP_HEIGHT = (int) (screenHeight * 3 / 4);
        this.APP_WIDTH = (int) (screenWidth * 3 / 4);
        this.GRAPHIC_WIDTH = (int) (APP_WIDTH * 3 / 4);
        this.SIDEBAR_WIDTH = (int) (APP_WIDTH / 4);

        Utils.calculatePoints(GRAPHIC_WIDTH, APP_HEIGHT);

        cars = new ArrayList<>();
        trafficLights = new ArrayList<>();

        startJasonEnvironment();
    }

    private void startJasonEnvironment() {
        new Thread(() -> {
            try {
                File file = new File("src/main/crossroads.mas2j");
                RunLocalMAS mas = new RunLocalMAS();
                mas.init(new String[] { file.getAbsolutePath() });
                mas.create();
                mas.start();
                environment = (TrafficEnvironment) mas.getEnvironmentInfraTier().getUserEnvironment();
                environment.setMAS(mas);
                // Start JavaFX UI setup after environment is ready
                javafx.application.Platform.runLater(() -> setupStage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setupStage() {
        environment.addTrafficListener(this);

        Canvas canvas = new Canvas(GRAPHIC_WIDTH, APP_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawBackground(gc, GRAPHIC_WIDTH, APP_HEIGHT);
        drawIntersections(gc, GRAPHIC_WIDTH, APP_HEIGHT);
        drawDashedLines(gc, GRAPHIC_WIDTH, APP_HEIGHT);

        Timeline carMovementTimeline = new Timeline(
                new KeyFrame(Duration.millis(30), _ -> updateGraphics(gc, GRAPHIC_WIDTH, APP_HEIGHT)));
        carMovementTimeline.setCycleCount(Timeline.INDEFINITE);
        carMovementTimeline.play();

        Timeline trafficLightTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), _ -> updateTrafficLights()));
        trafficLightTimeline.setCycleCount(Timeline.INDEFINITE);
        trafficLightTimeline.play();

        StackPane canvasContainer = new StackPane(canvas);

        VBox sidebar = new VBox();
        sidebar.setMinWidth(SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: lightgray;");

        Label titleLabel = new Label("Monitoring");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(10, 0, 10, 0));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.getChildren().add(titleLabel);

        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        VBox.setMargin(logArea, new Insets(10));
        VBox.setVgrow(logArea, javafx.scene.layout.Priority.ALWAYS);
        sidebar.getChildren().add(logArea);

        HBox root = new HBox(canvasContainer, sidebar);
        primaryStage.setScene(new Scene(root, APP_WIDTH, APP_HEIGHT));
        primaryStage.setTitle("Smart Crossroads");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void drawBackground(GraphicsContext gc, int WIDTH, int HEIGHT) {
        LinearGradient gradient = new LinearGradient(0, 0, 0, HEIGHT, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.DARKRED.darker().darker()), new Stop(1, Color.DARKRED.darker().darker()));
        gc.setFill(gradient);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawIntersections(GraphicsContext gc, int WIDTH, int HEIGHT) {
        LinearGradient roadGradient = new LinearGradient(0, 0, 0, Constants.ROAD_WIDTH, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GRAY), new Stop(1, Color.DARKGRAY));
        gc.setFill(roadGradient);

        gc.fillRect(0, HEIGHT / 3 - Constants.ROAD_WIDTH / 2, WIDTH, Constants.ROAD_WIDTH);
        gc.fillRect(0, 2 * HEIGHT / 3 - Constants.ROAD_WIDTH / 2, WIDTH, Constants.ROAD_WIDTH);

        gc.fillRect(WIDTH / 3 - Constants.ROAD_WIDTH / 2, 0, Constants.ROAD_WIDTH, HEIGHT);
        gc.fillRect(2 * WIDTH / 3 - Constants.ROAD_WIDTH / 2, 0, Constants.ROAD_WIDTH, HEIGHT);
    }

    private void drawDashedLines(GraphicsContext gc, int WIDTH, int HEIGHT) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        for (int x = 0; x < WIDTH; x += Constants.DASH_LENGTH + Constants.DASH_GAP) {
            if ((x + Constants.DASH_LENGTH) < WIDTH / 3 - Constants.ROAD_WIDTH / 2
                    || (x > WIDTH / 3 + Constants.ROAD_WIDTH / 2
                            && x + Constants.DASH_LENGTH < 2 * WIDTH / 3 - Constants.ROAD_WIDTH / 2)
                    || x > 2 * WIDTH / 3 + Constants.ROAD_WIDTH / 2) {
                gc.strokeLine(x, HEIGHT / 3, x + Constants.DASH_LENGTH, HEIGHT / 3);
                gc.strokeLine(x, 2 * HEIGHT / 3, x + Constants.DASH_LENGTH, 2 * HEIGHT / 3);
            }
        }

        for (int y = 0; y < HEIGHT; y += Constants.DASH_LENGTH + Constants.DASH_GAP) {
            if ((y + Constants.DASH_LENGTH) < HEIGHT / 3 - Constants.ROAD_WIDTH / 2
                    || (y > HEIGHT / 3 + Constants.ROAD_WIDTH / 2
                            && y + Constants.DASH_LENGTH < 2 * HEIGHT / 3 - Constants.ROAD_WIDTH / 2)
                    || y > 2 * HEIGHT / 3 + Constants.ROAD_WIDTH / 2) {
                gc.strokeLine(WIDTH / 3, y, WIDTH / 3, y + Constants.DASH_LENGTH);
                gc.strokeLine(2 * WIDTH / 3, y, 2 * WIDTH / 3, y + Constants.DASH_LENGTH);
            }
        }
    }

    private void updateGraphics(GraphicsContext gc, int WIDTH, int HEIGHT) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        drawBackground(gc, WIDTH, HEIGHT);
        drawIntersections(gc, WIDTH, HEIGHT);
        drawDashedLines(gc, WIDTH, HEIGHT);

        cars.removeIf(car -> car.hasReachedFinalDestination());

        for (var car : cars) {
            car.move();
            car.draw(gc);
        }

        for (var tl : trafficLights) {
            tl.draw(gc);
        }

        // drawTrafficLights(gc, WIDTH, HEIGHT);
    }

    private void updateTrafficLights() {
        for (TrafficLight light : trafficLights) {
            light.updateLight();
        }
    }

    @Override
    public void spawnCar(int carId, double initialX, double initialY) {
        var startPoints = Utils.map.entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith("s") && entry.getKey().endsWith("a"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        LinkedPoint point = startPoints.values().stream().filter(s -> s.getX() == initialX && s.getY() == initialY)
                .findFirst()
                .get();
        int randomType = new Random().nextInt(3) + 1;
        cars.add(new CarModel(randomType, point));
    }

    @Override
    public void spawnTrafficLight(int trafficLightId, double x, double y) {
        var position = RoadPosition.values()[trafficLightId % 4];
        trafficLights.add(new TrafficLight(true, x, y, position));
    }

    @Override
    public void removeCar(int carId) {
        // TO IMPLEMENT
        System.out.println("CAR REMOVED IN UI");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
