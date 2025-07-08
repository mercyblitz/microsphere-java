package io.microsphere.filter;

import org.junit.jupiter.api.Test;

import static io.microsphere.filter.FilterOperator.AND;
import static io.microsphere.filter.FilterOperator.OR;
import static io.microsphere.filter.FilterOperator.XOR;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link FilterOperator} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see FilterOperator
 * @since 1.0.0
 */
class FilterOperatorTest {

    TrueClassFilter filter1 = TrueClassFilter.INSTANCE;


    PackageNameClassFilter filter2 = new PackageNameClassFilter("io.microsphere", true);

    @Test
    public void testAND() {
        Filter filter = AND.createFilter(filter1, filter2);
        assertFalse(filter.accept(null));

        filter = AND.createFilter(filter1, filter1);
        assertTrue(filter.accept(null));
    }

    @Test
    public void testANDOnNullFilters() {
        Filter filter = AND.createFilter(null);
        assertTrue(filter.accept(null));
    }

    @Test
    public void testANDOnEmptyFilters() {
        Filter filter = AND.createFilter(new Filter[0]);
        assertTrue(filter.accept(null));
    }

    @Test
    public void testOR() {
        Filter filter = OR.createFilter(filter1, filter2);
        assertTrue(filter.accept(null));

        filter = OR.createFilter(filter2, filter2);
        assertFalse(filter.accept(null));
    }

    @Test
    public void testOROnNullFilters() {
        Filter filter = OR.createFilter(null);
        assertTrue(filter.accept(null));
    }

    @Test
    public void testOROnEmptyFilters() {
        Filter filter = OR.createFilter(new Filter[0]);
        assertTrue(filter.accept(null));
    }

    @Test
    public void testXOR() {
        Filter filter = XOR.createFilter(filter1, filter2);
        assertFalse(filter.accept(null));

        filter = XOR.createFilter(filter2, filter2);
        assertTrue(filter.accept(null));
    }

    @Test
    public void testXOROnNullFilters() {
        Filter filter = XOR.createFilter(null);
        assertTrue(filter.accept(null));
    }

    @Test
    public void testXOROnEmptyFilters() {
        Filter filter = XOR.createFilter(new Filter[0]);
        assertTrue(filter.accept(null));
    }

}