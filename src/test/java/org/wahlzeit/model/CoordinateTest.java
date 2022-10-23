package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CoordinateTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNaNShouldThrowException() {
        new Coordinate(Double.NaN, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNegInfinityShouldThrowException() {
        new Coordinate(0, Double.NEGATIVE_INFINITY, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithPosInfinityShouldThrowException() {
        new Coordinate(0, 0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testConstructorWithNegativeArgs() {
        assertTrue(new Coordinate(-1, -100, -1000).isEqual(new Coordinate(0,0,0)));
    }

    // somewhat unneccesary it feels like...
    @Test
    public void testConstructorWithLargeArgs() {
        assertTrue(new Coordinate(100.0E300, 175.0E306, 0).isEqual(new Coordinate(100.0E300,175.0E306,0)));
    }

    @Test
    public void testConstructorWithNominalArgs() {
        assertTrue(new Coordinate(150.3456, 3.14159, 22.0/7.0).isEqual(new Coordinate(150.3456,3.14159,22.0/7.0)));
    }
}
