package io.microsphere.reflect.generics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import static io.microsphere.reflect.generics.ParameterizedTypeImpl.of;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link ParameterizedTypeImpl} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see ParameterizedType
 * @see ParameterizedTypeImpl
 * @since 1.0.0
 */
public class ParameterizedTypeImplTest {

    private static Type[] actualTypeArguments = new Type[]{String.class, Integer.class};

    /**
     * The top-level type : Map&lt;String,Integer&gt;
     */
    private ParameterizedTypeImpl topLevelType;

    /**
     * The nested type : Map.Entry&lt;String,Integer&gt;
     */
    private ParameterizedTypeImpl nestedType;

    /**
     * The complex type
     */
    private ParameterizedTypeImpl complexType;

    @BeforeEach
    public void init() {
        topLevelType = of(Map.class, actualTypeArguments);
        nestedType = of(Map.Entry.class, actualTypeArguments, Map.class);
        complexType = of(Map.Entry.class, actualTypeArguments, topLevelType);
    }

    @Test
    public void testOfOnMalformedParameterizedTypeException() {
        assertThrows(MalformedParameterizedTypeException.class, () -> of(Map.class, new Type[]{String.class}));
    }

    @Test
    public void testGetActualTypeArguments() {
        assertArrayEquals(actualTypeArguments, topLevelType.getActualTypeArguments());
        assertArrayEquals(actualTypeArguments, nestedType.getActualTypeArguments());
        assertArrayEquals(actualTypeArguments, complexType.getActualTypeArguments());
    }

    @Test
    public void testGetRawType() {
        assertEquals(Map.class, topLevelType.getRawType());
        assertEquals(Map.Entry.class, nestedType.getRawType());
        assertEquals(Map.Entry.class, complexType.getRawType());
    }

    @Test
    public void testGetOwnerType() {
        assertNull(topLevelType.getOwnerType());
        assertEquals(Map.class, nestedType.getOwnerType());
        assertEquals(topLevelType, complexType.getOwnerType());
    }

    @Test
    public void testEqualsOnSame() {
        assertEquals(topLevelType, topLevelType);
        assertEquals(nestedType, nestedType);
        assertEquals(complexType, complexType);
    }

    @Test
    public void testEqualsOnSameType() {
        assertEquals(topLevelType, of(Map.class, actualTypeArguments, null));
        assertEquals(nestedType, of(Map.Entry.class, actualTypeArguments, Map.class));
        assertEquals(complexType, of(Map.Entry.class, actualTypeArguments, topLevelType));
    }

    @Test
    public void testEqualsOnDifferentType() {
        assertNotEquals(topLevelType, of(Map.class, actualTypeArguments, String.class));
        assertNotEquals(topLevelType, nestedType);
        assertNotEquals(topLevelType, complexType);
        assertNotEquals(topLevelType, Object.class);
    }

    @Test
    public void testHashCodeOnSame() {
        assertEquals(topLevelType.hashCode(), topLevelType.hashCode());
        assertEquals(nestedType.hashCode(), nestedType.hashCode());
        assertEquals(complexType.hashCode(), complexType.hashCode());
    }

    @Test
    public void testHashCodeOnSameType() {
        assertEquals(topLevelType.hashCode(), of(Map.class, actualTypeArguments, null).hashCode());
        assertEquals(nestedType.hashCode(), of(Map.Entry.class, actualTypeArguments, Map.class).hashCode());
        assertEquals(complexType.hashCode(), of(Map.Entry.class, actualTypeArguments, topLevelType).hashCode());
    }

    @Test
    public void testHashCodeOnDifferentType() {
        assertNotEquals(topLevelType.hashCode(), of(Map.class, actualTypeArguments, String.class).hashCode());
        assertNotEquals(topLevelType.hashCode(), nestedType.hashCode());
        assertNotEquals(topLevelType.hashCode(), complexType.hashCode());
    }

    @Test
    public void testToStringOnSame() {
        assertEquals(topLevelType.toString(), topLevelType.toString());
        assertEquals(nestedType.toString(), nestedType.toString());
        assertEquals(complexType.toString(), complexType.toString());
    }

    @Test
    public void testToStringOnSameType() {
        assertEquals(topLevelType.toString(), of(Map.class, actualTypeArguments, null).toString());
        assertEquals(nestedType.toString(), of(Map.Entry.class, actualTypeArguments, Map.class).toString());
        assertEquals(complexType.toString(), of(Map.Entry.class, actualTypeArguments, topLevelType).toString());
    }

    @Test
    public void testToStringOnDifferentType() {
        assertNotEquals(topLevelType.toString(), of(Map.class, actualTypeArguments, String.class).toString());
        assertNotEquals(topLevelType.toString(), nestedType.toString());
        assertNotEquals(topLevelType.toString(), complexType.toString());
    }
}