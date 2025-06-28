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
import org.ea.view.MainScene;
import org.ea.view.ModelControlPane;
import org.ea.view.ModelScene;

/**
 * Controller für {@link MainScene}.
 * <p>
 * - Highlight: halbtransparente Flächen + schwarze Gitter-Bounding-Box <br>
 * - Gizmos: 3-D-Achsensymbol zum Verschieben & Torus-Ring zum Rotieren <br>
 * - Maus-Gesten: Drag auf Gizmo verschiebt bzw. rotiert das Modell
 */
public class MainSceneController {

    /* ------------------------------------------------ Grundreferenzen */
    private final MainScene mainScene;
    private MeshController meshController;


    /* ------------------------------------------------ Highlight-State */
    private boolean       highlighted   = false;
    private PhongMaterial baseMat;
    private DrawMode      baseDrawMode  = DrawMode.FILL;
    private Box           highlightBox;

    /* ------------------------------------------------ Gizmos */
    private Group   moveGizmo;   // RGB-Achsen
    private MeshView rotateGizmo; // dünner Torus

    /* ------------------------------------------------ Drag-Speicher */
    private double lastMouseX, lastMouseY;
    private String activeAxis = "";     // "X","Y","Z" – bestimmt Übersetzung


    /* ------------------------------------------------ Konstruktor */
    public MainSceneController(MainScene mainScene) { this.mainScene = mainScene; }

    /* ------------------------------------------------ Bequeme Getter */
    public MainScene  getMainScene()  { return mainScene; }
    private ModelScene getModelScene() { return mainScene.getModelSubScene(); }
    private MeshView   getMeshView()   {
        ModelScene ms = getModelScene(); return (ms == null) ? null : (MeshView) ms.getMeshView();
    }

