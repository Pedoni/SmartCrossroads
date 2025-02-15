
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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import interfaces.TrafficListener;
import jason.infra.local.RunLocalMAS;
import model.view_elements.Car;
import model.view_elements.TrafficLight;
import utils.*;

public class Launcher extends Application implements TrafficListener {

    private List<Car> cars;
    private List<TrafficLight> trafficLights;

    private Stage primaryStage;
    private int APP_WIDTH;
    private int APP_HEIGHT;
    private int GRAPHIC_WIDTH;
    private int SIDEBAR_WIDTH;
    private int TILE_SIZE;
    private int GRID_COLS;
    private int GRID_ROWS;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        double screenWidth = Screen.getPrimary().getBounds().getWidth();

        this.APP_HEIGHT = (int) (screenHeight * 3 / 4);
        this.APP_WIDTH = (int) (screenWidth * 3 / 4);
        this.GRAPHIC_WIDTH = (int) (APP_WIDTH * 3 / 4);
        this.TILE_SIZE = 50;
        this.GRID_COLS = GRAPHIC_WIDTH / TILE_SIZE;
        this.GRID_ROWS = APP_HEIGHT / TILE_SIZE;
        // this.GRAPHIC_WIDTH = TILE_SIZE * GRID_COLS;
        this.SIDEBAR_WIDTH = (int) (APP_WIDTH / 4);

        Utils.calculatePoints(GRAPHIC_WIDTH, APP_HEIGHT);
        Utils.loadCarImages();

        cars = Collections.synchronizedList(new ArrayList<>());
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
                TrafficEnvironment environment = (TrafficEnvironment) mas.getEnvironmentInfraTier()
                        .getUserEnvironment();
                javafx.application.Platform.runLater(() -> {
                    environment.addTrafficListener(this);
                    setupStage();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setupStage() {
        Canvas canvas = new Canvas(GRAPHIC_WIDTH, APP_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawBackground(gc, GRAPHIC_WIDTH, APP_HEIGHT);
        drawIntersections(gc, GRAPHIC_WIDTH, APP_HEIGHT);
        drawDashedLines(gc, GRAPHIC_WIDTH, APP_HEIGHT);

        Timeline carMovementTimeline = new Timeline(
                new KeyFrame(Duration.millis(30), _ -> updateGraphics(gc, GRAPHIC_WIDTH, APP_HEIGHT)));
        carMovementTimeline.setCycleCount(Timeline.INDEFINITE);
        carMovementTimeline.play();

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

        gc.fillRect(0, TILE_SIZE * 3, WIDTH, TILE_SIZE * 2);
        gc.fillRect(0, TILE_SIZE * 8, WIDTH, TILE_SIZE * 2);

        gc.fillRect(TILE_SIZE * 5, 0, TILE_SIZE * 2, HEIGHT);
        gc.fillRect(TILE_SIZE * 10, 0, TILE_SIZE * 2, HEIGHT);
    }

    private void drawDashedLines(GraphicsContext gc, int WIDTH, int HEIGHT) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        for (int x = 0; x < WIDTH; x += Constants.DASH_LENGTH + Constants.DASH_GAP) {
            if ((x + Constants.DASH_LENGTH) < TILE_SIZE * 5
                    || (x > TILE_SIZE * 7 && x < TILE_SIZE * 10)
                    || x > TILE_SIZE * 12) {
                gc.strokeLine(x, TILE_SIZE * 4, x + Constants.DASH_LENGTH, TILE_SIZE * 4);
                gc.strokeLine(x, TILE_SIZE * 9, x + Constants.DASH_LENGTH, TILE_SIZE * 9);
            }
        }

        for (int y = 0; y < HEIGHT; y += Constants.DASH_LENGTH + Constants.DASH_GAP) {
            if (y < TILE_SIZE * 3
                    || (y > TILE_SIZE * 5 && y < TILE_SIZE * 8)
                    || y > TILE_SIZE * 10) {
                gc.strokeLine(TILE_SIZE * 6, y, TILE_SIZE * 6, y + Constants.DASH_LENGTH);
                gc.strokeLine(TILE_SIZE * 11, y, TILE_SIZE * 11, y + Constants.DASH_LENGTH);
            }
        }
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        for (int col = 0; col <= GRID_COLS; col++) {
            int x = col * TILE_SIZE;
            gc.strokeLine(x, 0, x, APP_HEIGHT);
        }

        for (int row = 0; row <= GRID_ROWS; row++) {
            int y = row * TILE_SIZE;
            gc.strokeLine(0, y, GRAPHIC_WIDTH, y);
        }
    }

    private void updateGraphics(GraphicsContext gc, int WIDTH, int HEIGHT) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        drawBackground(gc, WIDTH, HEIGHT);
        drawIntersections(gc, WIDTH, HEIGHT);
        drawDashedLines(gc, WIDTH, HEIGHT);
        drawGrid(gc);

        synchronized (cars) {
            for (Car car : cars) {
                car.draw(gc);
            }
        }

        for (var tl : trafficLights) {
            tl.draw(gc);
        }

    }

    @Override
    public void spawnCar(int carId, double initialX, double initialY) {
        int randomType = new Random().nextInt(3) + 1;
        cars.add(new Car(carId, randomType, initialX, initialY));
    }

    @Override
    public void moveCar(int carId, double newX, double newY, double newAngle) {
        for (var car : cars) {
            if (car.getId() == carId) {
                car.move(newX, newY, newAngle);
            }
        }
    }

    @Override
    public void spawnTrafficLight(boolean isGreen, int trafficLightId, double x, double y) {
        var position = RoadPosition.values()[trafficLightId % 4];
        trafficLights.add(new TrafficLight(trafficLightId, isGreen, x, y, position));
    }

    @Override
    public void updateTrafficLight(int trafficLightId, LightColor color) {
        for (var tl : trafficLights) {
            if (tl.getId() == trafficLightId) {
                tl.setColor(color);
            }
        }
    }

    @Override
    public void removeCar(int carId) {
        cars.removeIf(car -> car.getId() == carId);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
