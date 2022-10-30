package org.wahlzeit.model;

public class LocationId {

    public static final LocationId NULL_ID = new LocationId(0);

    private final int id;

    public LocationId(int id) {
        this.id = id;
    }

    public int asInt() {
        return id;
    }

    public LocationId getNextId() {
        return new LocationId(id + 1);
    }

    public static LocationId getIdFromInt(int id) {

        if (id < 0) {
            return NULL_ID;
        }
        return new LocationId(id);
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
        LocationId other = (LocationId) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
