package org.ea.constant;

import org.ea.model.DefaultVertex;
import org.ea.model.Vertex;

public interface GeometricConstants {
    float HALF_OF_PARALLELOGRAM = 2.0f;
    int FIRST_EDGE = 0;
    int SECOND_EDGE = 1;
    int THIRD_EDGE = 2;
    Vertex ORIGIN = new DefaultVertex(0,0,0);
    int TRIANGLE_VERTICES_AMOUNT = 3;
    int MINIMUM_AMOUNT_OF_EDGES = 3;

}
