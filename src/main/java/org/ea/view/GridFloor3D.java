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
 * Creates a checkered floor grid in the XZ‑plane (Y = 0) for visual reference in 3‑D scenes.
 *
 * @precondition All numeric parameters are positive and non‑zero.
 * @postcondition A {@link Group} populated with thin {@link Box} lines is fully constructed
 *                and ready to be added to a scene graph.
 */
public final class GridFloor3D extends Group {

    /**
     * Builds a grid floor that spans from {@code −size} to {@code +size} along both X and Z axes.
     *
     * @param size   half‑extent of the grid (total size is {@code 2×size})
     * @param step   spacing between adjacent grid lines
     * @param lineW  thickness (width) of each grid line
     *
     * @precondition {@code size > 0 && step > 0 && lineW > 0}
     * @postcondition Grid lines are generated and added to this {@code Group}
     */
    public GridFloor3D(double size, double step, double lineW) {
        Color major = Color.rgb(200, 200, 200);
        Color minor = Color.rgb(200, 200, 200);

        PhongMaterial matMajor = new PhongMaterial(major);
        PhongMaterial matMinor = new PhongMaterial(minor);

        int lines = (int) Math.ceil(size / step);

        for (int i = -lines; i <= lines; i++) {
            double pos = i * step;

            // Lines along X
            getChildren().add(buildLine(
                    new Point3D(-size, 0,  pos),
                    new Point3D( size, 0,  pos),
                    lineW,
                    (i % 5 == 0) ? matMajor : matMinor
            ));
            // Lines along Z
            getChildren().add(buildLine(
                    new Point3D( pos, 0, -size),
                    new Point3D( pos, 0,  size),
                    lineW,
                    (i % 5 == 0) ? matMajor : matMinor
            ));
        }
    }

    /**
     * Helper method that builds a thin {@link Box} acting as a 3‑D line between two points.
     *
     * @param p1        first endpoint in 3‑D space
     * @param p2        second endpoint in 3‑D space
     * @param thickness thickness of the line (box width & depth)
     * @param mat       {@link PhongMaterial} to apply for color
     * @return a {@link Node} representing the line
     *
     * @precondition {@code p1 != null && p2 != null && thickness > 0 && mat != null}
     * @postcondition A correctly oriented and positioned box is returned, ready for insertion
     *                into a scene graph.
     */
    private static Node buildLine(Point3D p1, Point3D p2,
                                  double thickness, PhongMaterial mat) {

        Point3D diff = p2.subtract(p1);
        double len   = diff.magnitude();

        Box line = new Box(thickness, len, thickness);
        line.setMaterial(mat);
        line.setCullFace(CullFace.FRONT); // visible from both sides

        // place at midpoint
        Point3D mid = p1.midpoint(p2);
        line.setTranslateX(mid.getX());
        line.setTranslateY(mid.getY());
        line.setTranslateZ(mid.getZ());

        // rotate so that box's Y‑axis aligns with direction vector
        Point3D yAxis = new Point3D(0, 1, 0);
        Point3D axis  = yAxis.crossProduct(diff);
        double  angle = Math.toDegrees(Math.acos(yAxis.dotProduct(diff) / len));

        if (!axis.equals(Point3D.ZERO) && angle != 0.0) {
            line.getTransforms().add(new Rotate(angle, axis));
        }
        return line;
    }
}
