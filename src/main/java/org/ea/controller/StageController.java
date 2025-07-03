package org.ea.controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ea.constant.Arguments;
import org.ea.utiltities.GeometryUtils;
import org.ea.view.MainScene;

/**
 * Controls the lifecycle and setup of the main application window (JavaFX stage).
 *
 * @precondition A {@link Stage} and {@link Application.Parameters} must be provided.
 * @postcondition Allows initialization and launching of the main 3-D scene with user input.
 */
public class StageController {
    private final Stage mainStage;
    private final Application.Parameters parameters;
    private MainSceneController ctrl;

    /**
     * Creates a controller for managing the JavaFX primary stage.
     *
     * @param stage the JavaFX stage to control
     * @param parameters the raw command-line parameters
     * @precondition Both arguments are non-null and properly initialized
     * @postcondition Stage and parameters are retained for later use
     */
    public StageController(Stage stage, Application.Parameters parameters) {
        this.mainStage = stage;
        this.parameters = parameters;
    }

    /**
     * Returns the command-line parameters.
     *
     * @return the stored {@link Application.Parameters}
     * @precondition None
     * @postcondition Parameters are returned unchanged
     */
    public Application.Parameters getParameters() {
        return parameters;
    }

    /**
     * Sets the window title of the primary stage.
     *
     * @param title the new window title
     * @precondition Stage is initialized
     * @postcondition The stage title is updated
     */
    public void setTitle(String title) {
        this.getMainStage().setTitle(title);
    }

    /**
     * Handles the full initialization process of the main scene and controller.
     * This includes loading geometry, creating the scene, wiring controls, and displaying the stage.
     *
     * @precondition Parameters contain a valid STL file name as the first argument
     * @postcondition The main 3-D scene is shown with all interaction handlers ready
     */
    public void handleStage() {
        this.setTitle("Model Viewer Application");

        MainScene mainScene = new MainScene(
                1280, 720,
                GeometryUtils.createPolyhedronFromFile(
                        this.getParameters()
                                .getRaw()
                                .get(Arguments.FILE_NAME_ARGUMENT)
                )
        );

        ctrl = new MainSceneController(mainScene);
        ctrl.handleSceneInterAction();

        this.startScene(mainScene);
    }

    /**
     * Returns the controller managing the 3-D view.
     *
     * @return {@link MainSceneController} instance after scene initialization
     * @precondition {@link #handleStage()} has been called
     * @postcondition Provides access to input and view interaction logic
     */
    public MainSceneController getMainSceneController() {
        return ctrl;
    }

    /**
     * Displays a JavaFX scene in the main window.
     *
     * @param scene the scene to show
     * @precondition The stage and scene are non-null and initialized
     * @postcondition The scene becomes visible on screen
     */
    public void startScene(Scene scene) {
        this.getMainStage().setScene(scene);
        this.getMainStage().show();
    }

    /**
     * Returns the primary JavaFX stage.
     *
     * @return the stored stage reference
     * @precondition None
     * @postcondition The same stage is returned without side effects
     */
    public Stage getMainStage() {
        return mainStage;
    }
}
