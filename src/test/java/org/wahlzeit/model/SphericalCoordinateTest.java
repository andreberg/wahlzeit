package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class SphericalCoordinateTest {

    @Test(expected = AssertionError.class)
    public void constructorWithNaNShouldThrowException() {
        SphericalCoordinate.getSphericalCoordinate(Double.NaN, 0, 0);
    }

    @Test(expected = AssertionError.class)
    public void constructorWithNegInfinityShouldThrowException() {
        SphericalCoordinate.getSphericalCoordinate(0, Double.NEGATIVE_INFINITY, 0);
    }

    @Test(expected = AssertionError.class)
    public void constructorWithPosInfinityShouldThrowException() {
        SphericalCoordinate.getSphericalCoordinate(0, 0, Double.POSITIVE_INFINITY);
    }

    @Test
    public void testConstructorWithNegativeArgs() {
        assertTrue(SphericalCoordinate.getSphericalCoordinate(-1, -100, -1000).isEqual(SphericalCoordinate.getSphericalCoordinate(-1,-100,-1000)));
    }

    @Test
    public void testConstructorWithLargeArgs() {
        assertTrue(SphericalCoordinate.getSphericalCoordinate(100.0E300, 175.0E306, 0).isEqual(SphericalCoordinate.getSphericalCoordinate(100.0E300,175.0E306,0)));
    }

    @Test
    public void testConstructorWithNominalArgs() {
        assertTrue(SphericalCoordinate.getSphericalCoordinate(150.3456, 3.14159, 22.0/7.0).isEqual(SphericalCoordinate.getSphericalCoordinate(150.3456,3.14159,22.0/7.0)));
    }

    @Test
    public void testGetCentralAngle() {
        SphericalCoordinate a = SphericalCoordinate.getSphericalCoordinate(0, 0, 1);
        SphericalCoordinate b = SphericalCoordinate.getSphericalCoordinate(0,90,1);
        double distance = a.getCentralAngle(b);
        // System.out.println(distance);
        assertEquals(Math.PI*0.5, distance, Coordinate.EPSILON);
    }

    @Test
    public void testGetCentralAngleWithNegativeArgs() {
        SphericalCoordinate a = SphericalCoordinate.getSphericalCoordinate(0, 0, 1);
        SphericalCoordinate b = SphericalCoordinate.getSphericalCoordinate(0,-180,1);
        double distance = a.getCentralAngle(b);
        // System.out.println(distance);
        assertEquals(Math.PI, distance, Coordinate.EPSILON);
    }
}
