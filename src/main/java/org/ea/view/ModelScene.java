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
 * Reiner 3-D-Viewport – jetzt als {@link SubScene}.
 * Die komplette Transformations- und Fit-Logik blieb unverändert.
 */
public final class ModelScene extends SubScene {          // Scene → SubScene

    /* ---------------- Welt & Kamera ---------------- */
    private final Group world = new Group();
    private final PerspectiveCamera cam = new PerspectiveCamera(true);

    /* Orbit-Rotationen */
    private double anchorX, anchorY;
    private final Rotate rotX = new Rotate(0, Rotate.X_AXIS);
    private final Rotate rotY = new Rotate(0, Rotate.Y_AXIS);
    private final GlobalAxes3D axes;

    private Node meshView;                    // aktuell geladenes STL-Mesh

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

        /* Kamera */
        setCamera(cam);
        cam.getTransforms().addAll(rotY, rotX);
        cam.setNearClip(0.1);
        cam.setFarClip(10_000);

        Lights.addDefaultLights(world);
        enableMouseOrbit();

        if (model != null) {
            Node mesh = new MeshFactory().buildMeshView(model.getSurfaces());
            setMesh(mesh);
        }
    }

    /* ----------- öffentlich ----------- */

    public Node getMeshView()        { return meshView; }
    public PerspectiveCamera getCam(){ return cam;      }
    public Group getWorld()          { return world;    }

    public void resetView() {
        rotX.setAngle(-25);
        rotY.setAngle(-25);
        cam.setTranslateX(300);
        cam.setTranslateY(-300);
        cam.setTranslateZ(-500);
    }

    /* ----------- Model-Fit & Laden ----------- */

    public void setMesh(Node mesh) {
        if (mesh instanceof MeshView mv) mv.setCullFace(CullFace.BACK);

        mesh.getTransforms().add(new Rotate(90, Rotate.X_AXIS));      // Z-up → Y-up
        world.getChildren().setAll(mesh);
        meshView = mesh;

        /* Nach Layout-Pass */
        Platform.runLater(() -> fitAndCenter(mesh));
    }

    private void fitAndCenter(Node mesh) {
        Bounds b  = mesh.getBoundsInParent();
        double cx = (b.getMinX() + b.getMaxX()) * .5;
        double cy = (b.getMinY() + b.getMaxY()) * .5;
        double cz = (b.getMinZ() + b.getMaxZ()) * .5;
        mesh.getTransforms().add(new Translate(-cx, -cy, -cz));

        double max = Math.max(Math.max(b.getWidth(), b.getHeight()), b.getDepth());
        if (max > 0)
            mesh.getTransforms().add(new Scale(200 / max, 200 / max, 200 / max));

        rotX.setAngle(-25);
        rotY.setAngle(-25);
        cam.setTranslateX(300);
        cam.setTranslateY(-300);
        cam.setTranslateZ(-500);
    }

    /* ----------- Maus-Orbit ----------- */

    private void enableMouseOrbit() {
        setOnMousePressed(e -> { anchorX = e.getSceneX(); anchorY = e.getSceneY(); });
        setOnMouseDragged(e -> {
            rotX.setAngle(rotX.getAngle() - (e.getSceneY() - anchorY));
            rotY.setAngle(rotY.getAngle() + (e.getSceneX() - anchorX));
            anchorX = e.getSceneX();
            anchorY = e.getSceneY();
        });
    }

    /* ----------- internes Licht-Setup ----------- */
    private static final class Lights {
        static void addDefaultLights(Group g) {
            PointLight key = new PointLight(Color.WHITE);
            key.setTranslateX(-300); key.setTranslateY(-300); key.setTranslateZ(-300);
            g.getChildren().addAll(key, new AmbientLight(Color.color(0.4, 0.4, 0.4)));
        }
    }

    public void toggleAxis() {
        this.axes.setVisible(!axes.isVisible());
    }
}
