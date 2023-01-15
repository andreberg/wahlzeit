package org.wahlzeit.model;

public class CGISoftwareId {

    public static final CGISoftwareId NULL_ID = new CGISoftwareId(0);

    public static int curId = 0;

    private final int id;

    public CGISoftwareId(int id) {
        this.id = id;
    }

    public int asInt() {
        return id;
    }

    public synchronized static CGISoftwareId getNextId() {
        curId = curId + 1;
        return new CGISoftwareId(curId);
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
        CGISoftwareId other = (CGISoftwareId) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}