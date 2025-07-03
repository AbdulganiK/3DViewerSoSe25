package org.ea.view;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.*;
import org.ea.model.Polyhedron;
import org.ea.utiltities.MeshFactory;

/**
 * Pure 3‑D viewport implemented as a JavaFX {@link SubScene}.
 *
 * @precondition JavaFX runtime is initialised and width/height are positive values.
 * @postcondition A fully configured 3‑D sub‑scene with camera, grid, axes, and optional mesh is ready for display.
 */
public final class ModelScene extends SubScene {

    /* ---------------- World & Camera ---------------- */
    private final Group world = new Group();
    private final PerspectiveCamera cam = new PerspectiveCamera(true);

    /* Orbit rotation helpers */
    private double anchorX, anchorY;
    private final Rotate rotX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotY = new Rotate(0, Rotate.Y_AXIS);
    private final GlobalAxes3D axes;

    private Node meshView; // currently loaded STL mesh

    /**
     * Builds a 3‑D sub‑scene with default lights, grid floor, global axes and—if provided—a mesh.
     *
     * @param w     scene width in pixels (must be > 0)
     * @param h     scene height in pixels (must be > 0)
     * @param model optional {@link Polyhedron} to visualise; may be {@code null}
     *
     * @precondition {@code w > 0 && h > 0}
     * @postcondition Scene graph is populated; if {@code model != null} it is rendered and centred.
     */
    public ModelScene(double w, double h, Polyhedron model) {
        super(new Group(), w, h, true, SceneAntialiasing.BALANCED);
        setFill(Color.LIGHTGRAY);

        axes = new GlobalAxes3D(1_800, 2);
        Group root3d = (Group) getRoot();
        root3d.getChildren().addAll(
                world,
                axes,
                new GridFloor3D(1_800, 50, 0.2)
        );
        root3d.setDepthTest(DepthTest.ENABLE);

        /* Camera */
        setCamera(cam);
        cam.getTransforms().addAll(rotY, rotX);
        cam.setNearClip(0.1);
        cam.setFarClip(10_000);

        Lights.addDefaultLights(world);
        //enableMouseOrbit(); // left disabled by default

        if (model != null) {
            Node mesh = new MeshFactory().buildMeshView(model.getSurfaces());
            setMesh(mesh);
        }
    }



    public Node getMeshView() { return meshView; }


    public PerspectiveCamera getCam() { return cam; }


    public Group getWorld() { return world; }

    /**
     * Restores the camera to the default orbit view.
     *
     * @precondition Scene and camera are initialised.
     * @postcondition Camera rotation and translation are reset to starting values.
     */
    public void resetView() {
        rotX.setAngle(-25);
        rotY.setAngle(-25);
        cam.setTranslateX(300);
        cam.setTranslateY(-300);
        cam.setTranslateZ(-500);
    }

    /**
     * Replaces the current mesh with a new node and schedules centring/scaling.
     *
     * @param mesh the new mesh node; must not be {@code null}
     *
     * @precondition {@code mesh != null}
     * @postcondition Mesh is added to the world and will be centred after JavaFX layout pass.
     */
    public void setMesh(Node mesh) {
        if (mesh instanceof MeshView mv) mv.setCullFace(CullFace.BACK);

        mesh.getTransforms().add(new Rotate(90, Rotate.X_AXIS)); // convert Z‑up to Y‑up
        world.getChildren().setAll(mesh);
        meshView = mesh;

        /* defer centring until bounds are valid */
        Platform.runLater(() -> fitAndCenter(mesh));
    }

    /**
     * Recentres the mesh at the origin and scales it to fit a 400 × 400 × 400 cube.
     *
     * @param mesh the node to centre
     *
     * @precondition {@code mesh} is part of the scene graph and has valid bounds.
     * @postcondition Mesh is translated to origin and uniformly scaled; camera reset.
     */
    private void fitAndCenter(Node mesh) {
        Bounds b = mesh.getBoundsInParent();
        double cx = (b.getMinX() + b.getMaxX()) * 0.5;
        double cy = (b.getMinY() + b.getMaxY()) * 0.5;
        double cz = (b.getMinZ() + b.getMaxZ()) * 0.5;
        mesh.getTransforms().add(new Translate(-cx, -cy, -cz));

        double max = Math.max(Math.max(b.getWidth(), b.getHeight()), b.getDepth());
        if (max > 0) {
            mesh.getTransforms().add(new Scale(200 / max, 200 / max, 200 / max));
        }

        resetView();
    }

    /**
     * Enables simple orbit controls by dragging the mouse (disabled by default).
     *
     * @precondition Should only be called once; event handlers are additive.
     * @postcondition Scene responds to mouse drag events to update camera angles.
     */
    private void enableMouseOrbit() {
        setOnMousePressed(e -> { anchorX = e.getSceneX(); anchorY = e.getSceneY(); });
        setOnMouseDragged(e -> {
            rotX.setAngle(rotX.getAngle() - (e.getSceneY() - anchorY));
            rotY.setAngle(rotY.getAngle() + (e.getSceneX() - anchorX));
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
        });
    }

    /** Utility class for adding default key and ambient lights. */
    private static final class Lights {
        /**
         * Adds a key light and low‑level ambient light into the given group.
         *
         * @param g target group (must not be {@code null})
         *
         * @precondition {@code g != null}
         * @postcondition Lights are attached to the group for global illumination.
         */
        static void addDefaultLights(Group g) {
            PointLight key = new PointLight(Color.WHITE);
            key.setTranslateX(-300);
            key.setTranslateY(-300);
            key.setTranslateZ(-300);
            g.getChildren().addAll(key, new AmbientLight(Color.color(0.4, 0.4, 0.4)));
        }
    }

    /**
     * Toggles the visibility of the global axes helper.
     *
     * @precondition Scene initialised with {@link GlobalAxes3D}
     * @postcondition Axes toggled.
     */
    public void toggleAxis() {
        this.axes.setVisible(!axes.isVisible());
    }
}