    /* ======================================================================
       ÖFFENTLICHE INITIALISIERUNG
       ====================================================================== */
    public void handleSceneInterAction() {
        /* ESC beendet Anwendung */
        mainScene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ESCAPE) Platform.exit();
        });

        /* Highlighting einschalten */
        handleOnCLickBehaviorOnSTLObject();

        /* Control-Pane-Bindings */
        handleColorSelect();
        handleOpacitySelect();

        /* Gizmo-Gesten */
        moveModelOnAxisWithMouse();
        rotateModelWithMouse();
    }

    /* ======================================================================
       COLOR & OPACITY
       ====================================================================== */
    public void handleColorSelect() {
        ModelControlPane cp = mainScene.getModelControlPane();
        MeshView mv = getMeshView();
        if (cp == null || mv == null) return;

        PhongMaterial mat = ensurePhongMaterial(mv);
        mat.setDiffuseColor(cp.getColorPicker().getValue());

        cp.getColorPicker().valueProperty().addListener((o, oldC, newC) -> {
            mat.setDiffuseColor(newC);
            baseMat = new PhongMaterial(newC);          // Basisfarbe aktualisieren
        });
    }

    public void handleOpacitySelect() {
        ModelControlPane cp = mainScene.getModelControlPane();
        MeshView mv = getMeshView();
        if (cp == null || mv == null) return;

        cp.getOpacitySlider().valueProperty().addListener((o, oldV, newV) -> {
            double a = newV.doubleValue();                 // gewünschte Deckkraft

            // aktuelles – ggf. Highlight-Material – nehmen
            PhongMaterial mat = ensurePhongMaterial(mv);
            Color base = mat.getDiffuseColor();

            // wenn Highlight aktiv: nur 25 % der Slider-Opacity
            double alpha = highlighted ? a * 0.25 : a;
            mat.setDiffuseColor(
                    new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha));

            /*  Basis-Material nachziehen, damit es beim Zurückschalten stimmt  */
            if (baseMat != null) {
                baseMat.setDiffuseColor(
                        new Color(base.getRed(), base.getGreen(), base.getBlue(), a));
            }
        });

        /* Initiale Anwendung */
        double init = cp.getOpacitySlider().getValue();
        PhongMaterial mat0 = ensurePhongMaterial(mv);
        Color c0 = mat0.getDiffuseColor();
        mat0.setDiffuseColor(new Color(c0.getRed(), c0.getGreen(), c0.getBlue(), init));
        if (baseMat != null) {                             // Basis gleichziehen
            baseMat.setDiffuseColor(
                    new Color(c0.getRed(), c0.getGreen(), c0.getBlue(), init));
        }
    }

    /* ======================================================================
       HIGHLIGHTING
       ====================================================================== */
    public void handleOnCLickBehaviorOnSTLObject() {
        MeshView mv = getMeshView();
        if (mv == null) return;

        mv.setPickOnBounds(true);
        if (baseMat == null && mv.getMaterial() instanceof PhongMaterial pm) baseMat = pm;
        baseDrawMode = mv.getDrawMode();

        mv.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY || !e.isStillSincePress()) return;

            if (!highlighted) {                           /* EIN --------------- */
                mainScene.getModelControlPane().setPanelVisibility(true);
                moveGizmo.setVisible(false);
                rotateGizmo.setVisible(false);

                /* halbtransparentes Material */
                Color b = (baseMat != null) ? baseMat.getDiffuseColor() : Color.LIGHTGRAY;
                mv.setMaterial(new PhongMaterial(Color.color(b.getRed(), b.getGreen(), b.getBlue(), 0.25)));
                mv.setDrawMode(DrawMode.FILL); mv.setCullFace(CullFace.NONE);

                /* Bounding-Box */
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
            } else {                                      /* AUS --------------- */
                mainScene.getModelControlPane().setPanelVisibility(false);
                moveGizmo.setVisible(false);
                rotateGizmo.setVisible(false);
                this.getMainScene().getModelControlPane().getMoveBtn().setSelected(false);
                this.getMainScene().getModelControlPane().getRotateBtn().setSelected(false);

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

    /* ======================================================================
       G I Z M O  &  M A U S – G E S T E N
       ====================================================================== */
    public void moveModelOnAxisWithMouse() {
        MeshView mv = getMeshView(); if (mv == null) return;
        if (meshController == null) meshController = new MeshController(mv);

        ModelControlPane cp = mainScene.getModelControlPane();
        if (cp == null) return;
        ToggleButton moveBtn   = cp.getMoveBtn();
        ToggleButton rotateBtn = cp.getRotateBtn();

        if (moveGizmo == null) {
            moveGizmo = createMoveGizmo(mv.getBoundsInParent());
            ((Group) getModelScene().getRoot()).getChildren().add(moveGizmo);
        }

        // Sichtbarkeiten initial
        moveGizmo.setVisible(moveBtn.isSelected());
        if (rotateGizmo != null) rotateGizmo.setVisible(rotateBtn.isSelected());

        // Listener für Umschalten
        moveBtn.selectedProperty().addListener((o, oldSel, sel) -> {
            moveGizmo.setVisible(sel);
            if (rotateGizmo != null) rotateGizmo.setVisible(rotateBtn.isSelected());
        });

        /* ───────── Drag‑Logik ───────── */
        moveGizmo.setOnMousePressed(e -> {
            storeLastMousePos(e);
            Node hit = e.getPickResult().getIntersectedNode();
            activeAxis = (hit != null && hit.getUserData() instanceof String s) ? s : "";
        });

        moveGizmo.setOnMouseDragged(e -> {
            if (!moveBtn.isSelected() || activeAxis.isEmpty()) return;

            double dx = e.getSceneX() - lastMouseX;
            double dy = e.getSceneY() - lastMouseY;
            double s  = 0.09;                  // Empfindlichkeit

            double delta = switch (activeAxis) {
                case "X" ->  dx * s;           // links / rechts
                case "Y" -> -dy * s;           // hoch / runter  (Screen-Y invertiert)
                case "Z" -> -dy * s;           // vor / zurück   (einfach an Y gekoppelt)
                default   -> 0;
            };
            meshController.moveBy(activeAxis, delta);  // neue Achsen-Variante

            storeLastMousePos(e);
            updateGizmoPositions();
        });
    }

    private void updateGizmoPositions() {
        MeshView mv = getMeshView();
        if (mv == null) return;

        Bounds bb = mv.getBoundsInParent();
        double cx = bb.getMinX() + bb.getWidth()  / 2;
        double cy = bb.getMinY() + bb.getHeight() / 2;
        double cz = bb.getMinZ() + bb.getDepth()  / 2;

        if (moveGizmo   != null) { moveGizmo  .setTranslateX(cx); moveGizmo  .setTranslateY(cy); moveGizmo  .setTranslateZ(cz); }

        if (rotateGizmo != null) { rotateGizmo.setTranslateX(cx); rotateGizmo.setTranslateY(cy); rotateGizmo.setTranslateZ(cz); }

        if (highlightBox != null) {
            highlightBox.setTranslateX(cx);
            highlightBox.setTranslateY(cy);
            highlightBox.setTranslateZ(cz);
        }
    }

    public void rotateModelWithMouse() {
        MeshView mv = getMeshView(); if (mv == null) return;
        if (meshController == null) meshController = new MeshController(mv);

        ModelControlPane cp = mainScene.getModelControlPane();
        if (cp == null) return;
        ToggleButton rotateBtn = cp.getRotateBtn();
        ToggleButton moveBtn   = cp.getMoveBtn();

        /* Gizmo einmalig bauen */
        if (rotateGizmo == null) {
            rotateGizmo = createRotateGizmo(mv.getBoundsInParent());
            ((Group) getModelScene().getRoot()).getChildren().add(rotateGizmo);
            rotateGizmo.setVisible(false);
        }

        /* Sichtbarkeit initial setzen */
        rotateGizmo.setVisible(rotateBtn.isSelected());
        if (moveGizmo != null) moveGizmo.setVisible(moveBtn.isSelected());

        /* Umschalten bei Button-Änderung */
        rotateBtn.selectedProperty().addListener((obs, oldSel, sel) -> {
            rotateGizmo.setVisible(sel);
        });

        /* Drag-Logik nur aktiv, wenn Rotate-Button an ist */
        rotateGizmo.setOnMousePressed(this::storeLastMousePos);
        rotateGizmo.setOnMouseDragged(e -> {
            if (!rotateBtn.isSelected()) return;        // kein Rotieren, wenn Button aus
            double dx = e.getSceneX() - lastMouseX;
            double dy = e.getSceneY() - lastMouseY;
            meshController.rotateBy(dx * 0.2, dy * 0.2, 0);    // Yaw / Pitch
            storeLastMousePos(e);
            updateGizmoPositions();
        });
    }

    /* ======================================================================
       H I L F S F U N K T I O N E N
       ====================================================================== */
    private void storeLastMousePos(MouseEvent e) {
        lastMouseX = e.getSceneX(); lastMouseY = e.getSceneY();
    }

    private PhongMaterial ensurePhongMaterial(MeshView mv) {
        if (mv.getMaterial() instanceof PhongMaterial pm) return pm;
        PhongMaterial mat = new PhongMaterial(Color.LIGHTGRAY);
        mv.setMaterial(mat); return mat;
    }

    /** 3-achsiges Koordinaten-Gizmo (RGB-Zylinder) */
    private Group createMoveGizmo(Bounds bb) {
        // größte Ausdehnung des Objekts
        double objMax = Math.max(Math.max(bb.getWidth(), bb.getHeight()), bb.getDepth());

        // Gizmo-Länge = 120 % dieser Ausdehnung (mindestens 50 px)
        double len   = Math.max(objMax * 1.20, 50);
        double thick = len * 0.02;                 // Achsen-Durchmesser ≈ 2 %

        Cylinder x = new Cylinder(thick, len);
        x.setMaterial(new PhongMaterial(Color.RED));
        x.setRotationAxis(Rotate.Z_AXIS);
        x.setRotate(90);
        x.setUserData("X");

        Cylinder y = new Cylinder(thick, len);
        y.setMaterial(new PhongMaterial(Color.LIME));
        y.setUserData("Y");

        Cylinder z = new Cylinder(thick, len);
        z.setMaterial(new PhongMaterial(Color.BLUE));
        z.setRotationAxis(Rotate.X_AXIS);
        z.setRotate(90);
        z.setUserData("Z");

        Group g = new Group(x, y, z);
        g.setTranslateX(bb.getMinX() + bb.getWidth()  / 2);
        g.setTranslateY(bb.getMinY() + bb.getHeight() / 2);
        g.setTranslateZ(bb.getMinZ() + bb.getDepth()  / 2);
        return g;
    }

    private MeshView createRotateGizmo(Bounds bb) {
        double majorR = Math.max(Math.max(bb.getWidth(), bb.getHeight()), bb.getDepth()) * 0.7;
        double minorR = majorR * 0.02;
        TriangleMesh m = buildTorusMesh(majorR, minorR, 40, 12);
        MeshView ring = new MeshView(m);
        ring.setDrawMode(DrawMode.LINE);
        ring.setCullFace(CullFace.NONE);
        ring.setMaterial(new PhongMaterial(Color.ORANGE));
        ring.setTranslateX(bb.getMinX()+bb.getWidth()/2);
        ring.setTranslateY(bb.getMinY()+bb.getHeight()/2);
        ring.setTranslateZ(bb.getMinZ()+bb.getDepth()/2);
        return ring;
    }

    /** primitiver Torus-Builder (reicht für Gizmo) */
    private TriangleMesh buildTorusMesh(double R, double r, int segR, int segr) {
        TriangleMesh m = new TriangleMesh();
        float[] pts = new float[(segR+1)*(segr+1)*3];
        float[] tx  = new float[(segR+1)*(segr+1)*2];
        int[] faces = new int[segR * segr * 12];
        int p=0,t=0;
        for (int i=0;i<=segR;i++){
            double u = 2*Math.PI*i/segR, cu=Math.cos(u), su=Math.sin(u);
            for (int j=0;j<=segr;j++){
                double v = 2*Math.PI*j/segr, cv=Math.cos(v), sv=Math.sin(v);
                pts[p++] = (float)((R+r*cv)*cu);
                pts[p++] = (float)((R+r*cv)*su);
                pts[p++] = (float)(r*sv);
                tx[t++] = (float)i/segR; tx[t++] = (float)j/segr;
            }
        }
        int f=0;
        for(int i=0;i<segR;i++){
            for(int j=0;j<segr;j++){
                int p0=i*(segr+1)+j, p1=(i+1)*(segr+1)+j,
                        p2=p1+1, p3=p0+1;
                faces[f++]=p0; faces[f++]=p0;
                faces[f++]=p1; faces[f++]=p1;
                faces[f++]=p2; faces[f++]=p2;
                faces[f++]=p0; faces[f++]=p0;
                faces[f++]=p2; faces[f++]=p2;
                faces[f++]=p3; faces[f++]=p3;
            }
        }
        m.getPoints().setAll(pts);
        m.getTexCoords().setAll(tx);
        m.getFaces().setAll(faces);
        return m;
    }


}
