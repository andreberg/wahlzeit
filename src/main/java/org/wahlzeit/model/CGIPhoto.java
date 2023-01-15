package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.wahlzeit.utils.AssertUtil.*;

public class CGIPhoto extends Photo {

    public static final String SOFTWARE = "software";
    public static final String NOT_SPECIFIED = "Not specified";

    /**
     * This string is shown in place of the software name so the website user knows
     * the software name entered previously could not be fetched from DB.
     */
    public static final String ERR_MSG_READING_SOFTWARE_NAME_FROM_DB = "reading software name from database failed";

    // Initialize with a catch-all default so unit-tests continue to work
    // This should be overwritten later when the user uploads a photo with
    // the CGI Software field filled out in the UI.
    protected CGISoftware software = CGISoftwareManager.getInstance().createCGISoftware(NOT_SPECIFIED);

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
        CGISoftwareManager.getInstance().createCGISoftware(rset.getString("software_name"));
    }

    /**
     * @param photo
     * @param softwareName
     * @methodtype constructor
     */
    public CGIPhoto(Photo photo, String softwareName) {
        copyPhotoFields(photo);
        this.software = CGISoftwareManager.getInstance().createCGISoftware(softwareName);
    }

    /**
     * @Preconditions: photo != null
     * @Postconditions:
     * @Invariants: this.softwareName != null
     *
     * @param photo
     * @methodtype constructor
     */
    public CGIPhoto(Photo photo) {

        assertNotNull(photo);

        copyPhotoFields(photo);
        software = getCGISoftwareFromString(readSoftwareNameFromDBForID(photo.getId().asInt()));

        assertClassInvariants();
    }

    // --- Getters/Setters ---

    protected CGISoftware getCGISoftwareFromString(String softwareName) {
        return CGISoftwareManager.getInstance().createCGISoftware(softwareName);
    }

    public String getSoftwareName() {
        return this.software.name;
    }

    /**
     * @Preconditions: softwareName != null
     * @Postconditions:
     * @Invariants: this.softwareName != null
     */
    public void setSoftwareName(String softwareName) {
        this.software = getCGISoftwareFromString(softwareName);
        assertClassInvariants();
    }

    // --- Helper Methods ---

    /**
     * @Preconditions: photo != null
     * @Postconditions: this.id == photo.id
     * @Invariants: this.softwareName != null
     */
    protected void copyPhotoFields(Photo photo) {

        assertNotNull(photo);

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

        // this.id == photo.id is trivially valid

        assertClassInvariants();
    }

    // --- Query Methods ---

    /**
     * @Preconditions: id >= 0
     * @Postconditions: result != null && result.length() > 0
     * @Invariants: this.softwareName != null
     */
    protected String readSoftwareNameFromDBForID(int id) {

        // preconditions
        assertNonNegative(id);

        String result = ERR_MSG_READING_SOFTWARE_NAME_FROM_DB;
        try {
            PreparedStatement stmt = CGIPhotoManager.getMyInstance().exposeReadingStatement("SELECT * FROM photos WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rset = stmt.executeQuery();
            if (rset.next()) {
                result = rset.getString("software_name");
            }
        } catch (SQLException sex) {
            SysLog.logThrowable(sex);
        }

        // postconditions
        assertStringNotNullOrEmpty(result);

        assertClassInvariants();

        return result;
    }

    // --- Photo Overrides ---

    /**
     * @Preconditions: cfg != null
     * @Postconditions: result != 0 && result.length() > 0
     * @Invariants: this.softwareName != null
     */
    @Override
    public String getSummary(ModelConfig cfg) {

        // preconditions
        assertNotNull(cfg);

        String result = cfg.asCGIPhotoSummary(ownerName, this.software.name);

        // postconditions
        assertStringNotNullOrEmpty(result);

        assertClassInvariants();
        return result;
    }

    /**
     * @Preconditions: rset != null
     * @Postconditions:  this.softwareName.length() > 0
     * @Invariants: this.softwareName != null
     */
    public void readFrom(ResultSet rset) throws SQLException {

        assertNotNull(rset);

        super.readFrom(rset);
        software = getCGISoftwareFromString(rset.getString("software_name"));

        assertGreater(software.name.length(), 0);

        assertClassInvariants();
    }

    /**
     * @Preconditions: rset != null
     * @Postconditions:
     * @Invariants: this.softwareName != null
     */
    public void writeOn(ResultSet rset) throws SQLException {

        assertNotNull(rset);

        super.writeOn(rset);
        rset.updateString("software_name", software.name);

        assertClassInvariants();
    }

    public void assertClassInvariants() {
        assert this.software.name != null : "Design-by-contract violation: softwareName can't be null";
    }
}
