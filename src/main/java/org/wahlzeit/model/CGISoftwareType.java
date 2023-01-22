package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

public class CGISoftwareType {
    public CGISoftwareManager manager = null;
    public CGISoftwareType parentType = null;

    public CGISoftwareType() {
        SysLog.logSysInfo("CGISoftwareType() default constructor called.");
    }

    public CGISoftware createInstance(String name) {
        return new CGISoftware(this, name);
    }

    public boolean isSubtype() {
        return null != parentType;
    }
}
