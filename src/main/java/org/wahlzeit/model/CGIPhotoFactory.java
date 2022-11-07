package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CGIPhotoFactory extends PhotoFactory {

    private static CGIPhotoFactory instance = null;

    /**
     * Public singleton access method.
     */
    public static synchronized CGIPhotoFactory getInstance() {
        if (instance == null) {
            SysLog.logSysInfo("setting CGI PhotoFactory");
            setInstance(new CGIPhotoFactory());
        }

        return instance;
    }

    /**
     * Method to set the singleton instance of CGIPhotoFactory.
     */
    protected static synchronized void setInstance(CGIPhotoFactory cgiPhotoFactory) {
        if (instance != null) {
            throw new IllegalStateException("attempt to initialize CGIPhotoFactory twice");
        }

        instance = cgiPhotoFactory;
    }

    /**
     * Hidden singleton instance; needs to be initialized from the outside.
     */
    public static void initialize() {
        getInstance();
    }

    public CGIPhoto createPhoto(PhotoId id) {
        return new CGIPhoto(id);
    }

    public CGIPhoto createPhoto() {
        return new CGIPhoto();
    }

    public CGIPhoto createPhoto(ResultSet rs) throws SQLException {
        return new CGIPhoto(rs);
    }

}
