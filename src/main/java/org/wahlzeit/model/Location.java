package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Location extends DataObject {

    protected LocationId id;

    protected static LocationId lastLocationId = LocationId.NULL_ID;

    private Coordinate coordinate;

    public Location() {
        // be careful here:
        // this should not be replaced by Coordinate.NULL_COORDINATE
        // because then the coordinate ID wouldn't be incremented!
        this(new Coordinate(0,0,0));
    }

    public Location(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    public Location(LocationId myId) {
        id = myId;
        incWriteCount();
    }

    public Location(Coordinate coordinate) {
        if (null == coordinate) {
            throw new IllegalArgumentException("coordinate cannot be null!");
        }
        this.coordinate = coordinate;
        id = getNextLocationId();
        incWriteCount();
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate newCoordinate) {
        if (null == newCoordinate) throw new IllegalArgumentException("newCoordinate cannot be null");
        coordinate = newCoordinate;
        incWriteCount();
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

    @Override
    public String getIdAsString() {
        return String.valueOf(id);
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        id = new LocationId(rset.getInt("id"));
        coordinate = CoordinateManager.getCoordinate(CoordinateId.getIdFromInt(rset.getInt("coordinate")));
    }

    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        rset.updateInt("id", id.asInt());
        rset.updateInt("coordinate", (coordinate == null) ? 0 : coordinate.getId().asInt());
    }

    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        stmt.setInt(pos, id.asInt());
    }

    public LocationId getId() {
        return id;
    }

    public static synchronized LocationId getNextLocationId() {

        if (lastLocationId == null) {
            return lastLocationId = LocationId.NULL_ID;
        }
        return lastLocationId = lastLocationId.getNextId();
    }

    public static synchronized LocationId getLastLocationId() {
        return lastLocationId;
    }

    public static synchronized void setLastLocationId(LocationId newId) {
        lastLocationId = newId;
    }

}
