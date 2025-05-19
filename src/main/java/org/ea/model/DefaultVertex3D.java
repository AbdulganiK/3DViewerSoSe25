package org.ea.model;

import java.util.Objects;

public class DefaultVertex3D implements Vertex3D {
    private float x;
    private float y;
    private float z;

    public DefaultVertex3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public float getX() {
        return this.x;
    }

    @Override
    public float getY() {
        return this.y;
    }

    @Override
    public float getZ() {
        return this.z;
    }

    @Override
    public Vector3D subtract(Vertex3D other) {
        return new DefaultVector3D(
                this.getX() - other.getX(),  // Korrekt: this - other
                this.getY() - other.getY(),
                this.getZ() - other.getZ()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vertex3D other)) return false;
        return Vertex3D.super.equals(other);
    }
}
