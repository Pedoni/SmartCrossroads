package utils;

public class Configuration {
    public static int TILE_SIZE;
    public static int APP_WIDTH;
    public static int APP_HEIGHT;
    public static int GRAPHIC_WIDTH;
    public static int SIDEBAR_WIDTH;
    public static int GRID_COLS = 17;
    public static int GRID_ROWS = 13;

    public static void initializeSizes(double screenWidth) {
        APP_WIDTH = (int) (screenWidth * 3 / 4);
        GRAPHIC_WIDTH = (int) (APP_WIDTH * 3 / 4);
        TILE_SIZE = GRAPHIC_WIDTH / 17;
        GRAPHIC_WIDTH = TILE_SIZE * 17;
        SIDEBAR_WIDTH = (int) (APP_WIDTH / 4);
        APP_HEIGHT = TILE_SIZE * GRID_ROWS;
    }
}
