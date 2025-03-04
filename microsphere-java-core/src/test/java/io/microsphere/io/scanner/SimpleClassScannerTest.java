/**
 *
 */
package io.microsphere.io.scanner;

import io.microsphere.AbstractTestCase;
import io.microsphere.util.ClassLoaderUtils;
import org.junit.jupiter.api.Test;

import javax.annotation.Nonnull;
import java.net.URL;
import java.util.Set;

import static io.microsphere.util.ClassLoaderUtils.getClassResource;
import static io.microsphere.util.ClassLoaderUtils.getDefaultClassLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link SimpleClassScannerTest}
 *
 * @author <a href="mercyblitz@gmail.com">Mercy<a/>
 * @version 1.0.0
 * @see SimpleClassScannerTest
 * @since 1.0.0
 */
public class SimpleClassScannerTest extends AbstractTestCase {

    private SimpleClassScanner simpleClassScanner = SimpleClassScanner.INSTANCE;

    @Test
    public void testScanInPackage() {
        Set<Class<?>> classesSet = simpleClassScanner.scan(classLoader, "io.microsphere.io.scanner");
        assertFalse(classesSet.isEmpty());

        classesSet = simpleClassScanner.scan(classLoader, "javax.annotation.concurrent", false, true);
        assertEquals(4, classesSet.size());

        classesSet = simpleClassScanner.scan(classLoader, "i", false, true);
        assertTrue(classesSet.isEmpty());
    }

    @Test
    public void testScanInArchive() {
        URL nonnullClassResource = getClassResource(classLoader, Nonnull.class);
        Set<Class<?>> classesSet = simpleClassScanner.scan(classLoader, nonnullClassResource, false);
        assertFalse(classesSet.isEmpty());
    }

}
