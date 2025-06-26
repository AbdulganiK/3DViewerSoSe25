package org.ea.controller;

import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

/**
 * Controls a single 3‑D mesh node.  The class is deliberately kept small:
 * it only knows how to **rotate** and **translate** the mesh.  All user‑ or
 * network‑level interactions will be added later.
 *
 * <p>Planned but <em>not yet implemented</em> extensions:</p>
 * <ul>
 *     <li>Mouse / touch gestures</li>
 *     <li>Context‑menu commands</li>
 *     <li>Remote control via network messages</li>
 * </ul>
 */
public final class MeshController {

    /* ────────── target node ────────── */
    private final Node mesh;

    /* ────────── base transforms ────────── */
    private final Rotate rotX = new Rotate(0, Rotate.X_AXIS);   // Pitch
    private final Rotate rotY = new Rotate(0, Rotate.Y_AXIS);   // Yaw
    private final Rotate rotZ = new Rotate(0, Rotate.Z_AXIS);   // Roll
    private final Translate translate = new Translate();        // Pan / Move

    public MeshController(Node mesh) {
        this.mesh = mesh;
        mesh.getTransforms().addAll(rotY, rotX, rotZ, translate);
    }

    /* ────────── public API ────────── */

    /**
     * Adds the given angles (deg) to the current rotation.
     */
    public void rotateBy(double dYaw, double dPitch, double dRoll) {
        rotY.setAngle(rotY.getAngle() + dYaw);
        rotX.setAngle(rotX.getAngle() + dPitch);
        rotZ.setAngle(rotZ.getAngle() + dRoll);
    }

    /**
     * Adds the given deltas to the current translation.
     */
    public void moveBy(double dx, double dy, double dz) {
        translate.setX(translate.getX() + dx);
        translate.setY(translate.getY() + dy);
        translate.setZ(translate.getZ() + dz);
    }

    /**
     * Sets absolute Euler angles (deg).
     */
    public void setRotation(double yaw, double pitch, double roll) {
        rotY.setAngle(yaw);
        rotX.setAngle(pitch);
        rotZ.setAngle(roll);
    }

    /**
     * Sets absolute position.
     */
    public void setPosition(double x, double y, double z) {
        translate.setX(x);
        translate.setY(y);
        translate.setZ(z);
    }

    /**
     * Resets all transforms to identity.
     */
    public void reset() {
        setRotation(0, 0, 0);
        setPosition(0, 0, 0);
    }

}

