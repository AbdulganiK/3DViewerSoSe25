package org.ea.model;

public class DefaultVector3D implements Vector3D, BasicArithmetic<Vector3D> {
    private float x;
    private float y;
    private float z;

    @Override
    public Vector3D add(Vector3D other) {
        return new DefaultVector3D(this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ());
    }

    public DefaultVector3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vector3D subtract(Vector3D other) {
        return new DefaultVector3D(other.getX() - this.getX(), other.getY() - this.getY(), other.getZ() - this.getZ());
    }

    @Override
    public Vector3D multiply(Vector3D other) {
        return new DefaultVector3D(other.getX() * this.getX(), other.getY() * this.getY(), other.getZ() * this.getZ());
    }

    @Override
    public Float getX() {
        return this.x;
    }

    @Override
    public Float getY() {
        return this.y;
    }

    @Override
    public Float getZ() {
        return this.z;
    }

    @Override
    public Vector3D crossProduct(Vector3D other) {
        float cx = this.y * other.getZ() - this.z * other.getY();
        float cy = this.z * other.getX() - this.x * other.getZ();
        float cz = this.x * other.getY() - this.y * other.getX();
        return new DefaultVector3D(cx, cy, cz);
    }

}
