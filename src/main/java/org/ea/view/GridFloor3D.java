package org.ea.view;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.transform.Rotate;

/**
 * Raster‐/Gitterboden in der XZ‐Ebene (Y = 0).
 */
public final class GridFloor3D extends Group {

    /**
     * @param size   halbe Ausdehnung (Grid reicht von –size … +size)
     * @param step   Rasterabstand
     * @param lineW  Linienstärke
     */
    public GridFloor3D(double size, double step, double lineW) {
        Color major = Color.rgb(200, 200, 200);
        Color minor = Color.rgb(200, 200, 200);

        PhongMaterial matMajor = new PhongMaterial(major);
        PhongMaterial matMinor = new PhongMaterial(minor);

        int lines = (int) Math.ceil(size / step);

        for (int i = -lines; i <= lines; i++) {
            double pos = i * step;

            // ─ Linien entlang X ─
            getChildren().add(buildLine(
                    new Point3D(-size, 0,  pos),
                    new Point3D( size, 0,  pos),
                    lineW,
                    (i % 5 == 0) ? matMajor : matMinor
            ));
            // ─ Linien entlang Z ─
            getChildren().add(buildLine(
                    new Point3D( pos, 0, -size),
                    new Point3D( pos, 0,  size),
                    lineW,
                    (i % 5 == 0) ? matMajor : matMinor
            ));
        }
    }

    /* --------- Helfer: dünne Box als 3-D-Linie --------- */
    private static Node buildLine(Point3D p1, Point3D p2,
                                  double thickness, PhongMaterial mat) {

        Point3D diff = p2.subtract(p1);
        double len   = diff.magnitude();

        Box line = new Box(thickness, len, thickness);
        line.setMaterial(mat);
        line.setCullFace(CullFace.FRONT);          // beidseitig sichtbar

        /* Mittel­punkt positionieren */
        Point3D mid = p1.midpoint(p2);
        line.setTranslateX(mid.getX());
        line.setTranslateY(mid.getY());
        line.setTranslateZ(mid.getZ());

        /* Box-Achse (0,1,0) → Richtungs­vektor diff drehen */
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D axis  = yAxis.crossProduct(diff);
        double  angle = Math.toDegrees(Math.acos(
                yAxis.dotProduct(diff) / len));

        if (!axis.equals(Point3D.ZERO) && angle != 0.0) {
            line.getTransforms().add(
                    new Rotate(angle, axis));
        }
        return line;      // ← Rückgabetyp ist jetzt Node
    }
}