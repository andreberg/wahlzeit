package org.wahlzeit.model;

import org.wahlzeit.services.DataObject;
import static org.wahlzeit.utils.AssertUtil.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <h2>Collaboration Based Design</h2>
 * <h3>Location / Coordinate Collaboration</h3>
 * <l>
 *      <li>Purpose: assign a fixed spatial reference</li>
 *      <li>Role types: Location (Client), Coordinate (Service)</li>
 *      <li>Role binding: client, service</li>
 * </l>
 */
public class Location extends DataObject {

    protected LocationId id;

    protected static LocationId lastLocationId = LocationId.NULL_ID;

    private Coordinate coordinate;

    public Location() {
        // be careful here:
        // this should not be replaced by Coordinate.NULL_COORDINATE
        // because then the coordinate ID wouldn't be incremented!
        // this(new CartesianCoordinate(0,0,0));
        this(CartesianCoordinate.getCartesianCoordinate(0,0,0));
    }

    public Location(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    public Location(LocationId myId) {
        id = myId;
        incWriteCount();
    }

    /**
     * @Preconditions: coordinate != null
     * @Postconditions: this.coordinate == coordinate && this.id == lastLocationId + 1
     * @Invariants:
     */
    public Location(Coordinate coordinate) {

        LocationId oldLastLocationId = lastLocationId;

        assertNotNull(coordinate);

        this.coordinate = coordinate;
        id = getNextLocationId();

        assertPlusOne(id.asInt(), oldLastLocationId.asInt());

        incWriteCount();
    }

    public boolean isEqual(Location other) throws UnknownCoordinateTypeException {

        if (this == other) {
            return true;
        }
        if (null == other || this.getClass() != other.getClass()) {
            return false;
        }
        return coordinate.isEqual(other.getCoordinate());
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @Preconditions: newCoordinate != null
     * @Postconditions:
     * @Invariants:
     */
    public void setCoordinate(Coordinate newCoordinate) {
        assertNotNull(newCoordinate);
        coordinate = newCoordinate;
        incWriteCount();
    }

    public LocationId getId() {
        return id;
    }

    // --- Persistent Interface Overrides ---

    @Override
    public String getIdAsString() {
        return String.valueOf(id);
    }

    /**
     * @Preconditions: rset != null
     * @Postconditions:
     * @Invariants:
     */
    @Override
    public void readFrom(ResultSet rset) throws SQLException {
        assertNotNull(rset);
        id = new LocationId(rset.getInt("id"));
        coordinate = CoordinateManager.getCoordinate(CoordinateId.getIdFromInt(rset.getInt("coordinate")));
    }

    /**
     * @Preconditions: rset != null
     * @Postconditions:
     * @Invariants:
     */
    @Override
    public void writeOn(ResultSet rset) throws SQLException {
        assertNotNull(rset);
        rset.updateInt("id", id.asInt());
        rset.updateInt("coordinate", (coordinate == null) ? 0 : coordinate.getId().asInt());
    }

    /**
     * @Preconditions: stmt != null
     * @Postconditions:
     * @Invariants:
     */
    @Override
    public void writeId(PreparedStatement stmt, int pos) throws SQLException {
        assertNotNull(stmt);
        stmt.setInt(pos, id.asInt());
    }

    // --- Static Getters/Setters For LastLocationID ---

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
