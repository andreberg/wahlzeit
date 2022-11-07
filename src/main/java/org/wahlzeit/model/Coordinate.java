package org.wahlzeit.model;

import java.lang.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import org.wahlzeit.services.*;

public class Coordinate extends DataObject {

    protected CoordinateId id;
    protected static CoordinateId lastCoordinateId = new CoordinateId(0);

    private double x, y, z;

    public Coordinate(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    public Coordinate(CoordinateId myId) {
        id = myId;
        incWriteCount();
    }

    public Coordinate(double x, double y, double z) {

        if (!Double.isFinite(x)) throw new IllegalArgumentException("x is not finite!");
        if (!Double.isFinite(y)) throw new IllegalArgumentException("y is not finite!");
        if (!Double.isFinite(z)) throw new IllegalArgumentException("z is not finite!");
        this.x = Math.max(0.0, x);
        this.y = Math.max(0.0, y);
        this.z = Math.max(0.0, z);

        id = getNextCoordinateId();
        incWriteCount();
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof Coordinate)) {
            return false;
        }
        return isEqual((Coordinate) o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, z);
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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    protected double getDistance(Coordinate other) {

        if (null == other) {
            return 0;
        }

        double xdiff = other.getX() - x;
        double ydiff = other.getY() - y;
        double zdiff = other.getZ() - z;

        return Math.sqrt((xdiff * xdiff) + (ydiff * ydiff) + (zdiff * zdiff));
    }

    @Override
    public String getIdAsString() {
        return String.valueOf(id);
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {

        id = new CoordinateId(rset.getInt("id"));
        x = rset.getDouble("x");
        y = rset.getDouble("y");
        z = rset.getDouble("z");
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {

        rset.updateInt("id", id.asInt());
        rset.updateDouble("x", x);
        rset.updateDouble("y", y);
        rset.updateDouble("z", z);
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        stmt.setInt(pos, id.asInt());
    }

    public CoordinateId getId() {
        return id;
    }

    public static synchronized CoordinateId getNextCoordinateId() {

        if (lastCoordinateId == null) {
            return lastCoordinateId = CoordinateId.NULL_ID;
        }
        return lastCoordinateId = lastCoordinateId.getNextId();
    }

    public static synchronized CoordinateId getLastCoordinateId() {
        return lastCoordinateId;
    }

    public static synchronized void setLastCoordinateId(CoordinateId newId) {
        lastCoordinateId = newId;
    }
}
