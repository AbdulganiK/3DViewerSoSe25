package org.ea.model;

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
        return new DefaultVector3D(other.getX() - this.getX(), other.getY() - this.getY(), other.getZ() - this.getZ());
    }


}
