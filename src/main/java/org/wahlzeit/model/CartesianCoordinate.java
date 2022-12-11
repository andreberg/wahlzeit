package org.wahlzeit.model;

import java.lang.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;

import static org.wahlzeit.utils.AssertUtil.*;

public class CartesianCoordinate extends AbstractCoordinate {

    protected final CoordinateId id;
    protected static CoordinateId lastCoordinateId = new CoordinateId(0);

    public static Map<String, CartesianCoordinate> allCartesianCoordinates = new HashMap<>();

    private final double x, y, z;

    private CartesianCoordinate(ResultSet rset) throws SQLException {

        id = new CoordinateId(rset.getInt("id"));
        x = rset.getDouble("x");
        y = rset.getDouble("y");
        z = rset.getDouble("z");
    }

    /**
     * @Preconditions: myId != null
     * @Postconditions: this.id == myId && this.writeCount == old(this.writeCount) + 1
     * @Invariants:
     */
    private CartesianCoordinate(CoordinateId myId) {

        final int oldWriteCount = writeCount;

        // check pre-conditions
        assertNotNull(myId);

        id = myId;
        incWriteCount();

        x = y = z = 0;

        // check post-conditions
        assertEquals(this.id, myId);
        assertWriteCountPlusOne(oldWriteCount);
    }

    /**
     * @Preconditions: sc != null
     * @Postconditions: this.id == sc.id &&
     *                  this.writeCount == old(this.writeCount) + 1 &&
     *                  Double.isFinite(this.x) &&
     *                  Double.isFinite(this.y) &&
     *                  Double.isFinite(this.z)
     * @Invariants:
     */
    public CartesianCoordinate(SphericalCoordinate sc) {

        final int oldWriteCount = writeCount;

        id = sc.getId();
        incWriteCount();

        // check pre-conditions
        assertNotNull(sc.getId());


        double phi = sc.getPhi();
        double theta = sc.getTheta();
        double radius = sc.getRadius();
        double sin_phi = Math.sin(phi);
        x = radius * sin_phi * Math.cos(theta);
        y = radius * sin_phi * Math.sin(theta);
        z = radius * Math.cos(phi);

        // check post-conditions
        assertEquals(this.id.asInt(), sc.getId().asInt());
        assertWriteCountPlusOne(oldWriteCount);
        assertIsFinite(this.x);
        assertIsFinite(this.y);
        assertIsFinite(this.z);
    }

    /**
     * @Preconditions: Double.isFinite(x) &&
     *                 Double.isFinite(y) &&
     *                 Double.isFinite(z)
     * @Postconditions: this.id == this.lastCoordinateId + 1 &&
     *                  this.writeCount == old(this.writeCount) + 1 &&
     *                  this.x >= 0 &&
     *                  this.y >= 0 &&
     *                  this.z >= 0
     * @Invariants:
     */
    private CartesianCoordinate(double x, double y, double z) {

        final int oldWriteCount = writeCount;

        // check pre-conditions
        assertIsFinite(x);
        assertIsFinite(y);
        assertIsFinite(z);

        this.x = Math.max(0.0, x);
        this.y = Math.max(0.0, y);
        this.z = Math.max(0.0, z);

        id = getNextCoordinateId();
        incWriteCount();

        // check post-conditions
        assertEquals(this.id.asInt(), lastCoordinateId.asInt());
        assertWriteCountPlusOne(oldWriteCount);
        assertNonNegative(this.x);
        assertNonNegative(this.y);
        assertNonNegative(this.z);
    }

    /**
     * @Preconditions:
     * @Postconditions: result.id == this.id && result instanceof CartesianCoordinate
     * @Invariants: this.id
     */
    @Override
    public CartesianCoordinate asCartesianCoordinate() {

        // don't need to check anything here, since this
        // just returns itself, so that pre- and postconditions
        // are trivially satisfied.
        return this;
    }

    /**
     * @Preconditions:
     * @Postconditions: result.id == this.id && result instanceof SphericalCoordinate
     * @Invariants: this.id
     */
    @Override
    public SphericalCoordinate asSphericalCoordinate() {

        final CoordinateId curId = this.id;

        SphericalCoordinate result = new SphericalCoordinate(this);

        // check post-conditions
        assertEquals(result.getId(), this.id);

        // check class invariants
        assertInvariantId(curId);

        return result;
    }

    // ---- Field Accessors ----

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    // ---- Coordinate ID Methods ----

    public CoordinateId getId() {
        return id;
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

    // ---- Persistent Interface Methods ----

    @Override
    public String getIdAsString() {
        return String.valueOf(id);
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {

        // this will 'register' the cartesian coordinate read
        // from rset in allCartesianCoordinates
        getCartesianCoordinate(rset);
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

    // ---- Object Overrides ----

    @Override
    public String toString() {
        return String.format("CartesianCoordinate(x = %f, y = %f, z = %f)", x, y, z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, z);
    }

    // ---- Design-by-Contract Class Invariants Assertion Methods ----

    protected void assertInvariantId(CoordinateId id) {
        assert this.id == id;
    }

    // ---- Value Type Implementation Methods ----

    public static String makeKey(double x, double y, double z) {
        // convert to fixed point representation, so we
        // don't get problems with %f format specifier
        double convFactor = 1.0 / Coordinate.EPSILON;
        return String.format("CartesianCoordinate(x = %f, y = %f, z = %f)", x * convFactor, y * convFactor, z * convFactor);
    }

    public static CartesianCoordinate getCartesianCoordinate(double x, double y, double z) {
        String key = makeKey(x, y, z);
        CartesianCoordinate result = allCartesianCoordinates.get(key);
        if (result == null) {
            synchronized (CartesianCoordinate.class) {
                result = allCartesianCoordinates.get(key);
                if (result == null) {
                    result = new CartesianCoordinate(x, y, z);
                    allCartesianCoordinates.put(key, result);
                }
            }
        }
        return result;
    }

    public static CartesianCoordinate getCartesianCoordinate(ResultSet rset) throws SQLException {

        double x = rset.getDouble("x");
        double y = rset.getDouble("y");
        double z = rset.getDouble("z");
        return getCartesianCoordinate(x, y, z);
    }
}
