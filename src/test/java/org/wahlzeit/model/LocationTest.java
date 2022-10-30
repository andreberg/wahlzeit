package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LocationTest {

    @Test
    public void testDefaultConstructor() {
        assertTrue(new Location().getCoordinate().isEqual(new Coordinate(0,0,0)));
    }

    @Test
    public void testCoordinateConstructor() {
        assertTrue(new Location(new Coordinate(1,2,3)).getCoordinate().isEqual(new Coordinate(1,2,3)));
    }

    @Test
    public void testSetNewCoordinate() {
        Location loc = new Location();
        loc.setCoordinate(new Coordinate(1,2,3));
        assertTrue(loc.getCoordinate().isEqual(new Coordinate(1,2,3)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCoordinateWithNullShouldThrowException() {
        Location loc = new Location();
        loc.setCoordinate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNullCoordShouldTrowException() {
        new Location((Coordinate) null);
    }
}
