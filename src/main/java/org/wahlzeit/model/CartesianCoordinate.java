package org.wahlzeit.model;

import java.lang.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class CartesianCoordinate extends AbstractCoordinate {

    protected CoordinateId id;
    protected static CoordinateId lastCoordinateId = new CoordinateId(0);

    private double x, y, z;

    public CartesianCoordinate(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    public CartesianCoordinate(CoordinateId myId) {
        id = myId;
        incWriteCount();
    }

    public CartesianCoordinate(SphericalCoordinate sc) {
        this(sc.getId());
        double phi = sc.getPhi();
        double theta = sc.getTheta();
        double radius = sc.getRadius();
        double sin_phi = Math.sin(phi);
        x = radius * sin_phi * Math.cos(theta);
        y = radius * sin_phi * Math.sin(theta);
        z = radius * Math.cos(phi);
    }

    public CartesianCoordinate(double x, double y, double z) {

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
    public CoordinateId getId() {
        return id;
    }

    @Override
    public String getIdAsString() {
        return String.valueOf(id);
    }

    static CoordinateId getNextCoordinateId() {

        if (lastCoordinateId == null) {
            return lastCoordinateId = CoordinateId.NULL_ID;
        }
        return lastCoordinateId = lastCoordinateId.getNextId();
    }

    public static CoordinateId getLastCoordinateId() {
        return lastCoordinateId;
    }

    public static void setLastCoordinateId(CoordinateId newId) {
        lastCoordinateId = newId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, z);
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return this;
    }

    @Override
    public SphericalCoordinate asSphericalCoordinate() {
        return new SphericalCoordinate(this);
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

    protected double getDistance(CartesianCoordinate other) {
        return getCartesianDistance(other);
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

    @Override
    public String toString() {
        return String.format("CartesianCoordinate(x = %f, y = %f, z = %f)", x, y, z);
    }
}
