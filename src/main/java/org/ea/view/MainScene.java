package org.ea.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import org.ea.model.Polyhedron;
import org.ea.utiltities.GeometryUtils;
import org.ea.utiltities.MeshFactory;
import org.ea.utiltities.PolyhedronFactory;

import java.io.File;

/**
 * Haupt-Fenster (Scene) der Anwendung.<br>
 * ▸ Top   = Menüleiste<br>
 * ▸ Center = 3-D-Viewport ( {@link ModelScene} – jetzt eine {@code SubScene} )<br>
 * <p>
 * Alle bisherigen Menü-Einträge und Platzhalter-Methoden bleiben unverändert –
 * es wurde nur der 3-D-Bereich auf {@code SubScene} umgestellt.
 */
public final class MainScene extends Scene {

    private final BorderPane rootPane;
    private final ModelScene modelSubScene;      // 3-D-Viewport als SubScene

    public MainScene(double width, double height, Polyhedron model) {
        /* BorderPane als Root – Menü oben, 3-D in der Mitte */
        super(new BorderPane(), width, height);
        rootPane = (BorderPane) getRoot();

        /* 3-D-SubScene in einen zentrierenden StackPane-Container */
        modelSubScene = new ModelScene(width, height - 30, model);
        StackPane center = new StackPane(modelSubScene);
        center.setAlignment(Pos.CENTER);

        /* Layout zusammenbauen */
        rootPane.setTop(createMenuBar());
        rootPane.setCenter(center);
        rootPane.setStyle("-fx-background-color:#f0f0f0;");
    }

    /* ====================================================================== */
    /* Menü‐Leiste (Logik unverändert)                                        */
    /* ====================================================================== */

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        /* Datei ------------------------------------------------------------- */
        Menu fileMenu = new Menu("Datei");
        MenuItem loadItem = new MenuItem("Modell laden …");
        MenuItem exitItem = new MenuItem("Beenden");
        loadItem.setOnAction(e -> loadModel());          // Platzhalter
        exitItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().addAll(loadItem,
                new SeparatorMenuItem(),
                exitItem);

        /* Ansicht ----------------------------------------------------------- */
        Menu viewMenu = new Menu("Ansicht");
        MenuItem resetView = new MenuItem("Ansicht zurücksetzen");
        CheckMenuItem axesToggle = new CheckMenuItem("Achsen anzeigen");
        axesToggle.setOnAction(e -> modelSubScene.toggleAxis());
        resetView.setOnAction(e -> modelSubScene.resetView());
        viewMenu.getItems().addAll(resetView, axesToggle);

        /* Hilfe ------------------------------------------------------------- */
        Menu helpMenu = new Menu("Hilfe");
        MenuItem aboutItem = new MenuItem("Über");
        aboutItem.setOnAction(e -> showAboutDialog());   // Platzhalter
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return menuBar;
    }

    /* ====================================================================== */
    /* Platzhalter-Dialoge – Logik bleibt unverändert                         */
    /* ====================================================================== */

    private void loadModel() {
        /* ── einfacher Datei-Dialog ───────────────────────────── */
        FileChooser chooser = new FileChooser();
        chooser.setTitle("STL-Datei öffnen");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("STL-Dateien (*.stl)", "*.stl"),
                new FileChooser.ExtensionFilter("Alle Dateien", "*.*")
        );

        /* Dialog anzeigen; Fensterreferenz = aktuelle Stage */
        File stlFile = chooser.showOpenDialog(getWindow());

        /* Bis hierher nur den File wählen – Einlesen folgt später */
        if (stlFile != null) {
            System.out.println("Gewählte Datei: " + stlFile.getAbsolutePath());
            Polyhedron model = GeometryUtils.createPolyhedronFromFile(stlFile.getAbsolutePath());
            // Model neusetzen:
            this.getModelSubScene().setMesh(new MeshFactory().buildMeshView(model.getSurfaces()));

        } else {
            System.out.println("Auswahl abgebrochen.");
        }
    }

    private void showAboutDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Über diese Anwendung");
        alert.setHeaderText("3-D-Polyeder Viewer");
        alert.setContentText("Version 1.0\n© 2025 EA");
        alert.showAndWait();
    }

    /* Getter – unverändert -------------------------------------------------- */
    public ModelScene getModelSubScene() { return modelSubScene; }
}
