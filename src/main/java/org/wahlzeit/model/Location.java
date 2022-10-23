package org.wahlzeit.model;

public class Location {

    private Coordinate coordinate = new Coordinate(0,0,0);

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate newCoordinate) {
        if (null == newCoordinate) throw new IllegalArgumentException("newCoordinate cannot be null");
        coordinate = newCoordinate;
    }

    public boolean isEqual(Location other) {

        if (this == other) {
            return true;
        }
        if (null == other || this.getClass() != other.getClass()) {
            return false;
        }
        return coordinate.isEqual(other.getCoordinate());
    }
}
