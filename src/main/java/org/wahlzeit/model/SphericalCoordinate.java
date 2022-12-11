package org.wahlzeit.model;

import static org.wahlzeit.utils.AssertUtil.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SphericalCoordinate extends AbstractCoordinate {

    protected final CoordinateId id;
    protected static CoordinateId lastCoordinateId = new CoordinateId(0);

    public static Map<String, SphericalCoordinate> allSphericalCoordinates = new HashMap<>();

    private final double phi, theta, radius;

    private SphericalCoordinate(ResultSet rset) throws SQLException {

        id = new CoordinateId(rset.getInt("id"));
        phi = rset.getDouble("x");
        theta = rset.getDouble("y");
        radius = rset.getDouble("z");
    }

    /**
     * @Preconditions: myId != null
     * @Postconditions: this.id == myId && this.writeCount == old(this.writeCount) + 1
     * @Invariants:
     */
    public SphericalCoordinate(CoordinateId myId) {

        final int oldWriteCount = writeCount;

        // check pre-conditions
        assertNotNull(myId);

        id = myId;
        incWriteCount();

        phi = theta = radius = 0;

        // check post-conditions
        assertEquals(this.id, myId);
        assertWriteCountPlusOne(oldWriteCount);
    }

    /**
     * @Preconditions: cc != null
     * @Postconditions: this.id == cc.id &&
     *                  this.writeCount == old(this.writeCount) + 1 &&
     *                  Double.isFinite(this.phi) &&
     *                  Double.isFinite(this.theta) &&
     *                  Double.isFinite(this.radius)
     * @Invariants:
     */
    public SphericalCoordinate(CartesianCoordinate cc) {

        final int oldWriteCount = writeCount;

        id = cc.getId();
        incWriteCount();

        // check pre-conditions
        assertNotNull(cc.getId());

        double x = cc.getX();
        double y = cc.getY();
        double z = cc.getZ();
        radius = Math.sqrt(x * x + y * y + z * z);
        theta = Math.atan2(y, x);
        phi = Math.atan2(Math.sqrt(x * x + y * y), z);

        // check post-conditions
        assertEquals(this.id.asInt(), cc.getId().asInt());
        assertWriteCountPlusOne(oldWriteCount);
        assertIsFinite(this.phi);
        assertIsFinite(this.theta);
        assertIsFinite(this.radius);
    }

    /**
     * @Preconditions: Double.isFinite(phi_degrees) &&
     *                 Double.isFinite(theta_degrees) &&
     *                 Double.isFinite(radius)
     * @Postconditions: this.id == this.lastCoordinateId + 1 &&
     *                  this.writeCount == old(this.writeCount) + 1 &&
     *                  Double.isFinite(this.phi) &&
     *                  Double.isFinite(this.theta) &&
     *                  Double.isFinite(this.radius)
     * @Invariants:
     */
    private SphericalCoordinate(double phi_degrees, double theta_degrees, double radius) {

        final int oldWriteCount = writeCount;

        // check pre-conditions
        assertIsFinite(phi_degrees);
        assertIsFinite(theta_degrees);
        assertIsFinite(radius);

        this.phi = Math.toRadians(phi_degrees);
        this.theta = Math.toRadians(theta_degrees);
        this.radius = radius;

        id = getNextCoordinateId();
        incWriteCount();

        // check post-conditions
        assertIsFinite(this.phi);
        assertIsFinite(this.theta);
        assertIsFinite(this.radius);
        assertEquals(this.id.asInt(), lastCoordinateId.asInt());
        assertWriteCountPlusOne(oldWriteCount);
    }

    /**
     * @Preconditions:
     * @Postconditions: result.id == this.id
     * @Invariants: this.id
     */
    @Override
    public CartesianCoordinate asCartesianCoordinate() {

        final CoordinateId curId = this.id;

        CartesianCoordinate result = new CartesianCoordinate(this);

        // check post-conditions
        assertEquals(result.getId(), this.id);

        // check class invariants
        assertInvariantId(curId);

        return result;
    }

    /**
     * @Preconditions:
     * @Postconditions: result.id == this.id
     * @Invariants: this.id
     */
    @Override
    public SphericalCoordinate asSphericalCoordinate() {

        // don't need to check anything here, since this
        // just returns itself, so that pre- and postconditions
        // are trivially satisfied.
        return this;
    }

    // ---- Field Accessors ----

    public double getPhi() {
        return phi;
    }

    public double getTheta() {
        return theta;
    }

    public double getRadius() {
        return radius;
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

    static CoordinateId getLastCoordinateId() {
        return lastCoordinateId;
    }

    static void setLastCoordinateId(CoordinateId newId) {
        lastCoordinateId = newId;
    }

    // ---- Persistent Interface Methods ----

    @Override
    public String getIdAsString() {
        return String.valueOf(id);
    }

    @Override
    public void readFrom(ResultSet rset) throws SQLException {

        // this will 'register' the spherical coordinate read
        // from rset in allSphericalCoordinates
        getSphericalCoordinate(rset);
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

    // ---- Object Overrides ----

    @Override
    public String toString() {
        return String.format("SphericalCoordinate(phi_degrees = %f, theta_degrees = %f, radius = %f)", Math.toDegrees(phi), Math.toDegrees(theta), radius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phi, theta, radius);
    }

    // ---- Design-by-Contract Class Invariants Assertion Methods ----

    protected void assertInvariantId(CoordinateId id) {
        assert this.id == id;
    }

    // ---- Value Type Implementation Methods ----

    public static SphericalCoordinate getSphericalCoordinate(double phi, double theta, double radius) {
        String key = makeKey("SphericalCoordinate", phi, theta, radius);
        SphericalCoordinate result = allSphericalCoordinates.get(key);
        if (result == null) {
            synchronized (SphericalCoordinate.class) {
                result = allSphericalCoordinates.get(key);
                if (result == null) {
                    result = new SphericalCoordinate(phi, theta, radius);
                    allSphericalCoordinates.put(key, result);
                }
            }
        }
        return result;
    }

    public static SphericalCoordinate getSphericalCoordinate(ResultSet rset) throws SQLException {

        double phi = rset.getDouble("x");
        double theta = rset.getDouble("y");
        double radius = rset.getDouble("z");
        return getSphericalCoordinate(phi, theta, radius);
    }
}
