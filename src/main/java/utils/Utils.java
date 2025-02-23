package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jason.util.Pair;
import javafx.scene.image.Image;

import model.view_elements.Tile;

public final class Utils {

	private static final Map<Integer, Image> carImages = new HashMap<>();

	// Strade verticali
	private static final List<Integer> lanesX = List.of(5, 10);
	// Strade orizzonatali
	private static final List<Integer> lanesY = List.of(3, 8);
	private static final Map<Pair<Tile, Direction>, List<Pair<Tile, Direction>>> directions = new HashMap<>();

	public static void initializeThings() {
		final int numRows = Constants.ROWS;
		final int numColumns = Constants.COLUMNS;
		for (int x = 0; x < numColumns; x++) {
			final boolean onRoadX = lanesX.contains(x) || lanesX.contains(x - 1);
			for (int y = 0; y < numRows; y++) {
				final boolean onRoadY = lanesY.contains(y) || lanesY.contains(y - 1);
				final Tile tile = new Tile(x, y);
				// Siamo una strada
				if (onRoadX ^ onRoadY) {
					// Verticale
					if (onRoadX) {
						// Verso il basso
						if (lanesX.contains(x)) {
							directions.put(new Pair<>(tile, Direction.NORTH),
									List.of(new Pair<>(new Tile(x, y + 1), Direction.NORTH)));
						} else { // Verso l'alto
							directions.put(new Pair<>(tile, Direction.SOUTH),
									List.of(new Pair<>(new Tile(x, y - 1), Direction.SOUTH)));
						}
					}
					// Orizzontale
					if (onRoadY) {
						// Verso sinistra
						if (lanesY.contains(y)) {
							directions.put(new Pair<>(tile, Direction.EAST),
									List.of(new Pair<>(new Tile(x - 1, y), Direction.EAST)));
						} else { // Verso destra
							directions.put(new Pair<>(tile, Direction.WEST),
									List.of(new Pair<>(new Tile(x + 1, y), Direction.WEST)));
						}
					}

				} else if (onRoadX && onRoadY) { // Incrocio
					// Sinistra
					if (lanesX.contains(x)) {
						// In alto
						if (lanesY.contains(y)) {
							directions.put(new Pair<>(tile, Direction.NORTH), List.of(
									new Pair<>(new Tile(x - 1, y), Direction.EAST),
									new Pair<>(new Tile(x, y + 1), Direction.NORTH),
									new Pair<>(new Tile(x + 1, y + 1), Direction.WEST)));
							directions.put(new Pair<>(tile, Direction.EAST), List.of(
									new Pair<>(new Tile(x - 1, y), Direction.EAST)));
							directions.put(new Pair<>(tile, Direction.SOUTH), List.of(
									new Pair<>(new Tile(x - 1, y), Direction.EAST)));
						} else { // In basso
							directions.put(new Pair<>(tile, Direction.WEST), List.of(
									new Pair<>(new Tile(x + 1, y), Direction.WEST),
									new Pair<>(new Tile(x, y + 1), Direction.NORTH),
									new Pair<>(new Tile(x + 1, y - 1), Direction.SOUTH)));
							directions.put(new Pair<>(tile, Direction.NORTH), List.of(
									new Pair<>(new Tile(x, y + 1), Direction.NORTH)));
							directions.put(new Pair<>(tile, Direction.EAST), List.of(
									new Pair<>(new Tile(x, y + 1), Direction.NORTH)));
						}
					} else { // Destra
						// In alto
						if (lanesY.contains(y)) {
							directions.put(new Pair<>(tile, Direction.EAST), List.of(
									new Pair<>(new Tile(x - 1, y), Direction.EAST),
									new Pair<>(new Tile(x, y - 1), Direction.SOUTH),
									new Pair<>(new Tile(x - 1, y + 1), Direction.NORTH)));
							directions.put(new Pair<>(tile, Direction.SOUTH), List.of(
									new Pair<>(new Tile(x, y - 1), Direction.SOUTH)));
							directions.put(new Pair<>(tile, Direction.WEST), List.of(
									new Pair<>(new Tile(x, y - 1), Direction.SOUTH)));
						} else { // In basso
							directions.put(new Pair<>(tile, Direction.SOUTH), List.of(
									new Pair<>(new Tile(x, y - 1), Direction.SOUTH),
									new Pair<>(new Tile(x + 1, y), Direction.WEST),
									new Pair<>(new Tile(x - 1, y - 1), Direction.EAST)));
							directions.put(new Pair<>(tile, Direction.WEST), List.of(
									new Pair<>(new Tile(x + 1, y), Direction.WEST)));
							directions.put(new Pair<>(tile, Direction.NORTH), List.of(
									new Pair<>(new Tile(x + 1, y), Direction.WEST)));
						}
					}
				}
			}
		}
	}

	public static void loadCarImages() {
		carImages.put(1, new Image("file:src/main/resources/it/unibo/smartcrossroads/car1_s.png"));
		carImages.put(2, new Image("file:src/main/resources/it/unibo/smartcrossroads/car2_s.png"));
		carImages.put(3, new Image("file:src/main/resources/it/unibo/smartcrossroads/car3_s.png"));
	}

	public static Map<Integer, Image> getCarImages() {
		return carImages;
	}

	public static Map<Pair<Tile, Direction>, List<Pair<Tile, Direction>>> getDirections() {
		return directions;
	}

}
