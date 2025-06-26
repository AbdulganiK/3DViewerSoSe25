package org.ea.controller;

import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import org.ea.view.ModelScene;

public class MainSceneController {

    private boolean highlighted = false;
    private PhongMaterial baseMat;
    private DrawMode      baseDrawMode = DrawMode.FILL; // Standard-Default
    private final PhongMaterial hlMat =
            new PhongMaterial(Color.YELLOW);

    ModelScene modelScene;
    public MainSceneController(ModelScene modelScene) {
        this.modelScene = modelScene;
    }

    public ModelScene getModelScene() {
        return modelScene;
    }

    public void handleSceneInterAction() {
        this.getModelScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                System.out.println("ESC gedrückt – App schließen");
                Platform.exit();
            }
        });

        this.handleOnCLickBehaviorOnSTLObject();
    }

    public void handleOnCLickBehaviorOnSTLObject() {

        MeshView meshView = (MeshView) modelScene.getMeshView();
        if (meshView == null) return;

        meshView.setPickOnBounds(true);

        // Originalzustand einmal puffern
        if (baseMat == null) {
            // kann null sein → späteres Zurücksetzen auf DEFAULT-Material
            if (meshView.getMaterial() instanceof PhongMaterial pm)
                baseMat = pm;
            baseDrawMode = meshView.getDrawMode();
        }

        meshView.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.PRIMARY || !e.isStillSincePress()) return;

            if (!highlighted) {                         // ➜ Highlight AN
                meshView.setDrawMode(DrawMode.LINE);
                meshView.setMaterial(hlMat);
                highlighted = true;
            } else {                                    // ➜ Highlight AUS
                meshView.setDrawMode(baseDrawMode);

                // ursprüngliches oder ein neutrales Material zurücklegen
                if (baseMat != null) {
                    meshView.setMaterial(baseMat);
                } else {
                    meshView.setMaterial(new PhongMaterial(Color.LIGHTGRAY));
                }
                highlighted = false;
            }
            e.consume();
        });
    }

    public void showTransformerMenu() {

    }
}
