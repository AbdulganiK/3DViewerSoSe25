package org.ea.utiltities;

public class TransformMessage {
    String transformUnit;
    double val;
    String command;

    public TransformMessage(String command, String transformUnit, Double val) {
        this.command = command;
        this.val = val;
        this.transformUnit = transformUnit;
    }

    public double getVal() {
        return val;
    }

    public String getTransformUnit() {
        return transformUnit;
    }

    public String getCommand() {
        return command;
    }
}
