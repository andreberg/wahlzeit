package org.wahlzeit.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class SphericalCoordinate extends AbstractCoordinate {

    protected CoordinateId id;
    protected static CoordinateId lastCoordinateId = new CoordinateId(0);

    private double phi, theta, radius;

    public SphericalCoordinate(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    public SphericalCoordinate(CoordinateId myId) {
        id = myId;
        incWriteCount();
    }

    public SphericalCoordinate(CartesianCoordinate cc) {
        this(cc.getId());
        double x = cc.getX();
        double y = cc.getY();
        double z = cc.getZ();
        radius = Math.sqrt(x * x + y * y + z * z);
        theta = Math.atan2(y, x);
        phi = Math.atan2(Math.sqrt(x * x + y * y), z);
    }

    public SphericalCoordinate(double phi_degrees, double theta_degrees, double radius) {

        if (!Double.isFinite(phi_degrees)) throw new IllegalArgumentException("phi is not finite!");
        if (!Double.isFinite(theta_degrees)) throw new IllegalArgumentException("theta is not finite!");
        if (!Double.isFinite(radius)) throw new IllegalArgumentException("radius is not finite!");
        this.phi = Math.toRadians(phi_degrees);
        this.theta = Math.toRadians(theta_degrees);
        this.radius = radius;

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

    static CoordinateId getLastCoordinateId() {
        return lastCoordinateId;
    }

    static void setLastCoordinateId(CoordinateId newId) {
        lastCoordinateId = newId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phi, theta, radius);
    }

    public double getPhi() {
        return phi;
    }

    public double getTheta() {
        return theta;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public CartesianCoordinate asCartesianCoordinate() {
        return new CartesianCoordinate(this);
    }

    @Override
    public SphericalCoordinate asSphericalCoordinate() {
        return this;
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {

        id = new CoordinateId(rset.getInt("id"));
        phi = rset.getDouble("x");
        theta = rset.getDouble("y");
        radius = rset.getDouble("z");
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {

        rset.updateInt("id", id.asInt());
        rset.updateDouble("x", phi);
        rset.updateDouble("y", theta);
        rset.updateDouble("z", radius);
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        stmt.setInt(pos, id.asInt());
    }

    @Override
    public String toString() {
        return String.format("SphericalCoordinate(phi_degrees = %f, theta_degrees = %f, radius = %f)", Math.toDegrees(phi), Math.toDegrees(theta), radius);
    }
}
