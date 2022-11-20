package org.wahlzeit.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AbstractCoordinateTest {

    @Test
    public void testEquals() {

        Coordinate cc1 = new CartesianCoordinate(0, 0, 0);
        Coordinate cc2 = new CartesianCoordinate(0,0, Coordinate.EPSILON);
        Coordinate cc3 = new CartesianCoordinate(0,0,1e-15);

        // for debugging, ignore...
        // boolean cc1IsEqualToCc3 = cc1.equals(cc3);

        assertNotEquals(cc1, cc2);
        assertEquals(cc1, cc3);

    }
}
