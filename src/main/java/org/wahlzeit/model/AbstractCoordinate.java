package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Few things to keep in mind when providing a concrete implementation of AbstractCoordinate:
 * <p>
 * 1. AbstractCoordinate implements the Coordinate interface which provides default implementations
 *    for methods measuring the distance between two concrete coordinates of the same type. <br/>
 *    This distance is used in within the Coordinate's default implementation of isEqual to determine
 *    if two coordinates are equal to each other (currently, two coordinates are equal if
 *    the distance between them is less than 1e-14).
 * </p>
 * <p>
 * 2. When subclassing AbstractCoordinate to provide a new concrete realisation, you need to also add
 *    a corresponding distance measuring method to the Coordinate interface and call that method in
 *    Coordinate's isEqual.
 * </p>
 */
public abstract class AbstractCoordinate extends DataObject implements Coordinate {

    // ---- Coordinate interface methods ----

    @Override
    public abstract CartesianCoordinate asCartesianCoordinate();

    @Override
    public abstract SphericalCoordinate asSphericalCoordinate();

    // getCartesianDistance() has default implementation in Coordinate
    // getCentralAngle() has default implementation in Coordinate
    // isEqual() has default implementation in Coordinate

    // ---- Persistent interface methods ----

    @Override
    public abstract CoordinateId getId();

    @Override
    public abstract String getIdAsString();

    @Override
    public abstract void readFrom(ResultSet rset) throws SQLException;

    @Override
    public abstract void writeOn(ResultSet rset) throws SQLException;

    @Override
    public abstract void writeId(PreparedStatement stmt, int pos) throws SQLException;

    // ---- Object Overrides ----
    @Override
    public boolean equals(Object o) {

        if (!(o instanceof AbstractCoordinate)) {
            return false;
        }
        return isEqual((AbstractCoordinate) o);
    }

    // ---- Design-by-Contract Methods ----
    public void assertWriteCountPlusOne(int oldWriteCount) {
        assert writeCount == oldWriteCount + 1;
    }

    public static String makeKey(String name, double x, double y, double z) {
        // convert to fixed point representation, so we
        // don't get problems with %f format specifier
        double convFactor = 1.0 / Coordinate.EPSILON;
        return String.format("%s(x = %f, y = %f, z = %f)", name, x * convFactor, y * convFactor, z * convFactor);
    }
}
