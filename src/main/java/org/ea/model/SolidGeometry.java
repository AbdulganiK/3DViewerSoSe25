package org.ea.model;

public interface SolidGeometry extends SurfaceGeometry {
    double getVolume();

    Geometry translate(Vector3D translation);

    Geometry scale(double factor);

    Geometry rotate(Vector3D axis, double angle);

}
