package org.wahlzeit.utils;

public class AssertUtil {

    protected AssertUtil() {}

    static public void assertEquals(Object a, Object b) {
        assert a.equals(b);
    }

    static public void assertEquals(int a, int b) {
        assert a == b;
    }

    static public void assertNotNull(Object o) {
        assert o != null;
    }

    static public void assertIsFinite(Double value) {
        assert Double.isFinite(value);
    }

    static public void assertNonNegative(Double value) {
        assert Double.compare(value, 0) >= 0;
    }
}
