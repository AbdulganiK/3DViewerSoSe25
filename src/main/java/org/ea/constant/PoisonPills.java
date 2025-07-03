package org.ea.constant;


import org.ea.model.DefaultVertex;
import org.ea.model.Vertex;

/**
 * <p>Defines poison pill objects used for signaling termination in concurrent or stream-processing contexts.</p>
 *
 * @precondition None – this interface provides static poison pill constants for stream control.
 * @postcondition Poison pills remain statically accessible for signaling termination.
 */
public interface PoisonPills {
    Vertex[] VERTICES_POISON_PILL = {new DefaultVertex(1,1,1), new DefaultVertex(2,2,2), new DefaultVertex(3,3,3)};
}
