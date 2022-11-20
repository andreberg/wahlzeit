package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class SphericalCoordinateTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNaNShouldThrowException() {
        new SphericalCoordinate(Double.NaN, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithNegInfinityShouldThrowException() {
        new SphericalCoordinate(0, Double.NEGATIVE_INFINITY, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithPosInfinityShouldThrowException() {
        new SphericalCoordinate(0, 0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testConstructorWithNegativeArgs() {
        assertTrue(new SphericalCoordinate(-1, -100, -1000).isEqual(new SphericalCoordinate(-1,-100,-1000)));
    }

    @Test
    public void testConstructorWithLargeArgs() {
        assertTrue(new SphericalCoordinate(100.0E300, 175.0E306, 0).isEqual(new SphericalCoordinate(100.0E300,175.0E306,0)));
    }

    @Test
    public void testConstructorWithNominalArgs() {
        assertTrue(new SphericalCoordinate(150.3456, 3.14159, 22.0/7.0).isEqual(new SphericalCoordinate(150.3456,3.14159,22.0/7.0)));
    }

    @Test
    public void testGetCentralAngle() {
        SphericalCoordinate a = new SphericalCoordinate(0, 0, 1);
        SphericalCoordinate b = new SphericalCoordinate(0,90,1);
        double distance = a.getCentralAngle(b);
        // System.out.println(distance);
        assertEquals(Math.PI*0.5, distance, Coordinate.EPSILON);
    }

    @Test
    public void testGetCentralAngleWithNegativeArgs() {
        SphericalCoordinate a = new SphericalCoordinate(0, 0, 1);
        SphericalCoordinate b = new SphericalCoordinate(0,-180,1);
        double distance = a.getCentralAngle(b);
        // System.out.println(distance);
        assertEquals(Math.PI, distance, Coordinate.EPSILON);
    }
}
