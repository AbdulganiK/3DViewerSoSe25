package org.ea.controller;

import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Provides rotation and translation control over a single 3D mesh node using Euler angles and linear offsets.
 *
 * @precondition The target node must be a valid JavaFX 3D object capable of accepting transforms.
 * @postcondition The node is augmented with three {@link Rotate} and one {@link Translate} transform in standard order.
 */
public final class MeshController {

    private final Node mesh;

    private final Rotate rotX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotY = new Rotate(0, Rotate.Y_AXIS);
    private final Rotate rotZ = new Rotate(0, Rotate.Z_AXIS);
    private final Translate translate = new Translate();

    /**
     * Constructs a controller for a single 3D mesh node and applies rotation and translation transforms.
     *
     * @param mesh the JavaFX node to control
     * @precondition {@code mesh} is not null
     * @postcondition Mesh receives four transforms: Yaw, Pitch, Roll, and Translate (in that order)
     */
    public MeshController(Node mesh) {
        this.mesh = mesh;
        mesh.getTransforms().addAll(rotY, rotX, rotZ, translate);
    }

    /**
     * Applies incremental Euler-angle rotation (in degrees) around all three axes.
     *
     * @param dYaw change in Y-axis rotation
     * @param dPitch change in X-axis rotation
     * @param dRoll change in Z-axis rotation
     * @precondition Mesh has been initialized and attached to the scene graph
     * @postcondition The rotation angles are cumulatively increased
     */
    public void rotateBy(double dYaw, double dPitch, double dRoll) {
        rotY.setAngle(rotY.getAngle() + dYaw);
        rotX.setAngle(rotX.getAngle() + dPitch);
        rotZ.setAngle(rotZ.getAngle() + dRoll);
    }

    /**
     * Rotates the mesh by a specified number of degrees around a single axis.
     *
     * @param axis one of "X", "Y", or "Z" (case-insensitive)
     * @param degrees the angle to rotate in degrees
     * @throws IllegalArgumentException if an unsupported axis is given
     * @precondition Axis must be a valid Cartesian direction
     * @postcondition The corresponding rotation angle is incremented
     */
    public void rotateBy(String axis, double degrees) {
        switch (axis.toUpperCase()) {
            case "X": rotX.setAngle(rotX.getAngle() + degrees); break;
            case "Y": rotY.setAngle(rotY.getAngle() + degrees); break;
            case "Z": rotZ.setAngle(rotZ.getAngle() + degrees); break;
            default: throw new IllegalArgumentException("Invalid axis: " + axis + " (allowed: X, Y, Z)");
        }
    }

    /**
     * Translates the mesh by a specified offset in each axis.
     *
     * @param dx x offset
     * @param dy y offset
     * @param dz z offset
     * @precondition Mesh is transformable and visible
     * @postcondition Mesh position is shifted by the specified values
     */
    public void moveBy(double dx, double dy, double dz) {
        translate.setX(translate.getX() + dx);
        translate.setY(translate.getY() + dy);
        translate.setZ(translate.getZ() + dz);
    }

    /**
     * Translates the mesh along a single axis.
     *
     * @param axis one of "X", "Y", or "Z" (case-insensitive)
     * @param offset translation amount
     * @throws IllegalArgumentException if an unsupported axis is given
     * @precondition Axis is valid and node is initialized
     * @postcondition Mesh is moved along the specified axis
     */
    public void moveBy(String axis, double offset) {
        switch (axis.toUpperCase()) {
            case "X" -> translate.setX(translate.getX() + offset);
            case "Y" -> translate.setY(translate.getY() + offset);
            case "Z" -> translate.setZ(translate.getZ() + offset);
            default   -> throw new IllegalArgumentException("Axis must be X, Y or Z");
        }
    }

    /**
     * Sets absolute Euler angles in degrees, replacing all previous rotation.
     *
     * @param yaw Y-axis angle
     * @param pitch X-axis angle
     * @param roll Z-axis angle
     * @precondition None
     * @postcondition Rotation values are overwritten with new angles
     */
    public void setRotation(double yaw, double pitch, double roll) {
        rotY.setAngle(yaw);
        rotX.setAngle(pitch);
        rotZ.setAngle(roll);
    }

    /**
     * Moves the mesh to a specific coordinate.
     *
     * @param x new X position
     * @param y new Y position
     * @param z new Z position
     * @precondition None
     * @postcondition Mesh is moved to the new location absolutely
     */
    public void setPosition(double x, double y, double z) {
        translate.setX(x);
        translate.setY(y);
        translate.setZ(z);
    }

    /**
     * Resets all transformations to their identity state.
     *
     * @precondition None
     * @postcondition Rotation and translation are set to 0
     */
    public void reset() {
        setRotation(0, 0, 0);
        setPosition(0, 0, 0);
    }
}
