package org.ea.model;

/**
 * A default implementation of a 3D vector with basic arithmetic operations.
 */
public class DefaultVector implements Vector, BasicArithmetic<Vector> {
    private float x;
    private float y;
    private float z;

    /**
     * Constructs a 3D vector with the specified x, y, and z components.
     *
     * @param x the x-component
     * @param y the y-component
     * @param z the z-component
     * @precondition none
     * @postcondition A new {@code DefaultVector3D} is created with the specified components
     */
    public DefaultVector(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds another vector to this vector.
     *
     * @param other the vector to add
     * @return the result of component-wise addition
     * @precondition {@code other != null}
     * @postcondition Returns a new vector representing {@code this + other}
     */
    @Override
    public Vector add(Vector other) {
        return new DefaultVector(
                this.getX() + other.getX(),
                this.getY() + other.getY(),
                this.getZ() + other.getZ()
        );
    }

    /**
     * Subtracts this vector from another vector.
     *
     * @param other the vector to subtract from
     * @return the result of component-wise subtraction: {@code other - this}
     * @precondition {@code other != null}
     * @postcondition Returns a new vector representing {@code other - this}
     */
    @Override
    public Vector subtract(Vector other) {
        return new DefaultVector(
                other.getX() - this.getX(),
                other.getY() - this.getY(),
                other.getZ() - this.getZ()
        );
    }

    /**
     * Multiplies this vector component-wise with another vector.
     *
     * @param other the vector to multiply with
     * @return the result of component-wise multiplication
     * @precondition {@code other != null}
     * @postcondition Returns a new vector where each component is {@code this.component * other.component}
     */
    @Override
    public Vector multiply(Vector other) {
        return new DefaultVector(
                other.getX() * this.getX(),
                other.getY() * this.getY(),
                other.getZ() * this.getZ()
        );
    }

    /**
     * Returns the x-component of this vector.
     *
     * @return the x value
     * @precondition none
     * @postcondition Returns the stored x value
     */
    @Override
    public Float getX() {
        return this.x;
    }

    /**
     * Returns the y-component of this vector.
     *
     * @return the y value
     * @precondition none
     * @postcondition Returns the stored y value
     */
    @Override
    public Float getY() {
        return this.y;
    }

    /**
     * Returns the z-component of this vector.
     *
     * @return the z value
     * @precondition none
     * @postcondition Returns the stored z value
     */
    @Override
    public Float getZ() {
        return this.z;
    }

    /**
     * Computes the cross product of this vector with another.
     *
     * @param other the vector to compute the cross product with
     * @return the result of the cross product (this × other)
     * @precondition {@code other != null}
     * @postcondition Returns a new vector perpendicular to both {@code this} and {@code other}
     */
    @Override
    public Vector crossProduct(Vector other) {
        float cx = this.y * other.getZ() - this.z * other.getY();
        float cy = this.z * other.getX() - this.x * other.getZ();
        float cz = this.x * other.getY() - this.y * other.getX();
        return new DefaultVector(cx, cy, cz);
    }
}
