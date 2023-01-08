package org.wahlzeit.model;

import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.Persistent;
import org.wahlzeit.services.SysLog;
import org.wahlzeit.utils.PatternInstance;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@PatternInstance(
    patternName = "Mediator",
    participants = {
        "ConcreteMediator"
    }
)
public class LocationManager extends ObjectManager {

    protected static final LocationManager instance = new LocationManager();

    public static final LocationManager getInstance() {
        return instance;
    }

    protected Map<LocationId, Location> locationCache = new HashMap<>();

    @Override
    protected Location createObject(ResultSet rset) throws SQLException {
        return new Location(rset);
    }

    public static final Location getLocation(LocationId id) {
        return instance.getLocationFromId(id);
    }

    private Location getLocationFromId(LocationId id) {

        if (LocationId.NULL_ID == id) {
            return null;
        }

        Location result = locationCache.get(id);

        if (result == null) {
            try {
                PreparedStatement stmt = getReadingStatement("SELECT * FROM locations WHERE id = ?");
                result = (Location) readObject(stmt, id.asInt());
            } catch (SQLException sex) {
                SysLog.logThrowable(sex);
            }
            if (result != null) {
                locationCache.put(result.getId(), result);
            }
        }

        return result;
    }

    public void saveLocations() {
        try {
            PreparedStatement stmt = getUpdatingStatement("SELECT * FROM locations WHERE id = ?");
            updateObjects(locationCache.values(), stmt);
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
    }

    @Override
    protected void updateDependents(Persistent obj) throws SQLException {

        Location location = (Location) obj;
        Coordinate coordinate = location.getCoordinate();

        PreparedStatement stmt = getReadingStatement("DELETE FROM coordinates WHERE id = ?");
        deleteObject(coordinate, stmt);

        stmt = getReadingStatement("INSERT INTO coordinates VALUES(?, ?, ?, ?)");
        stmt.setInt(1, coordinate.getId().asInt());
        stmt.setDouble(2, coordinate.asCartesianCoordinate().getX());
        stmt.setDouble(3, coordinate.asCartesianCoordinate().getY());
        stmt.setDouble(4, coordinate.asCartesianCoordinate().getZ());

        SysLog.logQuery(stmt);
        stmt.executeUpdate();
    }
}
