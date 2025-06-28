package org.ea.controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ea.constant.Arguments;
import org.ea.utiltities.GeometryUtils;
import org.ea.view.MainScene;

public class StageController {
    private final Stage mainStage;
    private final Application.Parameters parameters;
    private MainSceneController ctrl;

    public StageController(Stage stage, Application.Parameters parameters) {
        this.mainStage = stage;
        this.parameters = parameters;
    }

    public Application.Parameters getParameters() {
        return parameters;
    }

    public void setTitle(String title) {
        this.getMainStage().setTitle(title);
    }

    public void handleStage() {

        /* ---------- Fenster‐Titel ---------- */
        this.setTitle("Model Viewer Application");

        /* ---------- Szene aufbauen ---------- */
        MainScene mainScene = new MainScene(
                1280, 720,
                GeometryUtils.createPolyhedronFromFile(
                        this.getParameters()
                                .getRaw()
                                .get(Arguments.FILE_NAME_ARGUMENT)
                )
        );

        /* ---------- Controller für 3-D-Viewport ---------- */
        ctrl =
                new MainSceneController(mainScene); // SubScene!

        ctrl.handleSceneInterAction();          // Hotkeys / Click-Highlight usw.

        /* ---------- Szene anzeigen ---------- */
        this.startScene(mainScene);             // ← jetzt MainScene, nicht ModelScene
    }

    public MainSceneController getMainSceneController() {
        return ctrl;
    }

    public void startScene(Scene scene) {
        this.getMainStage().setScene(scene);
        this.getMainStage().show();
    }

    public Stage getMainStage() {
        return mainStage;
    }
}
