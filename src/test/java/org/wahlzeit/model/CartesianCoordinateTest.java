package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class CartesianCoordinateTest {

    @Test(expected = AssertionError.class)
    public void constructorWithNaNShouldThrowException() {
        new CartesianCoordinate(Double.NaN, 0, 0);
    }

    @Test(expected = AssertionError.class)
    public void constructorWithNegInfinityShouldThrowException() {
        new CartesianCoordinate(0, Double.NEGATIVE_INFINITY, 0);
    }

    @Test(expected = AssertionError.class)
    public void constructorWithPosInfinityShouldThrowException() {
        new CartesianCoordinate(0, 0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testConstructorWithNegativeArgs() {
        assertTrue(new CartesianCoordinate(-1, -100, -1000).isEqual(new CartesianCoordinate(0,0,0)));
    }

    // somewhat unneccesary it feels like...
    @Test
    public void testConstructorWithLargeArgs() {
        assertTrue(new CartesianCoordinate(100.0E300, 175.0E306, 0).isEqual(new CartesianCoordinate(100.0E300,175.0E306,0)));
    }

    @Test
    public void testConstructorWithNominalArgs() {
        assertTrue(new CartesianCoordinate(150.3456, 3.14159, 22.0/7.0).isEqual(new CartesianCoordinate(150.3456,3.14159,22.0/7.0)));
    }

    @Test
    public void testGetDistance() {
        CartesianCoordinate a = new CartesianCoordinate(2, 3, 1);
        CartesianCoordinate b = new CartesianCoordinate(8,5,0);
        double distance = a.getCartesianDistance(b);
        assertEquals(6.4031242374328485, distance, Coordinate.EPSILON);
    }

    @Test
    public void testGetDistanceWithNegativeArgs() {
        CartesianCoordinate a = new CartesianCoordinate(2, 3, 1);
        CartesianCoordinate b = new CartesianCoordinate(8,-5,0);
        double distance = a.getCartesianDistance(b);
        assertEquals(6.782329983125268, distance, Coordinate.EPSILON);
    }
}
