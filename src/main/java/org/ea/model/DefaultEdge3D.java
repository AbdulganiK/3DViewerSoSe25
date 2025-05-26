package org.ea.model;

import java.util.Objects;

public class DefaultEdge3D implements Edge3D{
    private final Vertex3D startVertex;
    private final Vertex3D endVertex;


    public DefaultEdge3D(Vertex3D startVertex, Vertex3D endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }

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
        return this.getStart().subtract(this.getEnd());
    }

    @Override
    public double getLength() {
        return this.getDirection().length();
    }

    @Override
    public int hashCode() {
        // commutativ: A^B == B^A
        return startVertex.hashCode() ^ endVertex.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Edge3D other)) return false;
        return Edge3D.super.equals(other);
    }
}
