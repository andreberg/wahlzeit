package org.wahlzeit.model;

import static org.wahlzeit.utils.AssertUtil.*;

public class CoordinateId {

    public static final CoordinateId NULL_ID = new CoordinateId(0);

    private final int id;

    public CoordinateId(int id) {
        this.id = id;
    }

    public int asInt() {
        return id;
    }

    /**
     * @Preconditions:
     * @Postconditions: result.id == id + 1
     * @Invariants:
     */
    public CoordinateId getNextId() {

        CoordinateId result = new CoordinateId(id + 1);

        assertPlusOne(result.id, id);

        return result;
    }

    /**
     * @Preconditions:
     * @Postconditions: result.id >= 0
     * @Invariants:
     */
    public static CoordinateId getIdFromInt(int id) {

        if (id < 0) {
            return NULL_ID;
        }

        // postconditions trivially true

        return new CoordinateId(id);
    }

    // --- Object Overrides ---

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CoordinateId other = (CoordinateId) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
