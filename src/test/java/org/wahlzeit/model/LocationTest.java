package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LocationTest {

    @Test
    public void testDefaultConstructor() {
        assertTrue(new Location().getCoordinate().isEqual(new CartesianCoordinate(0,0,0)));
    }

    @Test
    public void testCoordinateConstructor() {
        assertTrue(new Location(new CartesianCoordinate(1,2,3)).getCoordinate().isEqual(new CartesianCoordinate(1,2,3)));
    }

    @Test
    public void testSetNewCoordinate() {
        Location loc = new Location();
        loc.setCoordinate(new CartesianCoordinate(1,2,3));
        assertTrue(loc.getCoordinate().isEqual(new CartesianCoordinate(1,2,3)));
    }

    @Test(expected = AssertionError.class)
    public void setCoordinateWithNullShouldThrowException() {
        Location loc = new Location();
        loc.setCoordinate(null);
    }

    @Test(expected = AssertionError.class)
    public void constructorWithNullCoordShouldTrowException() {
        new Location((CartesianCoordinate) null);
    }
}
