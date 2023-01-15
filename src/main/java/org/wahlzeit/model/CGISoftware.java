package org.wahlzeit.model;

public class CGISoftware {

    protected CGISoftwareType cgiSoftwareType;

    protected CGISoftwareId id;

    // type information
    public String name;

    public CGISoftware(CGISoftwareType ct) {
        cgiSoftwareType = ct;
        id = CGISoftwareId.getNextId();
    }

    public CGISoftwareId getId() {
        return id;
    }
}
