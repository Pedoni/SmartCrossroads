package it.unibo.smartcrossroads;

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

public class TrafficApplication extends Application {
    private static final int ROAD_WIDTH = 100;
    private static final int DASH_LENGTH = 10;
    private static final int DASH_GAP = 10;

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

        StackPane canvasContainer = new StackPane(canvas);

        // Create the sidebar with a title and a logging zone
        VBox sidebar = new VBox();
        sidebar.setMinWidth(SIDEBAR_WIDTH);
        sidebar.setStyle("-fx-background-color: lightgray;");

        Label titleLabel = new Label("Monitoring");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        VBox.setMargin(titleLabel, new Insets(10, 0, 10, 0)); // Padding top and bottom
        sidebar.setAlignment(Pos.TOP_CENTER); // Center the title horizontally
        sidebar.getChildren().add(titleLabel);

        // Add a logging zone (TextArea) with padding and extended to the bottom
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setWrapText(true);
        logArea.setStyle("-fx-font-family: monospace; -fx-font-size: 12px;");
        VBox.setMargin(logArea, new Insets(10)); // Padding on all sides
        VBox.setVgrow(logArea, javafx.scene.layout.Priority.ALWAYS); // Extend to the bottom
        sidebar.getChildren().add(logArea);

        HBox root = new HBox(canvasContainer, sidebar);
        primaryStage.setScene(new Scene(root, APP_WIDTH, APP_HEIGHT));
        primaryStage.setTitle("Smart Crossroads");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void drawBackground(GraphicsContext gc, int WIDTH, int HEIGHT) {
        // Gradient background for buildings (changed to dark red)
        LinearGradient gradient = new LinearGradient(0, 0, 0, HEIGHT, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.DARKRED), new Stop(1, Color.DARKRED.darker()));
        gc.setFill(gradient);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void drawIntersections(GraphicsContext gc, int WIDTH, int HEIGHT) {
        // Gradient for roads
        LinearGradient roadGradient = new LinearGradient(0, 0, 0, ROAD_WIDTH, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.GRAY), new Stop(1, Color.DARKGRAY));
        gc.setFill(roadGradient);

        // Draw two horizontal roads
        gc.fillRect(0, HEIGHT / 3 - ROAD_WIDTH / 2, WIDTH, ROAD_WIDTH);
        gc.fillRect(0, 2 * HEIGHT / 3 - ROAD_WIDTH / 2, WIDTH, ROAD_WIDTH);

        // Draw two vertical roads
        gc.fillRect(WIDTH / 3 - ROAD_WIDTH / 2, 0, ROAD_WIDTH, HEIGHT);
        gc.fillRect(2 * WIDTH / 3 - ROAD_WIDTH / 2, 0, ROAD_WIDTH, HEIGHT);
    }

    private void drawDashedLines(GraphicsContext gc, int WIDTH, int HEIGHT) {
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        // Draw horizontal dashed lines
        for (int x = 0; x < WIDTH; x += DASH_LENGTH + DASH_GAP) {
            if ((x + DASH_LENGTH) < WIDTH / 3 - ROAD_WIDTH / 2
                    || (x > WIDTH / 3 + ROAD_WIDTH / 2 && x + DASH_LENGTH < 2 * WIDTH / 3 - ROAD_WIDTH / 2)
                    || x > 2 * WIDTH / 3 + ROAD_WIDTH / 2) {
                gc.strokeLine(x, HEIGHT / 3, x + DASH_LENGTH, HEIGHT / 3);
                gc.strokeLine(x, 2 * HEIGHT / 3, x + DASH_LENGTH, 2 * HEIGHT / 3);
            }
        }

        // Draw vertical dashed lines
        for (int y = 0; y < HEIGHT; y += DASH_LENGTH + DASH_GAP) {
            if ((y + DASH_LENGTH) < HEIGHT / 3 - ROAD_WIDTH / 2
                    || (y > HEIGHT / 3 + ROAD_WIDTH / 2 && y + DASH_LENGTH < 2 * HEIGHT / 3 - ROAD_WIDTH / 2)
                    || y > 2 * HEIGHT / 3 + ROAD_WIDTH / 2) {
                gc.strokeLine(WIDTH / 3, y, WIDTH / 3, y + DASH_LENGTH);
                gc.strokeLine(2 * WIDTH / 3, y, 2 * WIDTH / 3, y + DASH_LENGTH);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}