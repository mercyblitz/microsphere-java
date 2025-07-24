package io.microsphere.process;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ClassicProcessIdResolver} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see ClassicProcessIdResolver
 * @since 1.0.0
 */
class ClassicProcessIdResolverTest {

    private ClassicProcessIdResolver resolver;

    @BeforeEach
    public void setUp() {
        resolver = new ClassicProcessIdResolver();
    }

    @Test
    public void testSupports() {
        assertTrue(resolver.supports());
    }

    @Test
    public void testCurrent() {
        assertNotNull(resolver.current());
    }

    @Test
    public void testGetPriority() {
        assertEquals(9, resolver.getPriority());
    }
}