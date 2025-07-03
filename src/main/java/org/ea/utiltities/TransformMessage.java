package org.ea.utiltities;

/**
 * Represents a transformation command message sent between client and server.
 * Contains the command type, transformation axis/unit, and transformation value.
 *
 * @precondition All fields must be provided upon construction
 * @postcondition Message is ready to be interpreted or executed
 */
public class TransformMessage {
    private String transformUnit;
    private double val;
    private String command;

    /**
     * Constructs a new TransformMessage.
     *
     * @param command the transformation command (e.g., "rotate", "translate")
     * @param transformUnit the axis or unit to transform (e.g., "X", "Y", "Z")
     * @param val the numeric transformation value
     * @precondition {@code command != null}, {@code transformUnit != null}
     * @postcondition New TransformMessage with initialized values is created
     */
    public TransformMessage(String command, String transformUnit, Double val) {
        this.command = command;
        this.val = val;
        this.transformUnit = transformUnit;
    }

    /**
     * Returns the transformation value.
     *
     * @return transformation amount as a double
     * @precondition Object is initialized
     * @postcondition No state is modified
     */
    public double getVal() {
        return val;
    }

    /**
     * Returns the transformation unit or axis.
     *
     * @return transformation unit (e.g., X, Y, Z)
     * @precondition Object is initialized
     * @postcondition No state is modified
     */
    public String getTransformUnit() {
        return transformUnit;
    }

    /**
     * Returns the transformation command.
     *
     * @return transformation command (e.g., rotate, translate)
     * @precondition Object is initialized
     * @postcondition No state is modified
     */
    public String getCommand() {
        return command;
    }
}
