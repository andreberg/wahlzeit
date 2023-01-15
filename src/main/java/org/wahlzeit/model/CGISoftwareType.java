package org.wahlzeit.model;

public class CGISoftwareType {
    public CGISoftwareManager manager = null;
    public CGISoftwareType parentType = null;

    public CGISoftware createInstance() {
        return new CGISoftware(this);
    }

    public boolean isSubtype() {
        return null != parentType;
    }
}
