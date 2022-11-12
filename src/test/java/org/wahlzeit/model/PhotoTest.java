package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class PhotoTest {

    @Test
    public void testPhotoHasNoLocationByDefault() {
        Photo photo = new Photo();
        assertFalse(photo.hasLocation());
    }

    @Test
    public void testSetLocation() {
        Photo photo = new Photo();
        photo.setLocation(new Location());
        assertTrue(photo.hasLocation());
    }

    @Test
    public void testGetLocation() {
        Photo photo = new Photo();
        assertNull(photo.getLocation());
        Location loc = new Location();
        loc.setCoordinate(new CartesianCoordinate(1,2,3));
        photo.setLocation(loc);
        assertTrue(photo.getLocation().isEqual(loc));
    }
}
