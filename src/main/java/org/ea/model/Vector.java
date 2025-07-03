package org.ea.model;

/**
 * Represents a 3D vector and provides basic vector operations such as cross product,
 * dot product, length calculation, and angle between two vectors.
 *
 * @precondition Implementations must define non-null {@code getX()}, {@code getY()}, and {@code getZ()}.
 * @postcondition All vector calculations are consistent with standard 3D vector algebra.
 */
public interface Vector {
    int SECOND_POWER = 2;

    /**
     * @return X component of the vector
     * @precondition None
     * @postcondition A non-null float is returned representing the x-axis value
     */
    Float getX();

    /**
     * @return Y component of the vector
     * @precondition None
     * @postcondition A non-null float is returned representing the y-axis value
     */
    Float getY();

    /**
     * @return Z component of the vector
     * @precondition None
     * @postcondition A non-null float is returned representing the z-axis value
     */
    Float getZ();

    /**
     * Computes the cross product between this vector and another.
     *
     * @param other the other vector
     * @return a new vector that is the cross product
     * @precondition {@code other} must not be null
     * @postcondition The resulting vector is perpendicular to both input vectors
     */
    Vector crossProduct(Vector other);

    /**
     * Computes the Euclidean length (magnitude) of this vector.
     *
     * @return the length as a double
     * @precondition None
     * @postcondition Returns a non-negative real number
     */
    default double length() {
        return Math.sqrt(
                Math.pow(getX(), SECOND_POWER) +
                        Math.pow(getY(), SECOND_POWER) +
                        Math.pow(getZ(), SECOND_POWER)
        );
    }

    /**
     * Computes the angle (in radians) between this vector and another.
     *
     * @param other the other vector
     * @return the angle in radians between [0, π]
     * @precondition {@code other} is non-null and has non-zero length
     * @postcondition A valid angle (double) is returned, clamped to avoid NaN
     */
    default double angleBetween(Vector other) {
        double dot = dotProduct(other);
        double lenA = length();
        double lenB = other.length();
        double cosTheta = dot / (lenA * lenB);
        if (cosTheta < -1) cosTheta = -1;
        if (cosTheta >  1) cosTheta =  1;
        return Math.acos(cosTheta);
    }

    /**
     * Computes the dot (scalar) product with another vector.
     *
     * @param other the other vector
     * @return the scalar product as a float
     * @precondition {@code other} is not null
     * @postcondition A float representing the dot product is returned
     */
    default float dotProduct(Vector other) {
        return getX() * other.getX() + getY() * other.getY() + getZ() * other.getZ();
    }
}
