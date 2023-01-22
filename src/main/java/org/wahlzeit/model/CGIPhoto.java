package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.wahlzeit.utils.AssertUtil.*;

/**
 * <p> Photo class that models Computer-generated Images (CGI).
 *     Has an additional attribute for specifying the software used to generate the image.
 * </p>
 *
 * <p> The software used to make this image can be set in the webPart UI during upload.
 *     If the user doesn't specify anything, the default value is "Not specified".
 * </p>
 * <p>
 *     The software attribute is implemented as a <i>type object pattern</i>.
 *     A CGISoftwareManager maps CGISoftwareType type objects to CGISoftware domain objects. <br/>
 *     Each CGISoftware has a String field 'name' which is set from the text entered into the
 *     'Made with: ' input box by the user on upload and then later queried from the database
 *     when displayed in a webPart.
 * </p>
 * <p>Object instantiation during upload (bottom is most recent):
 * <pre><code>
 *   doHandlePost:55, UploadPhotoFormHandler (org.wahlzeit.handlers)
 *     createPhoto:142, CGIPhotoManager (org.wahlzeit.model)
 *       createCGIPhoto:31, PhotoUtil (org.wahlzeit.model)
 *         createPhoto:58, CGIPhotoFactory (org.wahlzeit.model)
 *           < init >:43, CGIPhoto (org.wahlzeit.model)
 * </code></pre>
 * <p>Object instantiation during display (bottom is most recent):
 * <pre><code>
 *   doMakeWebPart:31, ShowUserPhotoFormHandler (org.wahlzeit.handlers)
 *     < init >:43, CGIPhoto (org.wahlzeit.model) (*)
 * </code></pre>
 * (*) uses a copy constructor that replaces a Photo with a CGIPhoto instance.
 * </p>
 * <p>Object Creation Solution</p>
 * <l>
 *     <li>Delegation: separate-object</li>
 *     <li>Selection: on-the-spot, by-subclassing</li>
 *     <li>Configuration: in-code</li>
 *     <li>Instantiation: by-class-object</li>
 *     <li>Initialization: default, in-second-step</li>
 *     <li>Building: default</li>
 * </l>
 */
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
        SysLog.logSysInfo("CGIPhoto() default constructor called.");
    }

    /**
     * @param myId
     * @methodtype constructor
     */
    public CGIPhoto(PhotoId myId) {
        super(myId);
        SysLog.logSysInfo("CGIPhoto(PhotoId myId) constructor called.");
    }

    /**
     * @param rset
     * @methodtype constructor
     */
    public CGIPhoto(ResultSet rset) throws SQLException {
        super(rset);
        SysLog.logSysInfo("CGIPhoto(ResultSet rset) constructor called.");
        CGISoftwareManager.getInstance().createCGISoftware(rset.getString("software_name"));
    }

    /**
     * @param photo
     * @param softwareName
     * @methodtype constructor
     */
    public CGIPhoto(Photo photo, String softwareName) {
        copyPhotoFields(photo);
        SysLog.logSysInfo("CGIPhoto(Photo photo, String softwareName) copy constructor called.");
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
        SysLog.logSysInfo("CGIPhoto(Photo photo) copy constructor called.");

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
