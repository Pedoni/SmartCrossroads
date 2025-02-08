package it.unibo.smartcrossroads;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unibo.smartcrossroads.model.*;
import it.unibo.smartcrossroads.utils.*;

public class TrafficApplication extends Application {

    private List<Car> cars;

    @Override
    public void start(Stage primaryStage) {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();
        int APP_WIDTH = (int) (screenWidth * 3 / 4);
        int APP_HEIGHT = (int) (screenHeight * 2 / 3);
        int GRAPHIC_WIDTH = (int) (APP_WIDTH * 3 / 4);
        int SIDEBAR_WIDTH = (int) (APP_WIDTH / 4);

        Canvas canvas = new Canvas(GRAPHIC_WIDTH, APP_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawBackground(gc, GRAPHIC_WIDTH, APP_HEIGHT);
        drawIntersections(gc, GRAPHIC_WIDTH, APP_HEIGHT);
        drawDashedLines(gc, GRAPHIC_WIDTH, APP_HEIGHT);

        Utils.calculatePoints(APP_HEIGHT, GRAPHIC_WIDTH);

        List<Path> paths = Utils.getAllPaths();

        // Nel metodo start() di TrafficApplication:
        List<Point> path = new ArrayList<>();

        // Punto di partenza
        Point p0 = new Point(0, APP_HEIGHT * 2 / 3 + Constants.ROAD_WIDTH / 4 - 10);
        path.add(p0);
        Point p1 = new Point(GRAPHIC_WIDTH / 3 - Constants.ROAD_WIDTH / 2 - 10,
                APP_HEIGHT * 2 / 3 + Constants.ROAD_WIDTH / 4 - 10);
        path.add(p1);

        // Prosegui con gli altri segmenti (lineari oppure curvi)
        path.add(new Point(GRAPHIC_WIDTH / 3, APP_HEIGHT / 2));
        path.add(new Point(GRAPHIC_WIDTH / 2, APP_HEIGHT / 2));
        path.add(new Point(GRAPHIC_WIDTH / 2, 2 * APP_HEIGHT / 3));
        path.add(new Point(GRAPHIC_WIDTH, 2 * APP_HEIGHT / 3));

        cars = new LinkedList<>();
        cars.add(new Car(1, paths.get(0).points()));
        cars.add(new Car(2, paths.get(1).points()));

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(20), _ -> moveCar(gc, GRAPHIC_WIDTH, APP_HEIGHT)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

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
                new Stop(0, Color.DARKRED), new Stop(1, Color.DARKRED.darker()));
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

    private void moveCar(GraphicsContext gc, int WIDTH, int HEIGHT) {
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        drawBackground(gc, WIDTH, HEIGHT);
        drawIntersections(gc, WIDTH, HEIGHT);
        drawDashedLines(gc, WIDTH, HEIGHT);

        for (var car : cars) {
            car.move();
            car.draw(gc);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
