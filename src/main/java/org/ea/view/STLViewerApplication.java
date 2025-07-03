package org.ea.view;

import javafx.application.Application;
import javafx.stage.Stage;
import org.ea.controller.StageController;
import org.ea.utiltities.Server;

/**
 * Main JavaFX application class responsible for launching the STL viewer UI.
 * <p>
 * This class initializes the primary stage and starts the server for remote model control.
 */
public class STLViewerApplication extends Application {

    /**
     * Entry point for the JavaFX application.
     * Initializes the stage controller and starts a background thread
     * for the {@link Server} to handle incoming network commands.
     *
     * @param primaryStage the main window (stage) provided by the JavaFX runtime
     *
     * @precondition primaryStage != null
     * @postcondition UI is initialized and server thread is started
     * @throws Exception if initialization or server start fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        StageController stageController = new StageController(primaryStage, this.getParameters());
        Thread thread = new Thread(new Server(9000, stageController));
        thread.start();
        stageController.handleStage();
    }
}
