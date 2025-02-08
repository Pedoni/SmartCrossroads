package it.unibo.smartcrossroads.utils;

import java.util.ArrayList;
import java.util.List;

import it.unibo.smartcrossroads.model.Point;

public class PathUtils {

    /**
     * Genera una lista di punti che approssima un arco circolare tra A e B.
     * 
     * @param A          Punto di partenza.
     * @param B          Punto di arrivo.
     * @param curveRight Se true, la curva sarà realizzata a destra (relativamente
     *                   al verso da A a B);
     *                   se false, la curva sarà realizzata a sinistra.
     * @return Una lista di punti che compongono la curva.
     */
    public static List<Point> generateCurvePoints(Point A, Point B, boolean curveRight) {
        List<Point> curvePoints = new ArrayList<>();

        // Calcoliamo il vettore del segmento e la lunghezza del segmento (corda)
        double dx = B.x() - A.x();
        double dy = B.y() - A.y();
        double chordLength = Math.sqrt(dx * dx + dy * dy);

        // Scegliamo un offset per determinare la curvatura.
        // Possiamo regolare questo valore (qui è scelto come 1/3 della lunghezza della
        // corda)
        double offset = chordLength / 3;

        // Calcoliamo il punto medio della corda
        double mx = (A.x() + B.x()) / 2.0;
        double my = (A.y() + B.y()) / 2.0;

        // Calcoliamo il vettore perpendicolare alla corda.
        // Per una curva a sinistra (relativamente al verso da A a B), il vettore
        // perpendicolare sarà (-dy, dx)
        // Per una curva a destra, usiamo (dy, -dx)
        double px, py;
        if (curveRight) {
            px = -dy;
            py = dx;
        } else {
            px = dy;
            py = -dx;
        }
        // Normalizziamo il vettore perpendicolare
        double pLength = Math.sqrt(px * px + py * py);
        px /= pLength;
        py /= pLength;

        // Determiniamo il centro della circonferenza spostando il punto medio lungo il
        // vettore perpendicolare
        double cx = mx + px * offset;
        double cy = my + py * offset;

        // Calcoliamo il raggio (distanza dal centro al punto A)
        double radius = Math.sqrt((A.x() - cx) * (A.x() - cx) + (A.y() - cy) * (A.y() - cy));

        // Calcoliamo gli angoli (in radianti) dei punti A e B rispetto al centro
        double angleA = Math.atan2(A.y() - cy, A.x() - cx);
        double angleB = Math.atan2(B.y() - cy, B.x() - cx);

        // Calcoliamo l'angolo di sweep lungo la circonferenza.
        double deltaAngle = angleB - angleA;

        // Adattiamo deltaAngle affinché sia compreso tra -PI e PI
        if (deltaAngle > Math.PI) {
            deltaAngle -= 2 * Math.PI;
        } else if (deltaAngle < -Math.PI) {
            deltaAngle += 2 * Math.PI;
        }

        /*
         * Per decidere il verso dello sweep in base alla curva (sinistra o destra)
         * potremmo voler "forzare" il senso del deltaAngle:
         * - Se curveLeft è true e deltaAngle è negativo, aggiungiamo 2π per avere un
         * sweep positivo.
         * - Se curveLeft è false e deltaAngle è positivo, sottraiamo 2π per avere un
         * sweep negativo.
         */
        if (curveRight && deltaAngle < 0) {
            deltaAngle += 2 * Math.PI;
        }
        if (!curveRight && deltaAngle > 0) {
            deltaAngle -= 2 * Math.PI;
        }

        // Scegliamo il numero di punti intermedi (più è alto, più la curva sarà liscia)
        int numSamples = 20;
        for (int i = 0; i <= numSamples; i++) {
            double t = (double) i / numSamples;
            double angle = angleA + deltaAngle * t;
            double x = cx + radius * Math.cos(angle);
            double y = cy + radius * Math.sin(angle);
            curvePoints.add(new Point(x, y));
        }

        return curvePoints;
    }

}
