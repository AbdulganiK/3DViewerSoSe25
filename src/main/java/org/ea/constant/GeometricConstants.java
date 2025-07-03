package org.ea.constant;

import org.ea.model.DefaultVertex;
import org.ea.model.Vertex;

/**
 * <p>Defines geometric constants used in 3D modeling calculations and STL processing.</p>
 *
 * @precondition None – this interface is used solely to expose constants.
 * @postcondition All constants are statically available and remain unchanged.
 */
public interface GeometricConstants {
    float HALF_OF_PARALLELOGRAM = 2.0f;
    int FIRST_EDGE = 0;
    int SECOND_EDGE = 1;
    int THIRD_EDGE = 2;
    Vertex ORIGIN = new DefaultVertex(0, 0, 0);
    int TRIANGLE_VERTICES_AMOUNT = 3;
    int MINIMUM_AMOUNT_OF_EDGES = 3;
    String SOLID = "solid";

}
