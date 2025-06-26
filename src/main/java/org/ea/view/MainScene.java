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
 * Haupt-Fenster (Scene) der Anwendung.<br>
 * ▸ Top   = Menüleiste<br>
 * ▸ Center = 3-D-Viewport ( {@link ModelScene} – jetzt eine {@code SubScene} )<br>
 * <p>
 * Alle bisherigen Menü-Einträge und Platzhalter-Methoden bleiben unverändert –
 * es wurde nur der 3-D-Bereich auf {@code SubScene} umgestellt.
 */
public final class MainScene extends Scene {

    private final BorderPane root;
    private final ModelScene modelSubScene;
    private final ModelControlPane modelControlPane;

    public MainScene(double w, double h, Polyhedron model) {
        super(new BorderPane(), w, h);
        root = (BorderPane) getRoot();

        /* 3-D-Viewport */
        modelSubScene = new ModelScene(w, h, model);

        /* ── Zentraler Layer-Stack ─────────────────────────────── */
        MenuBar menuBar = createMenuBar();            // ❶ altes Menü behalten

        this.modelControlPane = new ModelControlPane();   // Panel
        this.modelControlPane.setVisible(false);

        StackPane center = new StackPane(modelSubScene, this.modelControlPane);        // 3-D ganz unten
        StackPane.setAlignment(this.modelControlPane, Pos.CENTER_RIGHT);
        StackPane.setMargin(this.modelControlPane, new Insets(18, 0, 0, 0));  // ⬅ 10 px Abstand von rechts

        center.getChildren().add(menuBar);                     // ❷ Menü darüber
        StackPane.setAlignment(menuBar, Pos.TOP_LEFT);         // Ausrichtung
        menuBar.setMaxWidth(Double.MAX_VALUE);                 // volle Breite

        /* Alles zusammensetzen */
        root.setCenter(center);                       // oben im BorderPane bleibt jetzt leer
        root.setStyle("-fx-background-color:#f0f0f0;");
    }

    public ModelControlPane getModelControlPane() {
        return modelControlPane;
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
