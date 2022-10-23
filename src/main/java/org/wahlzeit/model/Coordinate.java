package org.wahlzeit.model;

import java.lang.*;

public class Coordinate {

    private final double x, y, z;
    private Location location = null;


    public Coordinate(double x, double y, double z) {

        if (!Double.isFinite(x)) throw new IllegalArgumentException("x is not finite!");
        if (!Double.isFinite(y)) throw new IllegalArgumentException("y is not finite!");
        if (!Double.isFinite(z)) throw new IllegalArgumentException("z is not finite!");
        this.x = Math.max(0.0, x);
        this.y = Math.max(0.0, y);
        this.z = Math.max(0.0, z);
    }

    public boolean isEqual(Coordinate other) {

        if (this == other) {
            return true;
        }
        if (null == other || this.getClass() != other.getClass()) {
            return false;
        }
        return (Double.compare(x, other.getX()) == 0) &&
                (Double.compare(y, other.getY()) == 0) &&
                (Double.compare(z, other.getZ()) == 0);
    }

    public boolean hasLocation() {
        return (null != location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location newLocation) {
        location = newLocation;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

}
