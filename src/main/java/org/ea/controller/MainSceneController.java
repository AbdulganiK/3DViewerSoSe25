package org.ea.controller;

import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import org.ea.view.MainScene;
import org.ea.view.ModelControlPane;
import org.ea.view.ModelScene;

/**
 * Controller für {@link MainScene}.  <br>
 * Einzige Änderung: neues, besser sichtbares Highlighting (halbtransparente
 * Flächen + schwarze Gitter‑Bounding‑Box). Alle Methodennamen bleiben gleich.
 */
public class MainSceneController {

    private final MainScene mainScene;                       // gesamte Scene

    private boolean       highlighted   = false;
    private PhongMaterial baseMat;                           // ursprüngliches Material
    private DrawMode      baseDrawMode = DrawMode.FILL;

    /* Bounding‑Box, die wir zum Hervorheben hinzufügen/entfernen */
    private Box highlightBox;

    /* ------------------------------------------------ Konstruktor */
    public MainSceneController(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    /* ------------------------------------------------ Getter */
    public MainScene getMainScene()   { return mainScene; }
    public ModelScene getModelScene() { return mainScene.getModelSubScene(); }

    /* ------------------------------------------------ Ursprüngliche API */

    public void handleSceneInterAction() {
        // ESC → Anwendung schließen
        mainScene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.out.println("ESC gedrückt – App schließen");
                Platform.exit();
            }
        });
        handleOnCLickBehaviorOnSTLObject();
        handleColorSelect();
        this.handleOpacitySelect();
    }

    public void handleColorSelect() {
        ModelControlPane cp = mainScene.getModelControlPane();
        if (cp == null) return;

        MeshView mv = (MeshView) getModelScene().getMeshView();
        if (mv == null) return;

        // Material sicherstellen
        if (!(mv.getMaterial() instanceof PhongMaterial)) {
            mv.setMaterial(new PhongMaterial(Color.LIGHTGRAY));
        }
        PhongMaterial mat = (PhongMaterial) mv.getMaterial();
        mat.setDiffuseColor(cp.getColorPicker().getValue());

        // Farb‑Listener
        cp.getColorPicker().valueProperty().addListener((obs, oldC, newC) -> {
            mat.setDiffuseColor(newC);
            baseMat = new PhongMaterial(newC); // Basisfarbe mitziehen
        });
    }

    /**
     * Klick‑Highlighting: halbtransparente Flächen + schwarze Drahtgitter‑Box.
     */
    public void handleOnCLickBehaviorOnSTLObject() {
        ModelScene modelScene = getModelScene();
        if (modelScene == null) return;

        MeshView meshView = (MeshView) modelScene.getMeshView();
        if (meshView == null) return;

        meshView.setPickOnBounds(true);

        // Ursprungsmaterial & DrawMode merken (einmalig)
        if (baseMat == null && meshView.getMaterial() instanceof PhongMaterial pm) {
            baseMat = pm;
        }
        baseDrawMode = meshView.getDrawMode();

        meshView.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY || !e.isStillSincePress()) return;

            if (!highlighted) {                // ── Highlight AN ──
                mainScene.getModelControlPane().setPanelVisibility(true);

                // 1) Flächen halbtransparent machen
                Color base = (baseMat != null ? baseMat.getDiffuseColor() : Color.LIGHTGRAY);
                PhongMaterial translucent = new PhongMaterial(Color.color(base.getRed(), base.getGreen(), base.getBlue(), 0.25));
                meshView.setMaterial(translucent);
                meshView.setDrawMode(DrawMode.FILL);
                meshView.setCullFace(CullFace.NONE);

                // 2) Schwarze Drahtgitter‑Bounding‑Box
                Bounds b = meshView.getBoundsInParent();
                highlightBox = new Box(b.getWidth()*1.04, b.getHeight()*1.04, b.getDepth()*1.04);
                highlightBox.setDrawMode(DrawMode.LINE);
                highlightBox.setCullFace(CullFace.NONE);
                highlightBox.setMaterial(new PhongMaterial(Color.rgb(0,0,0,0.8)));
                highlightBox.setTranslateX(b.getMinX()+b.getWidth()/2);
                highlightBox.setTranslateY(b.getMinY()+b.getHeight()/2);
                highlightBox.setTranslateZ(b.getMinZ()+b.getDepth()/2);

                // ▶️ Bounding‑Box soll NICHT selbst anklickbar sein
                highlightBox.setMouseTransparent(true);
                highlightBox.setDrawMode(DrawMode.LINE);
                highlightBox.setCullFace(CullFace.NONE);
                highlightBox.setMaterial(new PhongMaterial(Color.rgb(0,0,0,0.8)));
                highlightBox.setTranslateX(b.getMinX()+b.getWidth()/2);
                highlightBox.setTranslateY(b.getMinY()+b.getHeight()/2);
                highlightBox.setTranslateZ(b.getMinZ()+b.getDepth()/2);

                ((Group) modelScene.getRoot()).getChildren().add(highlightBox);

                highlighted = true;
            } else {                           // ── Highlight AUS ──
                mainScene.getModelControlPane().setPanelVisibility(false);

                // Ursprungszustände zurück
                meshView.setDrawMode(baseDrawMode);
                meshView.setMaterial(baseMat != null ? baseMat : new PhongMaterial(Color.LIGHTGRAY));
                meshView.setCullFace(CullFace.BACK);

                if (highlightBox != null) {
                    ((Group) modelScene.getRoot()).getChildren().remove(highlightBox);
                    highlightBox = null;
                }
                highlighted = false;
            }
            e.consume();
        });
    }

    public void handleOpacitySelect() {
        ModelControlPane controlPane = mainScene.getModelControlPane();
        if (controlPane == null) return;

        MeshView meshView = (MeshView) getModelScene().getMeshView();
        if (meshView == null) return;

        // Sicherstellen, dass ein PhongMaterial vorhanden ist
        if (!(meshView.getMaterial() instanceof PhongMaterial)) {
            meshView.setMaterial(new PhongMaterial(Color.LIGHTGRAY));
        }

        // Listener für Opacity-Änderungen
        controlPane.getOpacitySlider().valueProperty().addListener((obs, oldVal, newVal) -> {
            PhongMaterial currentMat = (PhongMaterial) meshView.getMaterial();

            // Aktuelle Farbe mit neuer Opacity aktualisieren
            Color currentColor = currentMat.getDiffuseColor();
            Color newColor = new Color(
                    currentColor.getRed(),
                    currentColor.getGreen(),
                    currentColor.getBlue(),
                    newVal.doubleValue()
            );

            currentMat.setDiffuseColor(newColor);

            // Falls im Highlight-Modus: Auch das transparente Material aktualisieren
            if (highlighted) {
                Color translucentColor = Color.color(
                        currentColor.getRed(),
                        currentColor.getGreen(),
                        currentColor.getBlue(),
                        newVal.doubleValue() * 0.25 // Halbtransparent im Highlight-Modus
                );
                currentMat.setDiffuseColor(translucentColor);
            }

            // Basismaterial aktualisieren (für den Fall, dass Highlight ausgeschaltet wird)
            if (baseMat != null) {
                baseMat.setDiffuseColor(newColor);
            }
        });

        // Initialen Wert setzen
        double initialOpacity = controlPane.getOpacitySlider().getValue();
        Color initialColor = ((PhongMaterial) meshView.getMaterial()).getDiffuseColor();
        Color adjustedColor = new Color(
                initialColor.getRed(),
                initialColor.getGreen(),
                initialColor.getBlue(),
                initialOpacity
        );
        ((PhongMaterial) meshView.getMaterial()).setDiffuseColor(adjustedColor);
    }
}
