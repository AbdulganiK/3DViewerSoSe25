package org.ea.model;

public interface Vector3D {
    int SECOND_POWER = 2;
    Float getX();
    Float getY();
    Float getZ();
    Vector3D crossProduct(Vector3D other);
    default double length() {
        return Math.sqrt(Math.pow(getX(), SECOND_POWER) + Math.pow(getY(), SECOND_POWER) + Math.pow(getZ(), SECOND_POWER));
    }

    default double angleBetween(Vector3D other) {
        // Skalarprodukt berechnen
        double dot = dotProduct(other);

        // Längen beider Vektoren berechnen
        double lenA = length();
        double lenB = other.length();

        // Kosinus des Winkels: cos(θ) = (a·b) / (|a| * |b|)
        double cosTheta = dot / (lenA * lenB);

        // Auf gültigen Bereich [-1, 1] bzw [π, 0] begrenzen, damit Math.acos keinen NaN zurückgibt
        if (cosTheta < -1) cosTheta = -1;
        if (cosTheta >  1) cosTheta =  1;

        // Arcus-Cosinus liefert den Winkel in Radiant
        return Math.acos(cosTheta);
    }
    default float dotProduct(Vector3D other){
        return getX() * other.getX() + getY() * other.getY() + getZ() * other.getZ();
    }

}
