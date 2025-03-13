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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import interfaces.TrafficListener;
import jason.infra.local.RunLocalMAS;
import model.Car;
import model.Tile;
import utils.*;

public class Launcher extends Application implements TrafficListener {

    private List<Car> cars;
    private List<Tile> tiles;

    private Stage primaryStage;
    private TextArea logArea;
    private TrafficEnvironment environment;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        Configuration.initializeSizes(screenWidth);

        Utils.initializeThings();
        Utils.loadCarImages();

        cars = Collections.synchronizedList(new ArrayList<>());
        tiles = Collections.synchronizedList(new ArrayList<>());
        initializeTiles();

        startJasonEnvironment();
    }

    private void initializeTiles() {
        for (int x = 0; x < 17; x++) {
            for (int y = 0; y < 13; y++) {
                tiles.add(new Tile(x, y));
            }
        }
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
                this.environment = environment;
                javafx.application.Platform.runLater(() -> {
                    environment.addTrafficListener(this);
                    setupStage();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void logMessage(String message) {
        javafx.application.Platform.runLater(() -> {
            logArea.appendText(message + "\n");
        });
    }

    public void setupStage() {
        Canvas canvas = new Canvas(Configuration.GRAPHIC_WIDTH, Configuration.APP_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawBackground(gc, Configuration.GRAPHIC_WIDTH, Configuration.APP_HEIGHT);
        drawIntersections(gc, Configuration.GRAPHIC_WIDTH, Configuration.APP_HEIGHT);
        drawDashedLines(gc, Configuration.GRAPHIC_WIDTH, Configuration.APP_HEIGHT);

        Timeline carMovementTimeline = new Timeline(
                new KeyFrame(Duration.millis(30),
                        _ -> updateGraphics(gc, Configuration.GRAPHIC_WIDTH, Configuration.APP_HEIGHT)));
        carMovementTimeline.setCycleCount(Timeline.INDEFINITE);
        carMovementTimeline.play();

        StackPane canvasContainer = new StackPane(canvas);

        VBox sidebar = new VBox();
        sidebar.setMinWidth(Configuration.SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: lightgray;");

        Label titleLabel = new Label("Monitoring");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(10, 0, 10, 0));
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.getChildren().add(titleLabel);

        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        VBox.setMargin(logArea, new Insets(10));
        VBox.setVgrow(logArea, javafx.scene.layout.Priority.ALWAYS);
        logArea.textProperty().addListener((_, _, _) -> logArea.setScrollTop(Double.MAX_VALUE));
        sidebar.getChildren().add(logArea);

        HBox root = new HBox(canvasContainer, sidebar);
        primaryStage.setScene(new Scene(root, Configuration.APP_WIDTH, Configuration.APP_HEIGHT));
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

        gc.fillRect(0, Configuration.TILE_SIZE * 3, WIDTH, Configuration.TILE_SIZE * 2);
        gc.fillRect(0, Configuration.TILE_SIZE * 8, WIDTH, Configuration.TILE_SIZE * 2);

        gc.fillRect(Configuration.TILE_SIZE * 5, 0, Configuration.TILE_SIZE * 2, HEIGHT);
        gc.fillRect(Configuration.TILE_SIZE * 10, 0, Configuration.TILE_SIZE * 2, HEIGHT);
    }

    private void drawDashedLines(GraphicsContext gc, int WIDTH, int HEIGHT) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        for (int x = 0; x < WIDTH; x += Constants.DASH_LENGTH + Constants.DASH_GAP) {
            if ((x + Constants.DASH_LENGTH) < Configuration.TILE_SIZE * 5
                    || (x > Configuration.TILE_SIZE * 7 && x < Configuration.TILE_SIZE * 10)
                    || x > Configuration.TILE_SIZE * 12) {
                gc.strokeLine(x, Configuration.TILE_SIZE * 4, x + Constants.DASH_LENGTH, Configuration.TILE_SIZE * 4);
                gc.strokeLine(x, Configuration.TILE_SIZE * 9, x + Constants.DASH_LENGTH, Configuration.TILE_SIZE * 9);
            }
        }

        for (int y = 0; y < HEIGHT; y += Constants.DASH_LENGTH + Constants.DASH_GAP) {
            if (y < Configuration.TILE_SIZE * 3
                    || (y > Configuration.TILE_SIZE * 5 && y < Configuration.TILE_SIZE * 8)
                    || y > Configuration.TILE_SIZE * 10) {
                gc.strokeLine(Configuration.TILE_SIZE * 6, y, Configuration.TILE_SIZE * 6, y + Constants.DASH_LENGTH);
                gc.strokeLine(Configuration.TILE_SIZE * 11, y, Configuration.TILE_SIZE * 11, y + Constants.DASH_LENGTH);
            }
        }
    }

    private void drawGrid(GraphicsContext gc) {
        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        for (int col = 0; col <= Configuration.GRID_COLS; col++) {
            int x = col * Configuration.TILE_SIZE;
            gc.strokeLine(x, 0, x, Configuration.APP_HEIGHT);
        }

        for (int row = 0; row <= Configuration.GRID_ROWS; row++) {
            int y = row * Configuration.TILE_SIZE;
            gc.strokeLine(0, y, Configuration.GRAPHIC_WIDTH, y);
        }
    }

    private void updateGraphics(GraphicsContext gc, int WIDTH, int HEIGHT) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        drawBackground(gc, WIDTH, HEIGHT);
        drawIntersections(gc, WIDTH, HEIGHT);
        drawDashedLines(gc, WIDTH, HEIGHT);
        drawGrid(gc);

        for (var tl : tiles) {
            tl.draw(gc);
        }

        synchronized (cars) {
            for (Car car : cars) {
                car.draw(gc);
            }
        }

    }

    @Override
    public void spawnCar(int carId, int posX, int posY) {
        int randomType = new Random().nextInt(3) + 1;
        cars.add(new Car(carId, randomType, posX, posY));
        logMessage("🚗 Auto #" + carId + " generata in (" + posX + ", " + posY + ")");
    }

    @Override
    public void moveCar(int carId, int posX, int posY, int dir) {
        for (var car : cars) {
            if (car.getId() == carId) {
                logMessage("🔄 Auto #" + carId + " si muove verso (" + posX + ", " + posY + ")");
                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(() -> {
                    boolean finished = car.move(posX, posY);
                    if (finished) {
                        environment.notifyAnimationFinished(carId, posX, posY, Direction.values()[dir]);
                        car.setPosition(posX, posY);
                        scheduler.shutdown();
                    }
                }, 0, 30, TimeUnit.MILLISECONDS);
                break;
            }
        }
    }

    @Override
    public void spawnTrafficLight(boolean isGreen, int trafficLightId, int posX, int posY) {
        for (var tile : tiles) {
            if (tile.getPosX() == posX && tile.getPosY() == posY) {
                tile.setTrafficLightId(trafficLightId);
                tile.setColor(isGreen ? LightColor.GREEN : LightColor.RED);
            }
        }
    }

    @Override
    public void updateTrafficLight(int trafficLightId, LightColor color) {
        for (var tile : tiles) {
            if (tile.getTrafficLightId() != -1 && tile.getTrafficLightId() == trafficLightId) {
                logMessage("🚦 Semaforo #" + trafficLightId + " cambiato a " + color);
                tile.setColor(color);
            }
        }
    }

    @Override
    public void removeCar(int carId) {
        cars.removeIf(car -> car.getId() == carId);
        logMessage("🔄 Auto #" + carId + " ha terminato il percorso");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
