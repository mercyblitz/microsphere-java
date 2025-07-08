//package io.microsphere.misc;
//
//import sun.misc.Unsafe;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
///**
// * {@link UnsafeUtils} Test
// *
// * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
// * @see UnsafeUtilTest
// * @since 1.0.0
// */
//class UnsafeUtilTest {
//
//    private Model model;
//
//    public void setUp() throws Exception {
//        model = new Model();
//    }
//
//    public void testStaticInit() throws Exception {
//        assertNotNull(UnsafeUtils.unsafe);
//        Unsafe unsafe = UnsafeUtils.unsafe;
//
//        assertEquals(UnsafeUtils.LONG_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(long[].class));
//        assertEquals(UnsafeUtils.INT_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(int[].class));
//        assertEquals(UnsafeUtils.SHORT_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(short[].class));
//        assertEquals(UnsafeUtils.BYTE_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(byte[].class));
//        assertEquals(UnsafeUtils.BOOLEAN_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(boolean[].class));
//        assertEquals(UnsafeUtils.DOUBLE_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(double[].class));
//        assertEquals(UnsafeUtils.FLOAT_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(float[].class));
//        assertEquals(UnsafeUtils.CHAR_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(char[].class));
//        assertEquals(UnsafeUtils.OBJECT_ARRAY_BASE_OFFSET, unsafe.arrayBaseOffset(Object[].class));
//
//
//        assertEquals(UnsafeUtils.LONG_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(long[].class));
//        assertEquals(UnsafeUtils.INT_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(int[].class));
//        assertEquals(UnsafeUtils.SHORT_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(short[].class));
//        assertEquals(UnsafeUtils.BYTE_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(byte[].class));
//        assertEquals(UnsafeUtils.BOOLEAN_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(boolean[].class));
//        assertEquals(UnsafeUtils.DOUBLE_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(double[].class));
//        assertEquals(UnsafeUtils.FLOAT_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(float[].class));
//        assertEquals(UnsafeUtils.CHAR_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(char[].class));
//        assertEquals(UnsafeUtils.OBJECT_ARRAY_INDEX_SCALE, unsafe.arrayIndexScale(Object[].class));
//
//    }
//
//    public void testPutLongAndGetLong() throws Exception {
//        String fieldName = "longValue";
//        long value = Long.MAX_VALUE;
//        UnsafeUtils.putLong(model, fieldName, value);
//        long returnValue = UnsafeUtils.getLong(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.longValue, returnValue);
//
//        value = Long.MIN_VALUE;
//        UnsafeUtils.putLongVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getLongVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.longValue, returnValue);
//
//        value = Long.MAX_VALUE;
//        UnsafeUtils.putOrderedLong(model, fieldName, value);
//        returnValue = UnsafeUtils.getLongVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.longValue, returnValue);
//    }
//
//    public void testPutIntAndGetInt() throws Exception {
//        String fieldName = "intValue";
//        int value = 123;
//        UnsafeUtils.putInt(model, fieldName, value);
//        int returnValue = UnsafeUtils.getInt(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.intValue, returnValue);
//
//        value = Integer.MAX_VALUE;
//        UnsafeUtils.putIntVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getIntVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.intValue, returnValue);
//
//        value = Integer.MIN_VALUE;
//        UnsafeUtils.putOrderedInt(model, fieldName, value);
//        returnValue = UnsafeUtils.getIntVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.intValue, returnValue);
//    }
//
//    public void testPutShortAndGetShort() throws Exception {
//        String fieldName = "shortValue";
//        short value = Short.MAX_VALUE;
//        UnsafeUtils.putShort(model, fieldName, value);
//        short returnValue = UnsafeUtils.getShort(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.shortValue, returnValue);
//
//        UnsafeUtils.putShortVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getShortVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.shortValue, returnValue);
//    }
//
//    public void testPutByteAndGetByte() throws Exception {
//        String fieldName = "byteValue";
//        byte value = Byte.MAX_VALUE;
//        UnsafeUtils.putByte(model, fieldName, value);
//        byte returnValue = UnsafeUtils.getByte(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.byteValue, returnValue);
//
//        UnsafeUtils.putByteVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getByteVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.byteValue, returnValue);
//    }
//
//    public void testPutBooleanAndGetBoolean() throws Exception {
//        String fieldName = "booleanValue";
//        boolean value = Boolean.TRUE;
//        UnsafeUtils.putBoolean(model, fieldName, value);
//        boolean returnValue = UnsafeUtils.getBoolean(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.booleanValue, returnValue);
//
//        UnsafeUtils.putBooleanVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getBooleanVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.booleanValue, returnValue);
//    }
//
//    public void testPutDoubleAndGetDouble() throws Exception {
//        String fieldName = "doubleValue";
//        double value = Double.MAX_VALUE;
//        UnsafeUtils.putDouble(model, fieldName, value);
//        double returnValue = UnsafeUtils.getDouble(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.doubleValue, returnValue);
//
//        UnsafeUtils.putDoubleVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getDoubleVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.doubleValue, returnValue);
//    }
//
//    public void testPutFloatAndGetFloat() throws Exception {
//        String fieldName = "floatValue";
//        float value = Float.MAX_VALUE;
//        UnsafeUtils.putFloat(model, fieldName, value);
//        float returnValue = UnsafeUtils.getFloat(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.floatValue, returnValue);
//
//        UnsafeUtils.putFloatVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getFloatVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.floatValue, returnValue);
//    }
//
//    public void testPutCharAndGetChar() throws Exception {
//        String fieldName = "charValue";
//        char value = '@';
//        UnsafeUtils.putChar(model, fieldName, value);
//        char returnValue = UnsafeUtils.getChar(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.charValue, returnValue);
//
//        UnsafeUtils.putCharVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getCharVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.charValue, returnValue);
//
//    }
//
//    public void testPutObjectAndGetObject() throws Exception {
//        String fieldName = "stringValue";
//        Object value = "Test text";
//        UnsafeUtils.putObject(model, fieldName, value);
//        Object returnValue = UnsafeUtils.getObject(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.stringValue, returnValue);
//
//        value = "AAAAAAAAAAAA";
//        UnsafeUtils.putObjectVolatile(model, fieldName, value);
//        returnValue = UnsafeUtils.getObjectVolatile(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.stringValue, returnValue);
//
//        value = "BBBBBB";
//        UnsafeUtils.putOrderedObject(model, fieldName, value);
//        returnValue = UnsafeUtils.getObject(model, fieldName);
//        assertEquals(returnValue, value);
//        assertEquals(model.stringValue, returnValue);
//    }
//
//    public void testPutLongIntoArrayVolatileAndGetLongFromArrayVolatile() throws Exception {
//        String fieldName = "longArrayValue";
//        long value = 123;
//        int index = 2;
//        UnsafeUtils.putLongIntoArrayVolatile(model, fieldName, index, value);
//        long returnValue = UnsafeUtils.getLongFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.longArrayValue[index], returnValue);
//
//        value = Integer.MAX_VALUE;
//        UnsafeUtils.putOrderedLongIntoArray(model, fieldName, index, value);
//        returnValue = UnsafeUtils.getLongFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.longArrayValue[index], returnValue);
//    }
//
//    public void testPutIntIntoArrayVolatileAndGetIntFromArrayVolatile() throws Exception {
//        String fieldName = "intArrayValue";
//        int value = 123;
//        int index = 1;
//        UnsafeUtils.putIntIntoArrayVolatile(model, fieldName, index, value);
//        int returnValue = UnsafeUtils.getIntFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.intArrayValue[index], returnValue);
//
//        value = Integer.MAX_VALUE;
//        UnsafeUtils.putOrderedIntIntoArray(model, fieldName, index, value);
//        returnValue = UnsafeUtils.getIntFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.intArrayValue[index], returnValue);
//    }
//
//    public void testPutShortIntoArrayVolatileAndGetShortFromArrayVolatile() throws Exception {
//        String fieldName = "shortArrayValue";
//        short value = 123;
//        int index = 5;
//        UnsafeUtils.putShortIntoArrayVolatile(model, fieldName, index, value);
//        short returnValue = UnsafeUtils.getShortFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.shortArrayValue[index], returnValue);
//    }
//
//    public void testPutByteIntoArrayVolatileAndGetByteFromArrayVolatile() throws Exception {
//        String fieldName = "byteArrayValue";
//        byte value = 123;
//        int index = 5;
//        UnsafeUtils.putByteIntoArrayVolatile(model, fieldName, index, value);
//        byte returnValue = UnsafeUtils.getByteFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.byteArrayValue[index], returnValue);
//    }
//
//    public void testPutBooleanIntoArrayVolatileAndGetBooleanFromArrayVolatile() throws Exception {
//        String fieldName = "booleanArrayValue";
//        boolean value = Boolean.TRUE;
//        int index = 3;
//        UnsafeUtils.putBooleanIntoArrayVolatile(model, fieldName, index, value);
//        boolean returnValue = UnsafeUtils.getBooleanFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.booleanArrayValue[index], returnValue);
//    }
//
//    public void testPutDoubleIntoArrayVolatileAndGetDoubleFromArrayVolatile() throws Exception {
//        String fieldName = "doubleArrayValue";
//        double value = Double.MAX_VALUE;
//        int index = 8;
//        UnsafeUtils.putDoubleIntoArrayVolatile(model, fieldName, index, value);
//        double returnValue = UnsafeUtils.getDoubleFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.doubleArrayValue[index], returnValue);
//    }
//
//    public void testPutFloatIntoArrayVolatileAndGetFloatFromArrayVolatile() throws Exception {
//        String fieldName = "floatArrayValue";
//        float value = Float.MAX_VALUE;
//        int index = 7;
//        UnsafeUtils.putFloatIntoArrayVolatile(model, fieldName, index, value);
//        float returnValue = UnsafeUtils.getFloatFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.floatArrayValue[index], returnValue);
//    }
//
//    public void testPutCharIntoArrayVolatileAndGetCharFromArrayVolatile() throws Exception {
//        String fieldName = "charArrayValue";
//        char value = '@';
//        int index = 9;
//        UnsafeUtils.putCharIntoArrayVolatile(model, fieldName, index, value);
//        char returnValue = UnsafeUtils.getCharFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.charArrayValue[index], returnValue);
//    }
//
//    public void testPutObjectIntoArrayVolatileAndGetObjectFromArrayVolatile() throws Exception {
//        String fieldName = "objectArrayValue";
//        Object value = "Test";
//        int index = 5;
//        UnsafeUtils.putObjectIntoArrayVolatile(model, fieldName, index, value);
//        Object returnValue = UnsafeUtils.getObjectFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.objectArrayValue[index], returnValue);
//
//        UnsafeUtils.putOrderedObjectIntoArray(model, fieldName, index, value);
//        returnValue = UnsafeUtils.getObjectFromArrayVolatile(model, fieldName, index);
//        assertEquals(returnValue, value);
//        assertEquals(model.objectArrayValue[index], returnValue);
//
//    }
//
//    /**
//     * 测试当存储更高范围类型的值，然后获取
//     */
//    public void tesPutOnInvalidTypeValue() throws Exception {
//        String fieldName = "intValue";
//        int value = Integer.MAX_VALUE;
//        int index = 1;
//        IllegalArgumentException exception;
//
//        exception = null;
//        assertNull(exception);
//        try {
//            UnsafeUtils.putLong(model, fieldName, value);
//        } catch (IllegalArgumentException e) {
//            exception = e;
//        }
//        assertNotNull(exception);
//
//        exception = null;
//        assertNull(exception);
//        try {
//            UnsafeUtils.putShort(model, fieldName, (short) 1);
//        } catch (IllegalArgumentException e) {
//            exception = e;
//        }
//        assertNotNull(exception);
//
//        exception = null;
//        assertNull(exception);
//        try {
//            UnsafeUtils.putChar(model, fieldName, (char) 1);
//        } catch (IllegalArgumentException e) {
//            exception = e;
//        }
//        assertNotNull(exception);
//
//
//    }
//
//
//    public void testOnObjectIsNull() throws Exception {
//        String fieldName = "aaaa";
//        Object value = "value";
//
//        NullPointerException exception = null;
//        try {
//            UnsafeUtils.putObject(null, fieldName, value);
//        } catch (NullPointerException e) {
//            exception = e;
//        }
//        assertNotNull(exception);
//    }
//
//    public void testOnIllegalArgumentException() throws Exception {
//        String fieldName = "aaaa";
//        Object value = "value";
//
//        NullPointerException exception = null;
//        try {
//            UnsafeUtils.putObject(model, fieldName, value);
//        } catch (NullPointerException e) {
//            exception = e;
//        }
//        assertNotNull(exception);
//    }
//
//    private static class Model {
//        private long longValue;
//        private int intValue;
//        private short shortValue;
//        private byte byteValue;
//        private boolean booleanValue;
//        private float floatValue;
//        private double doubleValue;
//        private char charValue;
//        private String stringValue;
//        private long[] longArrayValue = new long[10];
//        private int[] intArrayValue = new int[10];
//        private short[] shortArrayValue = new short[10];
//        private byte[] byteArrayValue = new byte[10];
//        private boolean[] booleanArrayValue = new boolean[10];
//        private double[] doubleArrayValue = new double[10];
//        private float[] floatArrayValue = new float[10];
//        private char[] charArrayValue = new char[10];
//        private Object[] objectArrayValue = new Object[10];
//    }
//
//}
