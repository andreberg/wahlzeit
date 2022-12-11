package org.wahlzeit.model;

import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.SysLog;
import static org.wahlzeit.utils.AssertUtil.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CoordinateManager extends ObjectManager {

    protected static final CoordinateManager instance = new CoordinateManager();

    public static final CoordinateManager getInstance() {
        return instance;
    }

    protected Map<CoordinateId, Coordinate> coordinateCache = new HashMap<>();

    @Override
    protected Coordinate createObject(ResultSet rset) throws SQLException {

        return CartesianCoordinate.getCartesianCoordinate(rset);
    }

    public static final Coordinate getCoordinate(CoordinateId id) {
        return instance.getCoordinateFromId(id);
    }

    /**
     * @Preconditions: id != null
     * @Postconditions:
     * @Invariants:
     */
    private Coordinate getCoordinateFromId(CoordinateId id) {

        assertNotNull(id);

        if (CoordinateId.NULL_ID == id) {
            return null;
        }

        Coordinate result = coordinateCache.get(id);

        if (result == null) {
            try {
                PreparedStatement stmt = getReadingStatement("SELECT * FROM coordinates WHERE id = ?");
                result = (Coordinate) readObject(stmt, id.asInt());
            } catch (SQLException sex) {
                SysLog.logThrowable(sex);
            }
            if (result != null) {
                coordinateCache.put(result.getId(), result);
            }
        }

        return result;
    }

    public void saveCoordinates() {
        try {
            PreparedStatement stmt = getUpdatingStatement("SELECT * FROM coordinates WHERE id = ?");
            updateObjects(coordinateCache.values(), stmt);
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
    }

}
