package org.wahlzeit.model;

import org.wahlzeit.services.SysLog;

/**
 * <p> CGISoftware domain class. Models software used to make computer-generated images (CGI).
 *     This class is used in a type object pattern instance involving CGISoftwareType and
 *     CGISoftwareManager.
 * </p>
 * <p> This class should always be instantiated by CGISoftwareType.createInstance(...).
 * </p>
 * <p>Object instantiation during upload (bottom is most recent):
 * <pre><code>
 *   doHandlePost:55, UploadPhotoFormHandler (org.wahlzeit.handlers)
 *     createPhoto:142, CGIPhotoManager (org.wahlzeit.model)
 *       createCGIPhoto:31, PhotoUtil (org.wahlzeit.model)
 *         createPhoto:58, CGIPhotoFactory (org.wahlzeit.model)
 *           < init >:43, CGIPhoto (org.wahlzeit.model)
 *             createCGISoftware:58, CGISoftwareManager (org.wahlzeit.model)
 *             createCGISoftware:51, CGISoftwareManager (org.wahlzeit.model)
 *               createInstance:8, CGISoftwareType (org.wahlzeit.model)
 *                 < init >:14, CGISoftware (org.wahlzeit.model)
 * </code></pre>
 * <p>Object instantiation during display (bottom is most recent):
 * <pre><code>
 *   doMakeWebPart:31, ShowUserPhotoFormHandler (org.wahlzeit.handlers)
 *     < init >:43, CGIPhoto (org.wahlzeit.model) (*)
 *       createCGISoftware:58, CGISoftwareManager (org.wahlzeit.model)
 *       createCGISoftware:51, CGISoftwareManager (org.wahlzeit.model)
 *         createInstance:14, CGISoftwareType (org.wahlzeit.model)
 *           < init >:14, CGISoftware (org.wahlzeit.model)
 * </code></pre>
 * (*) uses a copy constructor that replaces a Photo with a CGIPhoto instance.
 * </p>
 * <p>Object Creation Solution</p>
 * <l>
 *     <li>Delegation: this-object</li>
 *     <li>Selection: by-mapping</li>
 *     <li>Configuration: in-code</li>
 *     <li>Instantiation: by-class-object</li>
 *     <li>Initialization: default</li>
 *     <li>Building: default, by-building</li>
 * </l>
 */
public class CGISoftware {

    protected CGISoftwareType cgiSoftwareType;

    // type information
    public String name;

    public CGISoftware(CGISoftwareType ct, String name) {
        cgiSoftwareType = ct;
        this.name = name;
        SysLog.logSysInfo(String.format("CGISoftware(CGISoftwareType ct) constructor called. ct = %h, name = %s", ct, name));
    }

    public void setName(String newName) {
        SysLog.logSysInfo(String.format("CGISoftware.setName(%s)", newName));
        name = newName;
    }
}
