package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CGIPhoto extends Photo {

    public static final String SOFTWARE = "software";

    protected String softwareName = "";

    public CGIPhoto() {
    }

    /**
     * @param myId
     * @methodtype constructor
     */
    public CGIPhoto(PhotoId myId) {
        super(myId);
    }

    /**
     * @param rset
     * @methodtype constructor
     */
    public CGIPhoto(ResultSet rset) throws SQLException {
        super(rset);
        softwareName = rset.getString("software_name");
    }

    protected void copyPhotoFields(Photo photo) {

        id = photo.id;
        ownerId = photo.ownerId;
        ownerName = photo.ownerName;
        ownerNotifyAboutPraise = photo.ownerNotifyAboutPraise;
        ownerEmailAddress = photo.ownerEmailAddress;
        ownerLanguage = photo.ownerLanguage;
        ownerHomePage = photo.ownerHomePage;
        width = photo.width;
        height = photo.height;
        maxPhotoSize = photo.maxPhotoSize;
        tags = photo.tags;
        location = photo.location;
        status = photo.status;
        praiseSum = photo.praiseSum;
        noVotes = photo.noVotes;
        creationTime = photo.creationTime;
    }

    public CGIPhoto(Photo photo, String softwareName) {
        copyPhotoFields(photo);
        this.softwareName = softwareName;
    }

    protected String readSoftwareNameFromDBForID(int id) {
        try {
            PreparedStatement stmt = CGIPhotoManager.getMyInstance().exposeReadingStatement("SELECT * FROM photos WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                softwareName = rset.getString("software_name");
            }
            return softwareName;
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }
        return null;
    }

    public CGIPhoto(Photo photo) {
        copyPhotoFields(photo);
        softwareName = readSoftwareNameFromDBForID(photo.getId().asInt());
    }

    @Override
    public String getSummary(ModelConfig cfg) {
        return cfg.asCGIPhotoSummary(ownerName, softwareName);
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public void readFrom(ResultSet rset) throws SQLException {
        super.readFrom(rset);
        softwareName = rset.getString("software_name");
    }

    public void writeOn(ResultSet rset) throws SQLException {
        super.writeOn(rset);
        rset.updateString("software_name", softwareName);
    }

}
