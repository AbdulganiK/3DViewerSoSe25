package org.ea.view;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

/**
 * Einfache XYZ-Achsen (globales Koordinatensystem) für JavaFX-3D-Szenen.
 * <ul>
 *   <li> X-Achse: Rot  </li>
 *   <li> Y-Achse: Grün </li>
 *   <li> Z-Achse: Blau </li>
 * </ul>
 *
 * <p>Alle Achsen beginnen im Weltursprung (0/0/0).  Du kannst die Länge und
 * den Radius beim Erzeugen angeben sowie die Instanz später beliebig skalieren
 * oder ein/ausblenden.<br>
 * Beispiel:</p>
 *
 * <pre>
 * Group world = new Group();
 * world.getChildren().add(new GlobalAxes3D(300, 1.5));   // 300 Einheiten lang
 * </pre>
 */
public final class GlobalAxes3D extends Group {

    public GlobalAxes3D(double length) {
        this(length, 1.0);
    }

    public GlobalAxes3D(double length, double radius) {
        getChildren().addAll(
                buildAxis(Color.rgb(255, 50, 50, 0.3),   length, radius, Rotate.Z_AXIS, -90, Axis.X), // X

                //buildAxis(Color.GREEN, length, radius, null,          0,   Axis.Y), // Y
                buildAxis(Color.rgb(80, 80, 255, 0.3),  length, radius, Rotate.X_AXIS, -90, Axis.Z) // Z

        );
    }

    /* --------------------------------------------- */
    private enum Axis { X, Y, Z }

    private static Group buildAxis(Color color, double len, double radius,
                                   Point3D rotationAxis, double angleDeg, Axis axisDir) {
        Group axisGroup = new Group();

        // Zylinder für die positive Richtung
        Cylinder posCyl = createDirectionalCylinder(color, len, radius, rotationAxis, angleDeg, axisDir, 1);
        // Zylinder für die negative Richtung (180° gedreht)
        Cylinder negCyl = createDirectionalCylinder(color, len, radius, rotationAxis, angleDeg + 180, axisDir, -1);

        axisGroup.getChildren().addAll(posCyl, negCyl);
        return axisGroup;
    }

    private static Cylinder createDirectionalCylinder(Color color, double len, double radius,
                                                      Point3D rotationAxis, double angleDeg,
                                                      Axis axisDir, int directionSign) {
        Cylinder cyl = new Cylinder(radius, len);
        cyl.setMaterial(new PhongMaterial(color));
        cyl.setCullFace(CullFace.NONE);

        // Rotation anwenden
        if (rotationAxis != null) {
            cyl.setRotationAxis(rotationAxis);
            cyl.setRotate(angleDeg);
        }

        // Positionierung basierend auf Richtung (+1 oder -1)
        double half = len * 0.5;
        switch (axisDir) {
            case X -> cyl.setTranslateX(directionSign * half);
            case Y -> cyl.setTranslateY(directionSign * half);
            case Z -> cyl.setTranslateZ(directionSign * half);
        }
        return cyl;
    }
}

