package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CoordinateTest {

    public static final double eps = 1e-14;

    @Test
    public void testSphericalToCartesianConversion() {
        SphericalCoordinate a = SphericalCoordinate.getSphericalCoordinate(30,60,5);
        CartesianCoordinate aAsCartesian = a.asCartesianCoordinate();
        assertEquals(1.25, aAsCartesian.getX(), eps);
        assertEquals(2.165063509461096, aAsCartesian.getY(), eps);
        assertEquals(4.330127018922194, aAsCartesian.getZ(), eps);
    }

    @Test
    public void testCartesianToSphericalConversion() {
        CartesianCoordinate a = CartesianCoordinate.getCartesianCoordinate(1.25,2.165063509461096,4.330127018922194);
        SphericalCoordinate aAsSpherical = a.asSphericalCoordinate();
        double phi_degrees = Math.toDegrees(aAsSpherical.getPhi());
        double theta_degrees = Math.toDegrees(aAsSpherical.getTheta());
        double radius = aAsSpherical.getRadius();
        // System.out.println(aAsSpherical);
        assertEquals(30, phi_degrees, eps);
        assertEquals(60,theta_degrees, eps);
        assertEquals(5, radius, eps);
    }

    @Test
    public void testCentralAngleBetweenTwoCartesianCoords() {
        Coordinate a = CartesianCoordinate.getCartesianCoordinate(1,2,3);
        Coordinate b = CartesianCoordinate.getCartesianCoordinate(0,0,0);
        double sphericalDist = a.getCentralAngle(b);
        // System.out.format("\nsphericalDist = %s\n", sphericalDist);
        assertEquals(1.204062267702623, sphericalDist, eps);
    }

    @Test
    public void testCartesianDistanceBetweenTwoSphericalCoords() {
        Coordinate a = SphericalCoordinate.getSphericalCoordinate(27,33,1);
        Coordinate b = SphericalCoordinate.getSphericalCoordinate(0,0,0);
        double cartesianDist = a.getCartesianDistance(b);
        // System.out.format("\ncartesianDist = %s\n", cartesianDist);
        assertEquals(1, cartesianDist, eps);
    }
}
