package org.ea.view;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Overlay panel providing 3D model controls:
 * <ul>
 *     <li>Manipulation mode toggle (Move / Rotate)</li>
 *     <li>Color selection via {@link ColorPicker}</li>
 *     <li>Opacity adjustment slider</li>
 * </ul>
 *
 *
 * @precondition JavaFX platform initialized
 * @postcondition Interactive control pane for model adjustments is created
 */
public final class ModelControlPane extends BorderPane {

    /**
     * Enum representing manipulation modes.
     */
    public enum ManipulationMode { MOVE, ROTATE }

    private final ToggleButton moveBtn = new ToggleButton("Bewegen");
    private final ToggleButton rotateBtn = new ToggleButton("Rotieren");
    private final ColorPicker colorPicker = new ColorPicker();
    private final Slider opacitySlider = new Slider(0.0, 1.0, 1.0);

    /**
     * Constructs the model control pane and builds the UI.
     *
     * @precondition JavaFX scene graph must be available
     * @postcondition Control pane UI components initialized and ready
     */
    public ModelControlPane() {
        buildUI();
    }

    /**
     * Returns the current manipulation mode (MOVE or ROTATE).
     *
     * @return current mode
     * @precondition None
     * @postcondition Returns selected manipulation mode
     */
    public ManipulationMode getManipulationMode() {
        return moveBtn.isSelected() ? ManipulationMode.MOVE : ManipulationMode.ROTATE;
    }

    /**
     * @return move button
     * @precondition None
     * @postcondition Move button instance is returned
     */
    public ToggleButton getMoveBtn() { return moveBtn; }

    /**
     * @return rotate button
     * @precondition None
     * @postcondition Rotate button instance is returned
     */
    public ToggleButton getRotateBtn() { return rotateBtn; }

    /**
     * @return color picker
     * @precondition None
     * @postcondition Color picker instance is returned
     */
    public ColorPicker getColorPicker() { return colorPicker; }

    /**
     * @return opacity slider
     * @precondition None
     * @postcondition Opacity slider instance is returned
     */
    public Slider getOpacitySlider() { return opacitySlider; }

    /**
     * Builds the complete panel UI.
     *
     * @precondition JavaFX layout components available
     * @postcondition Panel layout created and styled
     */
    private void buildUI() {

        Label title = new Label("Model‑Steuerung");
        title.getStyleClass().add("panel-title");

        Button close = new Button("✕");
        close.setOnAction(e -> setVisible(false));

        Region spacer = new Region();
        HBox header = new HBox(6, title, spacer, close);
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ToggleGroup modeGroup = new ToggleGroup();
        moveBtn.setToggleGroup(modeGroup);
        rotateBtn.setToggleGroup(modeGroup);
        moveBtn.setSelected(false);
        HBox modeBox = new HBox(10, moveBtn, rotateBtn);
        modeBox.setAlignment(Pos.CENTER_LEFT);

        HBox colorBox = new HBox(10, new Label("Farbe:"), colorPicker);
        colorBox.setAlignment(Pos.CENTER_LEFT);

        opacitySlider.setShowTickMarks(true);
        opacitySlider.setShowTickLabels(true);
        opacitySlider.setMajorTickUnit(0.25);
        opacitySlider.setMinorTickCount(4);
        opacitySlider.setBlockIncrement(0.05);
        opacitySlider.setSnapToTicks(false);
        HBox opacityBox = new HBox(10, new Label("Deckkraft:"), opacitySlider);
        opacityBox.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(12,
                modeBox,
                new Separator(Orientation.HORIZONTAL),
                colorBox,
                new Separator(Orientation.HORIZONTAL),
                opacityBox
        );
        content.setAlignment(Pos.TOP_LEFT);
        content.setPadding(new Insets(10));

        setTop(header);
        setCenter(content);
        setPadding(new Insets(10));
        getStyleClass().add("overlay-panel");
        setMouseTransparent(false);

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
     * Shows or hides the control panel.
     *
     * @param visible {@code true} to show panel, {@code false} to hide it
     * @precondition None
     * @postcondition Panel visibility is updated accordingly
     */
    public void setPanelVisibility(boolean visible) {
        setVisible(visible);
    }
}
