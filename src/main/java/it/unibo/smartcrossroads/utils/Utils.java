package it.unibo.smartcrossroads.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.smartcrossroads.model.Path;
import it.unibo.smartcrossroads.model.Point;

public class Utils {
    final static Map<String, Point> map = new HashMap<>();

    public static void calculatePoints(int h, int w) {
        map.put("s1a", new Point(0, h / 3 + Constants.ROAD_WIDTH / 4));
        map.put("s1b", new Point(w / 3 - Constants.ROAD_WIDTH / 2, h / 3 + Constants.ROAD_WIDTH / 4));
        map.put("s2a", new Point(0, h * 2 / 3 + Constants.ROAD_WIDTH / 4));
        map.put("s2b", new Point(w / 3 - Constants.ROAD_WIDTH / 2, h * 2 / 3 + Constants.ROAD_WIDTH / 4));
        map.put("s3a", new Point(w / 3 - Constants.ROAD_WIDTH / 4, 0));
        map.put("s3b", new Point(w / 3 - Constants.ROAD_WIDTH / 4, h / 3 - Constants.ROAD_WIDTH / 2));
        map.put("s4a", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 4, 0));
        map.put("s4b", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 4, h / 3 - Constants.ROAD_WIDTH / 2));
        map.put("s5a", new Point(w, h / 3 - Constants.ROAD_WIDTH / 4));
        map.put("s5b", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 2, h / 3 - Constants.ROAD_WIDTH / 4));
        map.put("s6a", new Point(w, h * 2 / 3 - Constants.ROAD_WIDTH / 4));
        map.put("s6b", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 2, h * 2 / 3 - Constants.ROAD_WIDTH / 4));
        map.put("s7a", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 4, h));
        map.put("s7b", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 4, h * 2 / 3 + Constants.ROAD_WIDTH / 2));
        map.put("s8a", new Point(w / 3 + Constants.ROAD_WIDTH / 4, h));
        map.put("s8b", new Point(w / 3 + Constants.ROAD_WIDTH / 4, h * 2 / 3 + Constants.ROAD_WIDTH / 2));

        map.put("e1a", new Point(0, h / 3 - Constants.ROAD_WIDTH / 4));
        map.put("e1b", new Point(w / 3 - Constants.ROAD_WIDTH / 2, h / 3 - Constants.ROAD_WIDTH / 4));
        map.put("e2a", new Point(0, h * 2 / 3 - Constants.ROAD_WIDTH / 4));
        map.put("e2b", new Point(w / 3 - Constants.ROAD_WIDTH / 2, h * 2 / 3 - Constants.ROAD_WIDTH / 4));
        map.put("e3a", new Point(w / 3 + Constants.ROAD_WIDTH / 4, 0));
        map.put("e3b", new Point(w / 3 + Constants.ROAD_WIDTH / 4, h / 3 - Constants.ROAD_WIDTH / 2));
        map.put("e4a", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 4, 0));
        map.put("e4b", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 4, h / 3 - Constants.ROAD_WIDTH / 2));
        map.put("e5a", new Point(w, h / 3 + Constants.ROAD_WIDTH / 4));
        map.put("e5b", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 2, h / 3 + Constants.ROAD_WIDTH / 4));
        map.put("e6a", new Point(w, h * 2 / 3 + Constants.ROAD_WIDTH / 4));
        map.put("e6b", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 2, h * 2 / 3 + Constants.ROAD_WIDTH / 4));
        map.put("e7a", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 4, h));
        map.put("e7b", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 4, h * 2 / 3 + Constants.ROAD_WIDTH / 2));
        map.put("e8a", new Point(w / 3 - Constants.ROAD_WIDTH / 4, h));
        map.put("e8b", new Point(w / 3 - Constants.ROAD_WIDTH / 4, h * 2 / 3 + Constants.ROAD_WIDTH / 2));

        map.put("c1a", new Point(w / 3 - Constants.ROAD_WIDTH / 4, h / 3 + Constants.ROAD_WIDTH / 2));
        map.put("c1b", new Point(w / 3 - Constants.ROAD_WIDTH / 4, h * 2 / 3 - Constants.ROAD_WIDTH / 2));
        map.put("c2a", new Point(w / 3 + Constants.ROAD_WIDTH / 4, h / 3 + Constants.ROAD_WIDTH / 2));
        map.put("c2b", new Point(w / 3 + Constants.ROAD_WIDTH / 4, h * 2 / 3 - Constants.ROAD_WIDTH / 2));
        map.put("c3a", new Point(w / 3 + Constants.ROAD_WIDTH / 2, h / 3 - Constants.ROAD_WIDTH / 4));
        map.put("c3b", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 2, h / 3 - Constants.ROAD_WIDTH / 4));
        map.put("c4a", new Point(w / 3 + Constants.ROAD_WIDTH / 2, h / 3 + Constants.ROAD_WIDTH / 4));
        map.put("c4b", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 2, h / 3 + Constants.ROAD_WIDTH / 4));
        map.put("c5a", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 4, h / 3 + Constants.ROAD_WIDTH / 2));
        map.put("c5b", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 4, h * 2 / 3 - Constants.ROAD_WIDTH / 2));
        map.put("c6a", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 4, h / 3 + Constants.ROAD_WIDTH / 2));
        map.put("c6b", new Point(w * 2 / 3 + Constants.ROAD_WIDTH / 4, h * 2 / 3 - Constants.ROAD_WIDTH / 2));
        map.put("c7a", new Point(w / 3 + Constants.ROAD_WIDTH / 2, h * 2 / 3 - Constants.ROAD_WIDTH / 4));
        map.put("c7b", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 2, h * 2 / 3 - Constants.ROAD_WIDTH / 4));
        map.put("c8a", new Point(w / 3 + Constants.ROAD_WIDTH / 2, h * 2 / 3 + Constants.ROAD_WIDTH / 4));
        map.put("c8b", new Point(w * 2 / 3 - Constants.ROAD_WIDTH / 2, h * 2 / 3 + Constants.ROAD_WIDTH / 4));
    }

    public static Path getPath1() {
        final List<Point> points = new ArrayList<>();
        final Point p0 = map.get("s1a");
        final Point p1 = map.get("s1b");
        final Point p2 = map.get("c4a");
        final Point p3 = map.get("c4b");
        final Point p4 = map.get("c5a");

        List<Point> p3p4Curve = PathUtils.generateCurvePoints(p3, p4, true);
        p3p4Curve.remove(0);

        final Point p5 = map.get("c5b");
        final Point p6 = map.get("e6b");

        List<Point> p5p6Curve = PathUtils.generateCurvePoints(p5, p6, false);
        p5p6Curve.remove(0);

        final Point p7 = map.get("e6a");

        points.add(p0);
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.addAll(p3p4Curve);
        points.add(p4);
        points.add(p5);
        points.addAll(p5p6Curve);
        points.add(p6);
        points.add(p7);

        return new Path(points);
    }

    public static Path getPath2() {
        final List<Point> points = new ArrayList<>();
        final Point p0 = map.get("s2a");
        final Point p1 = map.get("s2b");
        final Point p2 = map.get("c2b");
        final Point p3 = map.get("c2a");

        List<Point> p1p2Curve = PathUtils.generateCurvePoints(p1, p2, false);
        p1p2Curve.remove(0);

        final Point p4 = map.get("e3b");
        final Point p5 = map.get("e3a");

        points.add(p0);
        points.add(p1);
        points.addAll(p1p2Curve);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);

        return new Path(points);
    }

    public static List<Path> getAllPaths() {
        final List<Path> paths = new ArrayList<>();

        paths.add(getPath1());
        paths.add(getPath2());

        return paths;
    }
}
