package org.ea.view;

import javafx.application.Application;
import javafx.stage.Stage;
import org.ea.Main;
import org.ea.constant.Arguments;
import org.ea.controller.StageController;
import org.ea.utiltities.GeometryUtils;
import org.ea.utiltities.Server;

public class STLViewerApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StageController stageController = new StageController(primaryStage, this.getParameters());
        Thread thread = new Thread(new Server(9000, stageController));
        thread.start();
        stageController.handleStage();

    }

}
