package org.ea.view;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

/**
 * Simple XYZ axes (global coordinate system) for JavaFX 3D scenes.
 * <ul>
 *   <li> X axis: Red </li>
 *   <li> Y axis: Green </li>
 *   <li> Z axis: Blue </li>
 * </ul>
 *
 * <p>All axes originate at the world origin (0/0/0). You can specify length and
 * radius upon creation, and the instance can be scaled or shown/hidden as needed.</p>
 *
 * <pre>
 * Group world = new Group();
 * world.getChildren().add(new GlobalAxes3D(300, 1.5));
 * </pre>
 *
 * @precondition Length and radius must be positive values.
 * @postcondition A 3D group of axes is constructed and added as child nodes.
 */
public final class GlobalAxes3D extends Group {

    /**
     * Constructs a global 3D axes object with default cylinder radius.
     *
     * @param length the total length of each axis
     * @precondition length > 0
     * @postcondition X and Z axes are initialized and added to the group
     */
    public GlobalAxes3D(double length) {
        this(length, 1.0);
    }

    /**
     * Constructs a global 3D axes object with specified length and cylinder radius.
     *
     * @param length the total length of each axis
     * @param radius the radius of each axis cylinder
     * @precondition length > 0 && radius > 0
     * @postcondition X and Z axes are initialized and added to the group
     */
    public GlobalAxes3D(double length, double radius) {
        getChildren().addAll(
                buildAxis(Color.rgb(255, 50, 50, 0.3), length, radius, Rotate.Z_AXIS, -90, Axis.X),
                // Y-axis intentionally disabled
                buildAxis(Color.rgb(80, 80, 255, 0.3), length, radius, Rotate.X_AXIS, -90, Axis.Z)
        );
    }

    /**
     * Internal enum to represent axis directions.
     */
    private enum Axis { X, Y, Z }

    /**
     * Builds a visual representation of a single axis as a pair of cylinders.
     *
     * @param color         the color of the axis
     * @param len           the length of the axis
     * @param radius        the radius of the cylinders
     * @param rotationAxis  the axis to rotate the cylinder around
     * @param angleDeg      rotation angle in degrees
     * @param axisDir       direction of the axis (X, Y, Z)
     * @return a group containing both positive and negative direction cylinders
     * @precondition color != null && len > 0 && radius > 0
     * @postcondition Group containing two cylinders is returned
     */
    private static Group buildAxis(Color color, double len, double radius,
                                   Point3D rotationAxis, double angleDeg, Axis axisDir) {
        Group axisGroup = new Group();

        Cylinder posCyl = createDirectionalCylinder(color, len, radius, rotationAxis, angleDeg, axisDir, 1);
        Cylinder negCyl = createDirectionalCylinder(color, len, radius, rotationAxis, angleDeg + 180, axisDir, -1);

        axisGroup.getChildren().addAll(posCyl, negCyl);
        return axisGroup;
    }

    /**
     * Creates a single directional cylinder for an axis.
     *
     * @param color         color of the cylinder
     * @param len           length of the cylinder
     * @param radius        radius of the cylinder
     * @param rotationAxis  axis to rotate around (optional)
     * @param angleDeg      angle to rotate
     * @param axisDir       which world axis this cylinder represents
     * @param directionSign 1 for positive direction, -1 for negative
     * @return configured Cylinder
     * @precondition color != null && len > 0 && radius > 0
     * @postcondition Cylinder is created, rotated, and positioned correctly
     */
    private static Cylinder createDirectionalCylinder(Color color, double len, double radius,
                                                      Point3D rotationAxis, double angleDeg,
                                                      Axis axisDir, int directionSign) {
        Cylinder cyl = new Cylinder(radius, len);
        cyl.setMaterial(new PhongMaterial(color));
        cyl.setCullFace(CullFace.NONE);

        if (rotationAxis != null) {
            cyl.setRotationAxis(rotationAxis);
            cyl.setRotate(angleDeg);
        }

        double half = len * 0.5;
        switch (axisDir) {
            case X -> cyl.setTranslateX(directionSign * half);
            case Y -> cyl.setTranslateY(directionSign * half);
            case Z -> cyl.setTranslateZ(directionSign * half);
        }

        return cyl;
    }
}
