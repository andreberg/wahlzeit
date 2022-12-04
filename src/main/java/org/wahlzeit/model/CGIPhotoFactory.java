package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.wahlzeit.utils.AssertUtil.*;

public class CGIPhotoFactory extends PhotoFactory {

    private static CGIPhotoFactory instance = null;

    /**
     * Public singleton access method.
     */
    public static synchronized CGIPhotoFactory getInstance() throws IllegalStateException {
        if (instance == null) {
            SysLog.logSysInfo("setting CGI PhotoFactory");
            setInstance(new CGIPhotoFactory());
        }

        return instance;
    }

    /**
     * @Preconditions: instance == null
     * @Postconditions: instance != null
     * @Invariants:
     */
    protected static synchronized void setInstance(CGIPhotoFactory cgiPhotoFactory) {

        assertNull(instance);

        if (instance != null) {
            throw new IllegalStateException("attempt to initialize CGIPhotoFactory twice");
        }

        instance = cgiPhotoFactory;

        assertNotNull(instance);
    }

    /**
     * Hidden singleton instance; needs to be initialized from the outside.
     */
    public static void initialize() {
        getInstance();
    }

    // --- PhotoFactory Overrides ---

    public CGIPhoto createPhoto() {
        return new CGIPhoto();
    }

    public CGIPhoto createPhoto(PhotoId id) {
        return new CGIPhoto(id);
    }


    public CGIPhoto createPhoto(ResultSet rs) throws SQLException {
        return new CGIPhoto(rs);
    }

}
