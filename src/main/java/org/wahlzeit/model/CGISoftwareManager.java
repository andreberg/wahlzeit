package org.wahlzeit.model;

import org.wahlzeit.services.ObjectManager;
import org.wahlzeit.services.Persistent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CGISoftwareManager extends ObjectManager {

    protected static final CGISoftwareManager instance = new CGISoftwareManager();

    protected CGISoftwareManager() {}

    @Override
    protected Persistent createObject(ResultSet rset) throws SQLException {
        // TODO: add DB serialization
        // wait, do we actually need to implement this? All the DB serialization is happening
        // on the CGIPhoto side and the types are being registerred at runtime.
        // This manager being a singleton instance it will get lazily initialized upon first use...
        return null;
    }

    protected Map<CGISoftwareId, CGISoftware> cgiSoftwareCache = new HashMap<>();
    protected Map<String, CGISoftwareType> cgiSoftwareTypes = new HashMap<>();

    public static CGISoftwareManager getInstance() {
        return instance;
    }

    /**
     * Checks registry if a CGISoftware for typeName (incl parentTypeName if given)
     * exists and creates it if it doesn't exist.
     * @param typeName the name of the CGI software (e.g. Adobe Photoshop)
     * @param parentTypeName the parent type name of the CGI software (e.g. CGI Software 2D)
     * @return the cached or newly created CGI Software instance
     */
    public CGISoftware createCGISoftware(String typeName, String parentTypeName) {

        CGISoftware cs = getCGISoftware(typeName, parentTypeName);
        if (cs != null) return cs;

        assertIsValidCGITypeName(typeName);
        CGISoftwareType ct = getCGISoftwareType(typeName);
        if (parentTypeName != null && cgiSoftwareTypes.containsKey(parentTypeName)) {
            assertIsValidCGITypeName(parentTypeName);
            ct.parentType = cgiSoftwareTypes.get(parentTypeName);
        }
        cs = ct.createInstance();
        cs.name = typeName;
        cgiSoftwareCache.put(cs.getId(), cs);
        return cs;
    }

    public CGISoftware createCGISoftware(String typeName) {
        return createCGISoftware(typeName, null);
    }

    private CGISoftware getCGISoftware(String typeName, String parentTypeName) {
        if (cgiSoftwareCache.containsKey(typeName)) {
            CGISoftware cgiSoftware = cgiSoftwareCache.get(typeName);
            if (null != parentTypeName && cgiSoftwareTypes.containsKey(parentTypeName)) {
                // If a parentType is supplied only return registered cgiSoftware
                // given that its parentType matches
                CGISoftwareType parentType = cgiSoftwareTypes.get(parentTypeName);
                if (cgiSoftware.cgiSoftwareType.equals(parentType)) {
                    return cgiSoftware;
                }
            } else {
                return cgiSoftware;
            }
        }
        return null;
    }

    CGISoftwareType getCGISoftwareType(String typeName) {
        if (!cgiSoftwareTypes.containsKey(typeName)) {
            // typeName doesn't exist yet, create a new type
            // and add it to the known types
            CGISoftwareType ct = new CGISoftwareType();
            ct.manager = this;
            cgiSoftwareTypes.put(typeName, ct);
            return ct;
        }
        return cgiSoftwareTypes.get(typeName);
    }

    void assertIsValidCGITypeName(String typeName) {
        // assert cgiSoftwareTypes.containsKey(typeName);
        //
        // I am not entirely sure what to check here... if we want to
        // have dynamic run-time creation of new instances
        // based on an exploding model type hierarchy (i.e. a CGISoftwareType
        // for Adobe Photoshop, one for GIMP, one for blender etc.) then
        // I can't do the check above... that would defeat the purpose
        // of the whole exercise if I only allowed CGISoftwareTypes that
        // I think of beforehand and 'hard-code' in.
        //
        // Maybe check if the (type)name satisfies a specific naming convention
        // something like the name attribute of a CGISoftwareType has to
        // be two parts, first part for the company that makes the software
        // second part for the name of the software itself...
        //
        // I am omitting this check here since it would just be a simple
        // string based comparison check.
    }
}
