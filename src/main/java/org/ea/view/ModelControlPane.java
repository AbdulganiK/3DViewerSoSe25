package org.ea.view;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Overlay-Panel mit drei Bedienelement-Sektionen:
 * <ul>
 *     <li>1️⃣ Bewegungsmodus (Bewegen / Rotieren – exklusiv)</li>
 *     <li>2️⃣ Farbwahl per {@link ColorPicker}</li>
 *     <li>3️⃣ Deckkraft‑Regler (0 % – 100 %)</li>
 * </ul>
 * Größe, Farbe und Schatten des Panels bleiben unverändert.
 */
public final class ModelControlPane extends BorderPane {

    /* Öffentlich sichtbare API ------------------------------------------ */
    public enum ManipulationMode { MOVE, ROTATE }

    private final ToggleButton moveBtn = new ToggleButton("Bewegen");
    private final ToggleButton rotateBtn = new ToggleButton("Rotieren");
    private final ColorPicker colorPicker = new ColorPicker();
    private final Slider opacitySlider = new Slider(0.0, 1.0, 1.0); // 0 = komplett durchsichtig, 1 = voll sichtbar

    public ModelControlPane() {
        buildUI();
    }

    /* Getter ------------------------------------------------------------ */
    public ManipulationMode getManipulationMode() {
        return moveBtn.isSelected() ? ManipulationMode.MOVE : ManipulationMode.ROTATE;
    }

    public ColorPicker getColorPicker() { return colorPicker; }

    public Slider getOpacitySlider()   { return opacitySlider; }

    /* UI‑Aufbau --------------------------------------------------------- */
    private void buildUI() {

        /* Kopfzeile ---------------------------------------------------- */
        Label title = new Label("Model‑Steuerung");
        title.getStyleClass().add("panel-title");

        Button close = new Button("✕");
        close.setOnAction(e -> setVisible(false));

        Region spacer = new Region();
        HBox header = new HBox(6, title, spacer, close);
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        /* 1️⃣ Manipulations-Modus -------------------------------------- */
        ToggleGroup modeGroup = new ToggleGroup();
        moveBtn.setToggleGroup(modeGroup);
        rotateBtn.setToggleGroup(modeGroup);
        moveBtn.setSelected(true);
        HBox modeBox = new HBox(10, moveBtn, rotateBtn);
        modeBox.setAlignment(Pos.CENTER_LEFT);

        /* 2️⃣ Farbwahl -------------------------------------------------- */
        HBox colorBox = new HBox(10, new Label("Farbe:"), colorPicker);
        colorBox.setAlignment(Pos.CENTER_LEFT);

        /* 3️⃣ Deckkraft‑Slider ---------------------------------------- */
        opacitySlider.setShowTickMarks(true);
        opacitySlider.setShowTickLabels(true);
        opacitySlider.setMajorTickUnit(0.25);
        opacitySlider.setMinorTickCount(4);
        opacitySlider.setBlockIncrement(0.05);
        opacitySlider.setSnapToTicks(false);
        HBox opacityBox = new HBox(10, new Label("Deckkraft:"), opacitySlider);
        opacityBox.setAlignment(Pos.CENTER_LEFT);

        /* Gesamtes Content‑Layout ------------------------------------- */
        VBox content = new VBox(12,
                modeBox,
                new Separator(Orientation.HORIZONTAL),
                colorBox,
                new Separator(Orientation.HORIZONTAL),
                opacityBox
        );
        content.setAlignment(Pos.TOP_LEFT);
        content.setPadding(new Insets(10));

        /* Panel zusammenbauen ---------------------------------------- */
        setTop(header);
        setCenter(content);
        setPadding(new Insets(10));
        getStyleClass().add("overlay-panel");
        setMouseTransparent(false);

        /* Basis‑Style (Größe, Farben) unverändert ------------------- */
        setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #666666;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 8;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 10;" +
                        "-fx-pref-width: 320;" +
                        "-fx-max-width: 320;" +
                        "-fx-pref-height: 400;" +
                        "-fx-max-height: 400;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 8, 0, 0, 2);"
        );
    }

    /**
     * Blendet das Panel ein oder aus.
     * @param visible {@code true} → Panel anzeigen · {@code false} → Panel verbergen
     */
    public void setPanelVisibility(boolean visible) {
        setVisible(visible);
    }
}
