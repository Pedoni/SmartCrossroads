package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jason.util.Pair;
import javafx.scene.image.Image;

import model.LinkedPoint;
import model.view_elements.Tile;

public final class Utils {

	public static final Map<String, LinkedPoint> map = new HashMap<>();
	public static final Map<Integer, Image> carImages = new HashMap<>();
	public static final List<Tile> tiles = new ArrayList<>();

	// Strade verticali
	private static final List<Integer> corsieX = List.of(5, 10);
	// Strade orizzonatali
	private static final List<Integer> corsieY = List.of(3, 8);
	public static final Map<Pair<Tile, Direction>, List<Tile>> directions = new HashMap<>();

	public static void initializeThings() {
		final int numRows = Constants.ROWS;
		final int numColumns = Constants.COLUMNS;
		for (int x = 0; x < numColumns; x++) {
			final boolean onRoadX = corsieX.contains(x) || corsieX.contains(x - 1);
			for (int y = 0; y < numRows; y++) {
				final boolean onRoadY = corsieY.contains(y) || corsieY.contains(y - 1);
				final Tile tile = new Tile(x, y);
				// Siamo una strada
				if (onRoadX ^ onRoadY) {
					// Verticale
					if (onRoadX) {
						// Verso il basso
						if (corsieX.contains(x)) {
							directions.put(new Pair<>(tile, Direction.NORTH), List.of(new Tile(x, y + 1)));
						} else { // Verso l'alto
							directions.put(new Pair<>(tile, Direction.SOUTH), List.of(new Tile(x, y - 1)));
						}
					}
					// Orizzontale
					if (onRoadY) {
						// Verso sinistra
						if (corsieY.contains(y)) {
							directions.put(new Pair<>(tile, Direction.EAST), List.of(new Tile(x - 1, y)));
						} else { // Verso destra
							directions.put(new Pair<>(tile, Direction.WEST), List.of(new Tile(x + 1, y)));
						}
					}

				} else if (onRoadX && onRoadY) { // Incrocio
					// Sinistra
					if (corsieX.contains(x)) {
						// In alto
						if (corsieY.contains(y)) {
							directions.put(new Pair<>(tile, Direction.NORTH), List.of(
									new Tile(x - 1, y),
									new Tile(x, y + 1),
									new Tile(x + 1, y + 1)));
							directions.put(new Pair<>(tile, Direction.EAST), List.of(
									new Tile(x - 1, y)));
							directions.put(new Pair<>(tile, Direction.SOUTH), List.of(
									new Tile(x - 1, y)));
						} else { // In basso
							directions.put(new Pair<>(tile, Direction.WEST), List.of(
									new Tile(x + 1, y),
									new Tile(x, y + 1),
									new Tile(x + 1, y - 1)));
							directions.put(new Pair<>(tile, Direction.NORTH), List.of(
									new Tile(x + 1, y)));
							directions.put(new Pair<>(tile, Direction.EAST), List.of(
									new Tile(x + 1, y)));
						}
					} else { // Destra
						// In alto
						if (corsieY.contains(y)) {
							directions.put(new Pair<>(tile, Direction.EAST), List.of(
									new Tile(x - 1, y),
									new Tile(x, y - 1),
									new Tile(x - 1, y + 1)));
							directions.put(new Pair<>(tile, Direction.SOUTH), List.of(
									new Tile(x - 1, y)));
							directions.put(new Pair<>(tile, Direction.WEST), List.of(
									new Tile(x - 1, y)));
						} else { // In basso
							directions.put(new Pair<>(tile, Direction.SOUTH), List.of(
									new Tile(x - 1, y),
									new Tile(x + 1, y),
									new Tile(x - 1, y - 1)));
							directions.put(new Pair<>(tile, Direction.WEST), List.of(
									new Tile(x + 1, y)));
							directions.put(new Pair<>(tile, Direction.NORTH), List.of(
									new Tile(x + 1, y)));
						}
					}
				} else { // Non strada

				}

				tiles.add(tile);
			}
		}
	}

