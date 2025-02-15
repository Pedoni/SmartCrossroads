package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.image.Image;

import model.*;

public class Utils {
        public final static Map<String, LinkedPoint> map = new HashMap<>();
        public final static Map<Integer, Image> carImages = new HashMap<>();

        public static void calculatePoints(int tile) {
                map.put("s1a", new LinkedPoint(0, tile * 4.5, List.of("s1b")));
                map.put("s1b", new LinkedPoint(tile * 5, tile * 4.5, List.of("e3b", "c4a", "c1a")));
                map.put("s2a", new LinkedPoint(0, tile * 9.5, List.of("s2b")));
                map.put("s2b", new LinkedPoint(tile * 5, tile * 9.5, List.of("e8b", "c2b", "c8a")));
                map.put("s3a", new LinkedPoint(tile * 5.5, 0, List.of("s3b")));
                map.put("s3b", new LinkedPoint(tile * 5.5, tile * 3, List.of("e1b", "c1a", "c4a")));
                map.put("s4a", new LinkedPoint(tile * 10.5, 0, List.of("s4b")));
                map.put("s4b", new LinkedPoint(tile * 10.5, tile * 3, List.of("e5b", "c3b", "c5a")));
                map.put("s5a", new LinkedPoint(tile * 17, tile * 3.5, List.of("s5b")));
                map.put("s5b", new LinkedPoint(tile * 12, tile * 3.5, List.of("e4b", "c3b", "c5a")));
                map.put("s6a", new LinkedPoint(tile * 17, tile * 8.5, List.of("s6b")));
                map.put("s6b", new LinkedPoint(tile * 12, tile * 8.5, List.of("e7b", "c6b", "c7b")));
                map.put("s7a", new LinkedPoint(tile * 11.5, tile * 13, List.of("s7b")));
                map.put("s7b", new LinkedPoint(tile * 11.5, tile * 10, List.of("e6b", "c6b", "c7b")));
                map.put("s8a", new LinkedPoint(tile * 6.5, tile * 13, List.of("s8b")));
                map.put("s8b", new LinkedPoint(tile * 6.5, tile * 10, List.of("e2b", "c2b", "c8a")));

                map.put("e1a", new LinkedPoint(0, tile * 3.5, List.of()));
                map.put("e1b", new LinkedPoint(tile * 5, tile * 3.5, List.of("e1a")));
                map.put("e2a", new LinkedPoint(0, tile * 8.5, List.of()));
                map.put("e2b", new LinkedPoint(tile * 5, tile * 8.5, List.of("e2a")));
                map.put("e3a", new LinkedPoint(tile * 6.5, 0, List.of()));
                map.put("e3b", new LinkedPoint(tile * 6.5, tile * 3, List.of("e3a")));
                map.put("e4a", new LinkedPoint(tile * 11.5, 0, List.of()));
                map.put("e4b", new LinkedPoint(tile * 11.5, tile * 3, List.of("e4a")));
                map.put("e5a", new LinkedPoint(tile * 17, tile * 4.5, List.of()));
                map.put("e5b", new LinkedPoint(tile * 12, tile * 4.5, List.of("e5a")));
                map.put("e6a", new LinkedPoint(tile * 17, tile * 9.5, List.of()));
                map.put("e6b", new LinkedPoint(tile * 12, tile * 9.5, List.of("e6a")));
                map.put("e7a", new LinkedPoint(tile * 10.5, tile * 13, List.of()));
                map.put("e7b", new LinkedPoint(tile * 10.5, tile * 10, List.of("e7a")));
                map.put("e8a", new LinkedPoint(tile * 5.5, tile * 13, List.of()));
                map.put("e8b", new LinkedPoint(tile * 5.5, tile * 10, List.of("e8a")));

                map.put("c1a", new LinkedPoint(tile * 5.5, tile * 5, List.of("c1b")));
                map.put("c1b", new LinkedPoint(tile * 5.5, tile * 8, List.of("e2b", "e8b", "c8a")));
                map.put("c2a", new LinkedPoint(tile * 6.5, tile * 5, List.of("c4a", "e3b", "e1b")));
                map.put("c2b", new LinkedPoint(tile * 6.5, tile * 8, List.of("c2a")));
                map.put("c3a", new LinkedPoint(tile * 7, tile * 3.5, List.of("e1b", "e3b", "c1a")));
                map.put("c3b", new LinkedPoint(tile * 10, tile * 3.5, List.of("c3a")));
                map.put("c4a", new LinkedPoint(tile * 7, tile * 4.5, List.of("c4b")));
                map.put("c4b", new LinkedPoint(tile * 10, tile * 4.5, List.of("e4b", "e5b", "c5a")));
                map.put("c5a", new LinkedPoint(tile * 10.5, tile * 5, List.of("c5b")));
                map.put("c5b", new LinkedPoint(tile * 10.5, tile * 8, List.of("e6b", "e7b", "c7b")));
                map.put("c6a", new LinkedPoint(tile * 11.5, tile * 5, List.of("e4b", "e5b", "c3b")));
                map.put("c6b", new LinkedPoint(tile * 11.5, tile * 8, List.of("c6a")));
                map.put("c7a", new LinkedPoint(tile * 7, tile * 8.5, List.of("e2b", "e8b", "c2b")));
                map.put("c7b", new LinkedPoint(tile * 10, tile * 8.5, List.of("c7a")));
                map.put("c8a", new LinkedPoint(tile * 7, tile * 9.5, List.of("c8b")));
                map.put("c8b", new LinkedPoint(tile * 10, tile * 9.5, List.of("e6b", "e7b", "c6b")));
        }

        public static void loadCarImages() {
                carImages.put(1, new Image("file:src/main/resources/it/unibo/smartcrossroads/car1_s.png"));
                carImages.put(2, new Image("file:src/main/resources/it/unibo/smartcrossroads/car2_s.png"));
                carImages.put(3, new Image("file:src/main/resources/it/unibo/smartcrossroads/car3_s.png"));
        }

}
