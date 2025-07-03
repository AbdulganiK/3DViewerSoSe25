package org.ea.controller;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import org.ea.constant.Numbers;
import org.ea.constant.TextResources;
import org.ea.view.MainScene;
import org.ea.view.ModelControlPane;
import org.ea.view.ModelScene;

/**
 * Controller for {@link MainScene}. Handles user interactions, highlighting,
 * drag-based translation and rotation via gizmos, and updates to color/opacity controls.
 */
public class MainSceneController {

    /* ------------------------------------------------ Core References */
    private final MainScene mainScene;
    private MeshController meshController;

    /* ------------------------------------------------ Highlight State */
    private boolean       highlighted  = false;
    private PhongMaterial baseMat;
    private DrawMode      baseDrawMode = DrawMode.FILL;
    private Box           highlightBox;

    /* ------------------------------------------------ Gizmo Nodes */
    private Group   moveGizmo;   // three-axis translation gizmo
    private MeshView rotateGizmo; // torus‑shaped rotation gizmo

    /* ------------------------------------------------ Drag Memory */
    private double lastMouseX, lastMouseY;
    private String activeAxis = TextResources.EMPTY; // "X", "Y", or "Z"

    /**
     * Creates a controller bound to a specific {@link MainScene} instance.
     *
     * @param mainScene the main JavaFX scene containing 3‑D content and controls.
     * @precondition {@code mainScene} is non‑null and fully initialized.
     * @postcondition The controller stores {@code mainScene} for later use.
     */
    public MainSceneController(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    /**
     * Returns the managed {@link MainScene} instance.
     *
     * @return the reference passed to the constructor.
     * @precondition None.
     * @postcondition No side effects.
     */
    public MainScene getMainScene() {
        return mainScene;
    }

    /**
     * Convenience accessor for the embedded 3‑D {@link ModelScene}.
     *
     * @return the sub‑scene hosting the STL mesh or {@code null} if not loaded.
     * @precondition None.
     * @postcondition No state changes.
     */
    private ModelScene getModelScene() {
        return mainScene.getModelSubScene();
    }

    /**
     * Retrieves the {@link MeshView} that visualizes the loaded STL model.
     *
     * @return the mesh view cast from the sub‑scene, or {@code null} if absent.
     * @precondition None.
     * @postcondition Mesh state remains unchanged.
     */
    private MeshView getMeshView() {
        ModelScene ms = getModelScene();
        return (ms == null) ? null : (MeshView) ms.getMeshView();
    }

    /**
     * Sets up all event filters, listeners, and UI bindings. Call once after
     * the FXML or scene graph is fully constructed.
     *
     * @precondition The scene graph is visible and interactive.
     * @postcondition Keyboard shortcuts, color/opacity bindings, and gizmo
     *                listeners are active.
     */
    public void handleSceneInterAction() {
        // ESC closes the application
        mainScene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) Platform.exit();
        });

        handleOnCLickBehaviorOnSTLObject();
        handleColorSelect();
        handleOpacitySelect();
        moveModelOnAxisWithMouse();
        rotateModelWithMouse();
    }

    /**
     * Binds the color picker to the diffuse color of the mesh material.
     *
     * @precondition {@link ModelControlPane} and mesh exist.
     * @postcondition Material color updates live with picker changes; baseMat
     *                mirrors the selected color for highlight toggling.
     */
    public void handleColorSelect() {
        ModelControlPane cp = mainScene.getModelControlPane();
        MeshView mv = getMeshView();
        if (cp == null || mv == null) return;

        PhongMaterial mat = ensurePhongMaterial(mv);
        mat.setDiffuseColor(cp.getColorPicker().getValue());

        cp.getColorPicker().valueProperty().addListener((o, oldC, newC) -> {
            mat.setDiffuseColor(newC);
            baseMat = new PhongMaterial(newC);
        });
    }

    /**
     * Connects the opacity slider to the alpha channel of the mesh material.
     * When highlighted, the visible alpha is reduced (25 %) to maintain
     * translucency while controls are active.
     *
     * @precondition Slider, pane, and mesh are non‑null.
     * @postcondition Mesh transparency always reflects slider value adjusted by
     *                highlight state; baseMat keeps full alpha for toggling.
     */
    public void handleOpacitySelect() {
        ModelControlPane cp = mainScene.getModelControlPane();
        MeshView mv = getMeshView();
        if (cp == null || mv == null) return;

        cp.getOpacitySlider().valueProperty().addListener((o, oldV, newV) -> {
            double a = newV.doubleValue();
            PhongMaterial mat = ensurePhongMaterial(mv);
            Color base = mat.getDiffuseColor();
            double alpha = highlighted ? a * Numbers.HIGHLIGHT_VALUE : a;
            mat.setDiffuseColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha));

            if (baseMat != null) {
                baseMat.setDiffuseColor(new Color(base.getRed(), base.getGreen(), base.getBlue(), a));
            }
        });

        // Apply initial slider value
        double init = cp.getOpacitySlider().getValue();
        PhongMaterial mat0 = ensurePhongMaterial(mv);
        Color c0 = mat0.getDiffuseColor();
        mat0.setDiffuseColor(new Color(c0.getRed(), c0.getGreen(), c0.getBlue(), init));
        if (baseMat != null) {
            baseMat.setDiffuseColor(new Color(c0.getRed(), c0.getGreen(), c0.getBlue(), init));
        }
    }

    /**
     * Toggles highlight mode when the STL mesh is clicked. Highlighting makes
     * the model semi‑transparent, shows a bounding box, and reveals UI controls.
     *
     * @precondition Mesh view has pick‑on‑bounds enabled.
     * @postcondition The UI alternates between highlighted and normal states;
     *                all visual properties revert correctly when de‑highlighted.
     */
    public void handleOnCLickBehaviorOnSTLObject() {
        MeshView mv = getMeshView();
        if (mv == null) return;

        mv.setPickOnBounds(true);
        if (baseMat == null && mv.getMaterial() instanceof PhongMaterial pm) baseMat = pm;
        baseDrawMode = mv.getDrawMode();

        mv.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY || !e.isStillSincePress()) return;

            if (!highlighted) {
                // Activate highlight
                mainScene.getModelControlPane().setPanelVisibility(true);
                moveGizmo.setVisible(false);
                rotateGizmo.setVisible(false);

                Color b = (baseMat != null) ? baseMat.getDiffuseColor() : Color.LIGHTGRAY;
                mv.setMaterial(new PhongMaterial(Color.color(b.getRed(), b.getGreen(), b.getBlue(), 0.25)));
                mv.setDrawMode(DrawMode.FILL);
                mv.setCullFace(CullFace.NONE);

                Bounds bb = mv.getBoundsInParent();
                highlightBox = new Box(bb.getWidth()*1.04, bb.getHeight()*1.04, bb.getDepth()*1.04);
                highlightBox.setDrawMode(DrawMode.LINE);
                highlightBox.setCullFace(CullFace.NONE);
                highlightBox.setMaterial(new PhongMaterial(Color.rgb(0,0,0,0.8)));
                highlightBox.setTranslateX(bb.getMinX()+bb.getWidth()/2);
                highlightBox.setTranslateY(bb.getMinY()+bb.getHeight()/2);
                highlightBox.setTranslateZ(bb.getMinZ()+bb.getDepth()/2);
                highlightBox.setMouseTransparent(true);
                ((Group) getModelScene().getRoot()).getChildren().add(highlightBox);
                highlighted = true;
            } else {
                // Deactivate highlight
                mainScene.getModelControlPane().setPanelVisibility(false);
                moveGizmo.setVisible(false);
                rotateGizmo.setVisible(false);
                getMainScene().getModelControlPane().getMoveBtn().setSelected(false);
                getMainScene().getModelControlPane().getRotateBtn().setSelected(false);

                mv.setDrawMode(baseDrawMode);
                mv.setMaterial(baseMat != null ? baseMat : new PhongMaterial(Color.LIGHTGRAY));
                mv.setCullFace(CullFace.BACK);

                if (highlightBox != null) {
                    ((Group) getModelScene().getRoot()).getChildren().remove(highlightBox);
                    highlightBox = null;
                }
                highlighted = false;
            }
            e.consume();
        });
    }

    /**
     * Builds a three‑axis translation gizmo (RGB cylinders) and registers drag
     * handlers that move the mesh along the selected axis.
     *
     * @precondition Mesh and {@link MeshController} are initialised; move button
     *               exists in the control pane.
     * @postcondition Dragging the gizmo translates the STL model; gizmo
     *                visibility reflects toggle state.
     */
    public void moveModelOnAxisWithMouse() {
        MeshView mv = getMeshView();
        if (mv == null) return;
        if (meshController == null) meshController = new MeshController(mv);

        ModelControlPane cp = mainScene.getModelControlPane();
        if (cp == null) return;
        ToggleButton moveBtn = cp.getMoveBtn();
        ToggleButton rotateBtn = cp.getRotateBtn();

        if (moveGizmo == null) {
            moveGizmo = createMoveGizmo(mv.getBoundsInParent());
            ((Group) getModelScene().getRoot()).getChildren().add(moveGizmo);
        }

        moveGizmo.setVisible(moveBtn.isSelected());
        if (rotateGizmo != null) rotateGizmo.setVisible(rotateBtn.isSelected());

        moveBtn.selectedProperty().addListener((o, oldSel, sel) -> {
            moveGizmo.setVisible(sel);
            if (rotateGizmo != null) rotateGizmo.setVisible(rotateBtn.isSelected());
        });

        moveGizmo.setOnMousePressed(e -> {
            storeLastMousePos(e);
            Node hit = e.getPickResult().getIntersectedNode();
            activeAxis = (hit != null && hit.getUserData() instanceof String s) ? s : TextResources.EMPTY;
        });

        moveGizmo.setOnMouseDragged(e -> {
            if (!moveBtn.isSelected() || activeAxis.isEmpty()) return;
            double dx = e.getSceneX() - lastMouseX;
            double dy = e.getSceneY() - lastMouseY;
            double sensitivity = Numbers.SENSITIVITY;

            double delta = switch (activeAxis) {
                case TextResources.X -> dx * sensitivity;
                case TextResources.Y -> -dy * sensitivity; // screen‑Y inverted
                case TextResources.Z -> -dy * sensitivity;
                default -> 0;
            };
            meshController.moveBy(activeAxis, delta);
            storeLastMousePos(e);
            updateGizmoPositions();
        });
    }

    /**
     * Keeps gizmos and highlight box centered on the transformed mesh after
     * a translation or rotation.
     *
     * @precondition Mesh view exists and has been translated/rotated.
     * @postcondition All auxiliary nodes (gizmos, bounding box) follow the mesh
     *                so they appear anchored to it.
     */
    private void updateGizmoPositions() {
        MeshView mv = getMeshView();
        if (mv == null) return;

        Bounds bb = mv.getBoundsInParent();
        double cx = bb.getMinX() + bb.getWidth()  / 2;
        double cy = bb.getMinY() + bb.getHeight() / 2;
        double cz = bb.getMinZ() + bb.getDepth()  / 2;

        if (moveGizmo != null) {
            moveGizmo.setTranslateX(cx);
            moveGizmo.setTranslateY(cy);
            moveGizmo.setTranslateZ(cz);
        }
        if (rotateGizmo != null) {
            rotateGizmo.setTranslateX(cx);
            rotateGizmo.setTranslateY(cy);
            rotateGizmo.setTranslateZ(cz);
        }
        if (highlightBox != null) {
            highlightBox.setTranslateX(cx);
            highlightBox.setTranslateY(cy);
            highlightBox.setTranslateZ(cz);
        }
    }

    /**
     * Creates a torus‑shaped rotation gizmo and installs drag handlers that
     * rotate the model around its local X/Y axes based on mouse movement.
     *
     * @precondition Mesh, control pane, and rotate button exist.
     * @postcondition Dragging the ring rotates the STL model; visibility is
     *                governed by the rotate toggle button.
     */
    public void rotateModelWithMouse() {
        MeshView mv = getMeshView();
        if (mv == null) return;
        if (meshController == null) meshController = new MeshController(mv);

        ModelControlPane cp = mainScene.getModelControlPane();
        if (cp == null) return;
        ToggleButton rotateBtn = cp.getRotateBtn();
        ToggleButton moveBtn   = cp.getMoveBtn();

        if (rotateGizmo == null) {
            rotateGizmo = createRotateGizmo(mv.getBoundsInParent());
            ((Group) getModelScene().getRoot()).getChildren().add(rotateGizmo);
            rotateGizmo.setVisible(false);
        }

        rotateGizmo.setVisible(rotateBtn.isSelected());
        if (moveGizmo != null) moveGizmo.setVisible(moveBtn.isSelected());

        rotateBtn.selectedProperty().addListener((obs, oldSel, sel) -> rotateGizmo.setVisible(sel));

        rotateGizmo.setOnMousePressed(this::storeLastMousePos);
        rotateGizmo.setOnMouseDragged(e -> {
            if (!rotateBtn.isSelected()) return;
            double dx = e.getSceneX() - lastMouseX;
            double dy = e.getSceneY() - lastMouseY;
            meshController.rotateBy(dx * Numbers.ROTATION_VALUE, dy * Numbers.ROTATION_VALUE, 0);
            storeLastMousePos(e);
            updateGizmoPositions();
        });
    }

    /**
     * Stores the current mouse coordinates for subsequent delta calculations.
     *
     * @param e the mouse event providing screen‑space coordinates.
     * @precondition {@code e} originates from a JavaFX input handler.
     * @postcondition {@code lastMouseX} and {@code lastMouseY} mirror the event
     *                position.
     */
    private void storeLastMousePos(MouseEvent e) {
        lastMouseX = e.getSceneX();
        lastMouseY = e.getSceneY();
    }

    /**
     * Ensures that the mesh view has a {@link PhongMaterial}. Creates a new
     * one with LIGHTGRAY diffuse color if absent.
     *
     * @param mv the mesh view to examine.
     * @return an existing or newly created {@link PhongMaterial}.
     * @precondition {@code mv} is non‑null.
     * @postcondition {@code mv.getMaterial()} is guaranteed to be a
     *                {@link PhongMaterial} which is also returned.
     */
    private PhongMaterial ensurePhongMaterial(MeshView mv) {
        if (mv.getMaterial() instanceof PhongMaterial pm) return pm;
        PhongMaterial mat = new PhongMaterial(Color.LIGHTGRAY);
        mv.setMaterial(mat);
        return mat;
    }

    /**
     * Builds a three‑axis gizmo using colored cylinders.
     *
     * @param bb the bounding box of the current mesh.
     * @return a {@link Group} containing three cylinders aligned with X, Y, Z.
     * @precondition {@code bb} describes the extents of the mesh.
     * @postcondition The gizmo is centered on the mesh and tagged with userData
     *                strings ("X","Y","Z") for axis selection.
     */
    private Group createMoveGizmo(Bounds bb) {
        double objMax = Math.max(Math.max(bb.getWidth(), bb.getHeight()), bb.getDepth());
        double len    = Math.max(objMax * Numbers.OBJ_SIZE_MULTIPLICATOR, Numbers.OBJ_MAX_SIZE);
        double thick  = len * Numbers.THICKNESS_MULTIPLICATOR;

        Cylinder x = new Cylinder(thick, len);
        x.setMaterial(new PhongMaterial(Color.RED));
        x.setRotationAxis(Rotate.Z_AXIS);
        x.setRotate(Numbers.ANGLE);
        x.setUserData(TextResources.X);

        Cylinder y = new Cylinder(thick, len);
        y.setMaterial(new PhongMaterial(Color.LIME));
        y.setUserData(TextResources.Y);

        Cylinder z = new Cylinder(thick, len);
        z.setMaterial(new PhongMaterial(Color.BLUE));
        z.setRotationAxis(Rotate.X_AXIS);
        z.setRotate(Numbers.ANGLE);
        z.setUserData(TextResources.Z);

        Group g = new Group(x, y, z);
        g.setTranslateX(bb.getMinX() + bb.getWidth()  / 2);
        g.setTranslateY(bb.getMinY() + bb.getHeight() / 2);
        g.setTranslateZ(bb.getMinZ() + bb.getDepth()  / 2);
        return g;
    }

    /**
     * Generates a wire‑frame torus mesh to serve as a rotation gizmo.
     *
     * @param bb bounding box of the current STL mesh.
     * @return a {@link MeshView} visualizing the torus.
     * @precondition {@code bb} is valid and non‑null.
     * @postcondition The torus is sized around the mesh centre and rendered in
     *                ORANGE outline mode.
     */
    private MeshView createRotateGizmo(Bounds bb) {
        double majorR = Math.max(Math.max(bb.getWidth(), bb.getHeight()), bb.getDepth()) * Numbers.MAJOR_MULTIPLICATOR;
        double minorR = majorR * Numbers.THICKNESS_MULTIPLICATOR;
        TriangleMesh m = buildTorusMesh(majorR, minorR, 40, 12);
        MeshView ring = new MeshView(m);
        ring.setDrawMode(DrawMode.LINE);
        ring.setCullFace(CullFace.NONE);
        ring.setMaterial(new PhongMaterial(Color.ORANGE));
        ring.setTranslateX(bb.getMinX() + bb.getWidth()  / 2);
        ring.setTranslateY(bb.getMinY() + bb.getHeight() / 2);
        ring.setTranslateZ(bb.getMinZ() + bb.getDepth()  / 2);
        return ring;
    }

    /**
     * Constructs a low‑poly torus mesh (sufficient for gizmo visuals).
     *
     * @param R    major radius.
     * @param r    minor radius.
     * @param segR segments around the major circle.
     * @param segr segments around the minor circle.
     * @return a {@link TriangleMesh} representing the torus.
     * @precondition Radii and segment counts are positive.
     * @postcondition Mesh points, texCoords, and faces are populated.
     */
    private TriangleMesh buildTorusMesh(double R, double r, int segR, int segr) {
        TriangleMesh m = new TriangleMesh();
        float[] pts = new float[(segR + 1) * (segr + 1) * 3];
        float[] tx  = new float[(segR + 1) * (segr + 1) * 2];
        int[] faces = new int[segR * segr * 12];
        int p = 0, t = 0;
        for (int i = 0; i <= segR; i++) {
            double u = 2 * Math.PI * i / segR, cu = Math.cos(u), su = Math.sin(u);
            for (int j = 0; j <= segr; j++) {
                double v = 2 * Math.PI * j / segr, cv = Math.cos(v), sv = Math.sin(v);
                pts[p++] = (float) ((R + r * cv) * cu);
                pts[p++] = (float) ((R + r * cv) * su);
                pts[p++] = (float) (r * sv);
                tx[t++] = (float) i / segR;
                tx[t++] = (float) j / segr;
            }
        }
        int f = 0;
        for (int i = 0; i < segR; i++) {
            for (int j = 0; j < segr; j++) {
                int p0 = i * (segr + 1) + j;
                int p1 = (i + 1) * (segr + 1) + j;
                int p2 = p1 + 1;
                int p3 = p0 + 1;
                faces[f++] = p0; faces[f++] = p0;
                faces[f++] = p1; faces[f++] = p1;
                faces[f++] = p2; faces[f++] = p2;
                faces[f++] = p0; faces[f++] = p0;
                faces[f++] = p2; faces[f++] = p2;
                faces[f++] = p3; faces[f++] = p3;
            }
        }
        m.getPoints().setAll(pts);
        m.getTexCoords().setAll(tx);
        m.getFaces().setAll(faces);
        return m;
    }
}
