package org.wahlzeit.model;

public class CoordinateId {

    public static final CoordinateId NULL_ID = new CoordinateId(0);

    private final int id;

    public CoordinateId(int id) {
        this.id = id;
    }

    public int asInt() {
        return id;
    }

    public CoordinateId getNextId() {
        return new CoordinateId(id + 1);
    }

    public static CoordinateId getIdFromInt(int id) {

        if (id < 0) {
            return NULL_ID;
        }
        return new CoordinateId(id);
    }

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
