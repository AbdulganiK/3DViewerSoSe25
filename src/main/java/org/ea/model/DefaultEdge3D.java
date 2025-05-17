package org.ea.model;

public class DefaultEdge3D implements Edge3D{
    private Vertex3D startVertex;
    private Vertex3D endVertex;

    @Override
    public Vertex3D getStart() {
        return this.startVertex;
    }

    @Override
    public Vertex3D getEnd() {
        return this.endVertex;
    }

    @Override
    public Vector3D getDirection() {
        return this.startVertex.subtract(this.endVertex);
    }

    @Override
    public double getLength() {
        return this.getDirection().length();
    }
}