	public static void calculatePoints() {

		/*
		 * map.put("s1a", new LinkedPoint(0, 4, Map.ofEntries(Map.entry("-",
		 * List.of("s1b")))));
		 * map.put("s1b", new LinkedPoint(1, 4, Map.ofEntries(Map.entry("s1a",
		 * List.of("s1c")))));
		 * map.put("s1c", new LinkedPoint(2, 4, Map.ofEntries(Map.entry("s1b",
		 * List.of("s1d")))));
		 * map.put("s1d", new LinkedPoint(3, 4, Map.ofEntries(Map.entry("s1c",
		 * List.of("s1e")))));
		 * map.put("s1e", new LinkedPoint(4, 4, Map.ofEntries(Map.entry("s1d",
		 * List.of("i1d")))));
		 * map.put("s2a", new LinkedPoint(0, 9, Map.ofEntries(Map.entry("-",
		 * List.of("s2b")))));
		 * map.put("s2b", new LinkedPoint(1, 9, Map.ofEntries(Map.entry("s2a",
		 * List.of("s2c")))));
		 * map.put("s2c", new LinkedPoint(2, 9, Map.ofEntries(Map.entry("s2b",
		 * List.of("s2d")))));
		 * map.put("s2d", new LinkedPoint(3, 9, Map.ofEntries(Map.entry("s2c",
		 * List.of("s2e")))));
		 * map.put("s2e", new LinkedPoint(4, 9, Map.ofEntries(Map.entry("s2d",
		 * List.of("i4d")))));
		 * map.put("s3a", new LinkedPoint(5, 0, Map.ofEntries(Map.entry("-",
		 * List.of("s3b")))));
		 * map.put("s3b", new LinkedPoint(5, 1, Map.ofEntries(Map.entry("s3a",
		 * List.of("s3c")))));
		 * map.put("s3c", new LinkedPoint(5, 2, Map.ofEntries(Map.entry("s3b",
		 * List.of("i1a")))));
		 * map.put("s4a", new LinkedPoint(10, 0, Map.ofEntries(Map.entry("-",
		 * List.of("s4b")))));
		 * map.put("s4b", new LinkedPoint(10, 1, Map.ofEntries(Map.entry("s4a",
		 * List.of("s4c")))));
		 * map.put("s4c", new LinkedPoint(10, 2, Map.ofEntries(Map.entry("s4b",
		 * List.of("i2a")))));
		 * map.put("s5a", new LinkedPoint(16, 3, Map.ofEntries(Map.entry("-",
		 * List.of("s5b")))));
		 * map.put("s5b", new LinkedPoint(15, 3, Map.ofEntries(Map.entry("s5a",
		 * List.of("s5c")))));
		 * map.put("s5c", new LinkedPoint(14, 3, Map.ofEntries(Map.entry("s5b",
		 * List.of("s5d")))));
		 * map.put("s5d", new LinkedPoint(13, 3, Map.ofEntries(Map.entry("s5c",
		 * List.of("s5e")))));
		 * map.put("s5e", new LinkedPoint(12, 3, Map.ofEntries(Map.entry("s5d",
		 * List.of("i2b")))));
		 * map.put("s6a", new LinkedPoint(16, 8, Map.ofEntries(Map.entry("-",
		 * List.of("s6b")))));
		 * map.put("s6b", new LinkedPoint(15, 8, Map.ofEntries(Map.entry("s6a",
		 * List.of("s6c")))));
		 * map.put("s6c", new LinkedPoint(14, 8, Map.ofEntries(Map.entry("s6b",
		 * List.of("s6d")))));
		 * map.put("s6d", new LinkedPoint(13, 8, Map.ofEntries(Map.entry("s6c",
		 * List.of("s6e")))));
		 * map.put("s6e", new LinkedPoint(12, 8, Map.ofEntries(Map.entry("s6d",
		 * List.of("i3b")))));
		 * map.put("s7a", new LinkedPoint(11, 12, Map.ofEntries(Map.entry("-",
		 * List.of("s7b")))));
		 * map.put("s7b", new LinkedPoint(11, 11, Map.ofEntries(Map.entry("s7a",
		 * List.of("s7c")))));
		 * map.put("s7c", new LinkedPoint(11, 10, Map.ofEntries(Map.entry("s7b",
		 * List.of("i3c")))));
		 * map.put("s8a", new LinkedPoint(6, 12, Map.ofEntries(Map.entry("-",
		 * List.of("s8b")))));
		 * map.put("s8b", new LinkedPoint(6, 11, Map.ofEntries(Map.entry("s8a",
		 * List.of("s8c")))));
		 * map.put("s8c", new LinkedPoint(6, 10, Map.ofEntries(Map.entry("s8b",
		 * List.of("i4c")))));
		 * 
		 * map.put("e1a", new LinkedPoint(0, 3, null));
		 * map.put("e1b", new LinkedPoint(1, 3, Map.ofEntries(Map.entry("e1c",
		 * List.of("e1a")))));
		 * map.put("e1c", new LinkedPoint(2, 3, Map.ofEntries(Map.entry("e1d",
		 * List.of("e1b")))));
		 * map.put("e1d", new LinkedPoint(3, 3, Map.ofEntries(Map.entry("e1e",
		 * List.of("e1c")))));
		 * map.put("e1e", new LinkedPoint(4, 3, Map.ofEntries(Map.entry("i1a",
		 * List.of("e1d")))));
		 * map.put("e2a", new LinkedPoint(0, 8, null));
		 * map.put("e2b", new LinkedPoint(1, 8, Map.ofEntries(Map.entry("e2c",
		 * List.of("e2a")))));
		 * map.put("e2c", new LinkedPoint(2, 8, Map.ofEntries(Map.entry("e2d",
		 * List.of("e2b")))));
		 * map.put("e2d", new LinkedPoint(3, 8, Map.ofEntries(Map.entry("e2e",
		 * List.of("e2c")))));
		 * map.put("e2e", new LinkedPoint(4, 8, Map.ofEntries(Map.entry("i4a",
		 * List.of("e2d")))));
		 * map.put("e3a", new LinkedPoint(6, 0, null));
		 * map.put("e3b", new LinkedPoint(6, 1, Map.ofEntries(Map.entry("e3c",
		 * List.of("e3a")))));
		 * map.put("e3c", new LinkedPoint(6, 2, Map.ofEntries(Map.entry("i1b",
		 * List.of("e3b")))));
		 * map.put("e4a", new LinkedPoint(11, 0, null));
		 * map.put("e4b", new LinkedPoint(11, 1, Map.ofEntries(Map.entry("e4c",
		 * List.of("e4a")))));
		 * map.put("e4c", new LinkedPoint(11, 2, Map.ofEntries(Map.entry("i2b",
		 * List.of("e4b")))));
		 * map.put("e5a", new LinkedPoint(16, 4, null));
		 * map.put("e5b", new LinkedPoint(15, 4, Map.ofEntries(Map.entry("e5c",
		 * List.of("e5a")))));
		 * map.put("e5c", new LinkedPoint(14, 4, Map.ofEntries(Map.entry("e5d",
		 * List.of("e5b")))));
		 * map.put("e5d", new LinkedPoint(13, 4, Map.ofEntries(Map.entry("e5e",
		 * List.of("e5c")))));
		 * map.put("e5e", new LinkedPoint(12, 4, Map.ofEntries(Map.entry("i2c",
		 * List.of("e5d")))));
		 * map.put("e6a", new LinkedPoint(16, 9, null));
		 * map.put("e6b", new LinkedPoint(15, 9, Map.ofEntries(Map.entry("e6c",
		 * List.of("e6a")))));
		 * map.put("e6c", new LinkedPoint(14, 9, Map.ofEntries(Map.entry("e6d",
		 * List.of("e6b")))));
		 * map.put("e6d", new LinkedPoint(13, 9, Map.ofEntries(Map.entry("e6e",
		 * List.of("e6c")))));
		 * map.put("e6e", new LinkedPoint(12, 9, Map.ofEntries(Map.entry("i3c",
		 * List.of("e6d")))));
		 * map.put("e7a", new LinkedPoint(10, 12, null));
		 * map.put("e7b", new LinkedPoint(10, 11, Map.ofEntries(Map.entry("e7c",
		 * List.of("e7a")))));
		 * map.put("e7c", new LinkedPoint(10, 10, Map.ofEntries(Map.entry("i3d",
		 * List.of("e7b")))));
		 * map.put("e8a", new LinkedPoint(5, 12, null));
		 * map.put("e8b", new LinkedPoint(5, 11, Map.ofEntries(Map.entry("e8c",
		 * List.of("e8a")))));
		 * map.put("e8c", new LinkedPoint(5, 10, Map.ofEntries(Map.entry("i3d",
		 * List.of("e8b")))));
		 * 
		 * map.put("c1a", new LinkedPoint(5, 5, Map.ofEntries(Map.entry("i1d",
		 * List.of("c1b")))));
		 * map.put("c1b", new LinkedPoint(5, 6, Map.ofEntries(Map.entry("c1a",
		 * List.of("c1c")))));
		 * map.put("c1c", new LinkedPoint(5, 7, Map.ofEntries(Map.entry("c1b",
		 * List.of("i4a")))));
		 * map.put("c2a", new LinkedPoint(6, 5, Map.ofEntries(Map.entry("c2b",
		 * List.of("i1c")))));
		 * map.put("c2b", new LinkedPoint(6, 6, Map.ofEntries(Map.entry("c2c",
		 * List.of("c2a")))));
		 * map.put("c2c", new LinkedPoint(6, 7, Map.ofEntries(Map.entry("i4b",
		 * List.of("c2b")))));
		 * map.put("c3a", new LinkedPoint(7, 3, Map.ofEntries(Map.entry("c3b",
		 * List.of("i1b")))));
		 * map.put("c3b", new LinkedPoint(8, 3, Map.ofEntries(Map.entry("c3c",
		 * List.of("c3a")))));
		 * map.put("c3c", new LinkedPoint(9, 3, Map.ofEntries(Map.entry("i2a",
		 * List.of("c3b")))));
		 * map.put("c4a", new LinkedPoint(7, 4, Map.ofEntries(Map.entry("i1c",
		 * List.of("c4b")))));
		 * map.put("c4b", new LinkedPoint(8, 4, Map.ofEntries(Map.entry("c4a",
		 * List.of("c4c")))));
		 * map.put("c4c", new LinkedPoint(9, 4, Map.ofEntries(Map.entry("c4b",
		 * List.of("i2d")))));
		 * map.put("c5a", new LinkedPoint(10, 5, Map.ofEntries(Map.entry("i2d",
		 * List.of("c5b")))));
		 * map.put("c5b", new LinkedPoint(10, 6, Map.ofEntries(Map.entry("c5a",
		 * List.of("c5c")))));
		 * map.put("c5c", new LinkedPoint(10, 7, Map.ofEntries(Map.entry("c5b",
		 * List.of("i3a")))));
		 * map.put("c6a", new LinkedPoint(11, 5, Map.ofEntries(Map.entry("c6b",
		 * List.of("i2c")))));
		 * map.put("c6b", new LinkedPoint(11, 6, Map.ofEntries(Map.entry("c6c",
		 * List.of("c6a")))));
		 * map.put("c6c", new LinkedPoint(11, 7, Map.ofEntries(Map.entry("i3b",
		 * List.of("c6b")))));
		 * map.put("c7a", new LinkedPoint(7, 8, Map.ofEntries(Map.entry("c7b",
		 * List.of("i4b")))));
		 * map.put("c7b", new LinkedPoint(8, 8, Map.ofEntries(Map.entry("c7c",
		 * List.of("c7a")))));
		 * map.put("c7c", new LinkedPoint(9, 8, Map.ofEntries(Map.entry("i3a",
		 * List.of("c7b")))));
		 * map.put("c8a", new LinkedPoint(7, 9, Map.ofEntries(Map.entry("i4c",
		 * List.of("c8b")))));
		 * map.put("c8b", new LinkedPoint(8, 9, Map.ofEntries(Map.entry("c8a",
		 * List.of("c8c")))));
		 * map.put("c8c", new LinkedPoint(9, 9, Map.ofEntries(Map.entry("c8b",
		 * List.of("i3d")))));
		 * 
		 * map.put("i1a", new LinkedPoint(
		 * 5,
		 * 3,
		 * Map.ofEntries(
		 * Map.entry("s3c", List.of("e1e", "i1c", "i1d")),
		 * Map.entry("i1b", List.of("e1e")),
		 * Map.entry("i1c", List.of("e1e")))));
		 */}

	public static void loadCarImages() {
		carImages.put(1, new Image("file:src/main/resources/it/unibo/smartcrossroads/car1_s.png"));
		carImages.put(2, new Image("file:src/main/resources/it/unibo/smartcrossroads/car2_s.png"));
		carImages.put(3, new Image("file:src/main/resources/it/unibo/smartcrossroads/car3_s.png"));
	}

}
