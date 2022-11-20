package org.wahlzeit.model;

import org.wahlzeit.services.Persistent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Coordinate extends Persistent {

    double EPSILON = 1e-14;

    CartesianCoordinate asCartesianCoordinate();

    default double getCartesianDistance(Coordinate to) {

        if (null == to) {
            return 0;
        }

        CartesianCoordinate a = to.asCartesianCoordinate();
        CartesianCoordinate b = this.asCartesianCoordinate();
        double xdiff = a.getX() - b.getX();
        double ydiff = a.getY() - b.getY();
        double zdiff = a.getZ() - b.getZ();

        return Math.sqrt((xdiff * xdiff) + (ydiff * ydiff) + (zdiff * zdiff));
    }

    SphericalCoordinate asSphericalCoordinate();

    default double getCentralAngle(Coordinate to) {

        if (null == to) {
            return 0;
        }

        double theta1 = to.asSphericalCoordinate().getTheta();
        double theta2 = this.asSphericalCoordinate().getTheta();
        double phi1 = to.asSphericalCoordinate().getPhi();
        double phi2 = this.asSphericalCoordinate().getPhi();

        double delta_theta = Math.abs(theta1 - theta2);

        // spherical law of cosines
        //double delta_sigma = Math.acos(Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(delta_theta));

        // vincenty
        double t1 = Math.cos(phi2) * Math.sin(delta_theta);
        double t2 = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(delta_theta);
        double y = Math.sin(phi1) * Math.sin(phi2) + Math.cos(phi1) * Math.cos(phi2) * Math.cos(delta_theta);
        double x = Math.sqrt(t1 * t1 + t2 * t2);
        return Math.atan2(x, y);
    }

    default boolean isEqual(Coordinate other) {

        if (this == other) {
            return true;
        }
        if (null == other || this.getClass() != other.getClass()) {
            return false;
        }

        double dist;

        if (other instanceof SphericalCoordinate) {
            dist = other.getCentralAngle(this.asSphericalCoordinate());
        } else if (other instanceof CartesianCoordinate) {
            dist = other.getCartesianDistance(this.asCartesianCoordinate());
        } else {
            throw new IllegalArgumentException("coordinate being compared is of unknown type");
        }
        return Math.abs(dist - 0) < EPSILON;
    }

    CoordinateId getId();

    @Override
    String getIdAsString();

    @Override
    void readFrom(ResultSet rset) throws SQLException;

    @Override
    void writeOn(ResultSet rset) throws SQLException;

    @Override
    void writeId(PreparedStatement stmt, int pos) throws SQLException;
}
