package org.ea.constant;

import org.ea.model.DefaultVector;
import org.ea.model.DefaultVertex;
import org.ea.model.Triangle;
import org.ea.model.Vertex;
import org.ea.utiltities.GeometryUtils;
import org.ea.utiltities.TriangleFactory;

public interface PoisonPills {
    Vertex[] VERTICES_POISON_PILL = {new DefaultVertex(1,1,1), new DefaultVertex(2,2,2), new DefaultVertex(3,3,3)};
}
