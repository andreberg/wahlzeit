package org.wahlzeit.model;

import java.lang.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static org.wahlzeit.utils.AssertUtil.*;

public class CartesianCoordinate extends AbstractCoordinate {

    protected CoordinateId id;
    protected static CoordinateId lastCoordinateId = new CoordinateId(0);

    private double x = 0, y = 0, z = 0;

    public CartesianCoordinate(ResultSet rset) throws SQLException {
        readFrom(rset);
    }

    /**
     * @Preconditions: myId != null
     * @Postconditions: this.id == myId && this.writeCount == old(this.writeCount) + 1
     * @Invariants:
     */
    public CartesianCoordinate(CoordinateId myId) {

        final int oldWriteCount = writeCount;

        // check pre-conditions
        assertNotNull(myId);

        id = myId;
        incWriteCount();

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
        this(sc.getId());  // calls assertNotNull(cc), assertWriteCountPlusOne(...) -- this(...) has to be first line

        double phi = sc.getPhi();
        double theta = sc.getTheta();
        double radius = sc.getRadius();
        double sin_phi = Math.sin(phi);
        x = radius * sin_phi * Math.cos(theta);
        y = radius * sin_phi * Math.sin(theta);
        z = radius * Math.cos(phi);

        // check post-conditions
        assertEquals(this.id.asInt(), sc.getId().asInt());
        // assertWriteCountPlusOne checked in first-line constructor call
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
    public CartesianCoordinate(double x, double y, double z) {

        // if (!Double.isFinite(x)) throw new IllegalArgumentException("x is not finite!");
        // if (!Double.isFinite(y)) throw new IllegalArgumentException("y is not finite!");
        // if (!Double.isFinite(z)) throw new IllegalArgumentException("z is not finite!");

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
}
