package org.ea;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.enums.ButtonType;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.ea.model.*;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Vertex3D vertexA = new DefaultVertex3D(5, 3, 1);
        Vertex3D vertexB = new DefaultVertex3D(4, 1, 7);
        Vertex3D vertexC = new DefaultVertex3D(1, 13, 2);
        ArrayList<Edge3D> edges = new ArrayList<>();
        Edge3D  edgeA = new DefaultEdge3D(vertexA, vertexB);
        Edge3D  edgeB = new DefaultEdge3D(vertexB, vertexC);
        Edge3D  edgeC = new DefaultEdge3D(vertexC, vertexA);
        edges.add(edgeA);
        edges.add(edgeB);
        edges.add(edgeC);
        Triangle triangle = new Triangle(edges);
        System.out.println(triangle.getPerimeter());



    }
}
