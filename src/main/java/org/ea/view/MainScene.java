package org.ea.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.ea.model.Polyhedron;
import org.ea.utiltities.GeometryUtils;
import org.ea.utiltities.MeshFactory;
import org.ea.utiltities.PolyhedronFactory;

import java.io.File;

/**
 * Main application window scene.<br>
 * ▸ Center = 3-D viewport ({@link ModelScene}, implemented as a {@code SubScene})<br>
 *
 * <p>This class constructs the main layout of the application, containing a 3D view,
 * overlay controls, and a top menu for file operations and view configuration.</p>
 *
 * @precondition A valid {@link Polyhedron} must be provided.
 * @postcondition A fully initialized {@link Scene} is constructed and ready for display.
 */
public final class MainScene extends Scene {

    private final BorderPane root;
    private final ModelScene modelSubScene;
    private final ModelControlPane modelControlPane;

    /**
     * Constructs a new MainScene with the given dimensions and model.
     *
     * @param w      the width of the scene
     * @param h      the height of the scene
     * @param model  the 3D model to be displayed
     *
     * @precondition {@code w > 0 && h > 0 && model != null}
     * @postcondition Main scene is fully constructed with menu, 3D view, and control pane
     */
    public MainScene(double w, double h, Polyhedron model) {
        super(new BorderPane(), w, h);
        root = (BorderPane) getRoot();

        // 3-D viewport
        modelSubScene = new ModelScene(w, h, model);

        MenuBar menuBar = createMenuBar();
        this.modelControlPane = new ModelControlPane();
        this.modelControlPane.setVisible(false);

        StackPane center = new StackPane(modelSubScene, this.modelControlPane);
        StackPane.setAlignment(this.modelControlPane, Pos.CENTER_RIGHT);
        StackPane.setMargin(this.modelControlPane, new Insets(18, 0, 0, 0));

        center.getChildren().add(menuBar);
        StackPane.setAlignment(menuBar, Pos.TOP_LEFT);
        menuBar.setMaxWidth(Double.MAX_VALUE);

        root.setCenter(center);
        root.setStyle("-fx-background-color:#f0f0f0;");
    }

    /**
     * Returns the side control pane for model manipulation.
     *
     * @return the model control pane
     * @precondition None
     * @postcondition Caller receives non-null reference to control pane
     */
    public ModelControlPane getModelControlPane() {
        return modelControlPane;
    }

    /**
     * Creates the top menu bar of the scene with file and view operations.
     *
     * @return configured {@link MenuBar}
     * @precondition None
     * @postcondition Menu bar with functional event handlers is returned
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("Datei");
        MenuItem loadItem = new MenuItem("Modell laden …");
        MenuItem exitItem = new MenuItem("Beenden");
        loadItem.setOnAction(e -> loadModel());
        exitItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().addAll(loadItem, new SeparatorMenuItem(), exitItem);

        Menu viewMenu = new Menu("Ansicht");
        MenuItem resetView = new MenuItem("Ansicht zurücksetzen");
        CheckMenuItem axesToggle = new CheckMenuItem("Achsen anzeigen");
        axesToggle.setOnAction(e -> modelSubScene.toggleAxis());
        resetView.setOnAction(e -> modelSubScene.resetView());
        viewMenu.getItems().addAll(resetView, axesToggle);

        Menu helpMenu = new Menu("Hilfe");
        MenuItem aboutItem = new MenuItem("Über");
        aboutItem.setOnAction(e -> showAboutDialog());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return menuBar;
    }

    /**
     * Opens a file chooser dialog and loads a selected STL model.
     *
     * @precondition File must exist and be a valid STL format.
     * @postcondition Selected model is loaded and rendered in the 3D viewport.
     */
    private void loadModel() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("STL-Datei öffnen");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("STL-Dateien (*.stl)", "*.stl"),
                new FileChooser.ExtensionFilter("Alle Dateien", "*.*")
        );

        File stlFile = chooser.showOpenDialog(getWindow());

        if (stlFile != null) {
            System.out.println("Gewählte Datei: " + stlFile.getAbsolutePath());
            Polyhedron model = GeometryUtils.createPolyhedronFromFile(stlFile.getAbsolutePath());
            this.getModelSubScene().setMesh(new MeshFactory().buildMeshView(model.getSurfaces()));
        } else {
            System.out.println("Auswahl abgebrochen.");
        }
    }

    /**
     * Displays an information dialog about the application.
     *
     * @precondition None
     * @postcondition Dialog is shown and dismissed upon user confirmation.
     */
    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Über diese Anwendung");
        alert.setHeaderText("3-D-Polyeder Viewer");
        alert.setContentText("Version 1.0\n© 2025 EA");
        alert.showAndWait();
    }

    /**
     * Returns the embedded 3-D model subscene.
     *
     * @return {@link ModelScene} component containing 3-D model
     * @precondition None
     * @postcondition Non-null {@code SubScene} object returned
     */
    public ModelScene getModelSubScene() {
        return modelSubScene;
    }
}
