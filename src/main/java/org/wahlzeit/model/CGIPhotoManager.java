package org.wahlzeit.model;

import org.wahlzeit.main.ServiceMain;
import org.wahlzeit.services.SysLog;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class CGIPhotoManager extends PhotoManager {

    protected static final CGIPhotoManager instance = new CGIPhotoManager();
    protected Map<PhotoId, CGIPhoto> photoCache = new HashMap<>();

    public PreparedStatement exposeReadingStatement(String stmt) throws SQLException {
        return getReadingStatement(stmt);
    }

    public static CGIPhotoManager getMyInstance() {
        return instance;
    }

    protected CGIPhoto doGetPhotoFromId(PhotoId id) {
        return photoCache.get(id);
    }

    public CGIPhoto getPhotoFromId(PhotoId id) {

        if (id.isNullId()) {
            return null;
        }

        CGIPhoto result = doGetPhotoFromId(id);
        if (result == null) {
            try {
                PreparedStatement stmt = getReadingStatement("SELECT * FROM photos WHERE id = ?");
                Photo _photo = (Photo) readObject(stmt, id.asInt());
                result = new CGIPhoto(_photo);
            } catch (SQLException sex) {
                SysLog.logThrowable(sex);
            }
            if (result != null) {
                doAddPhoto(result);
            }
        }

        return result;
    }

    protected void doAddPhoto(CGIPhoto myPhoto) {
        photoCache.put(myPhoto.getId(), myPhoto);
    }

    public void addPhoto(CGIPhoto photo) {

        PhotoId id = photo.getId();
        assertIsNewPhoto(id);
        doAddPhoto(photo);

        try {
            PreparedStatement stmt = getReadingStatement("INSERT INTO photos(id) VALUES(?)");
            createObject(photo, stmt, id.asInt());
            ServiceMain.getInstance().saveGlobals();
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
    }

    public Set<CGIPhoto> findPhotosBySoftware(String softwareName) {

        Set<CGIPhoto> result = new HashSet<>();

        try {
            PreparedStatement stmt = getReadingStatement("SELECT * FROM photos WHERE software_name = ?");
            readObjects(result, stmt, softwareName);
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }

        for (CGIPhoto cgiPhoto : result) {
            doAddPhoto(cgiPhoto);
        }

        return result;
    }

    public CGIPhoto getVisiblePhoto(PhotoFilter filter) {

        CGIPhoto result = getPhotoFromFilter(filter);

        if(result == null) {
            java.util.List<PhotoId> list = getFilteredPhotoIds(filter);
            filter.setDisplayablePhotoIds(list);
            result = getPhotoFromFilter(filter);
        }

        return result;
    }

    protected CGIPhoto getPhotoFromFilter(PhotoFilter filter) {

        PhotoId id = filter.getRandomDisplayablePhotoId();

        CGIPhoto result = getPhotoFromId(id);

        while((result != null) && !result.isVisible()) {
            id = filter.getRandomDisplayablePhotoId();
            result = getPhotoFromId(id);
            if ((result != null) && !result.isVisible()) {
                filter.addProcessedPhoto(result);
            }
        }

        return result;
    }

    public CGIPhoto createPhoto(File file) throws Exception {
        PhotoId id = PhotoId.getNextId();
        CGIPhoto result = PhotoUtil.createCGIPhoto(file, id);
        addPhoto(result);
        return result;
    }

    public void savePhoto(CGIPhoto photo) {

        try {
            PreparedStatement stmt = getUpdatingStatement("SELECT * FROM photos WHERE id = ?");
            updateObject(photo, stmt);
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
    }
}
