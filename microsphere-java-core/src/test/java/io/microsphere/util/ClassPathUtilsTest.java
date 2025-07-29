/**
 *
 */
package io.microsphere.util;

import io.microsphere.AbstractTestCase;
import io.microsphere.lang.ClassDataRepository;
import org.junit.jupiter.api.Test;

import java.lang.management.RuntimeMXBean;
import java.net.URL;
import java.util.Set;

import static io.microsphere.util.ClassLoaderUtils.isLoadedClass;
import static io.microsphere.util.ClassPathUtils.getBootstrapClassPaths;
import static io.microsphere.util.ClassPathUtils.getClassPaths;
import static io.microsphere.util.ClassPathUtils.getRuntimeClassLocation;
import static java.lang.management.ManagementFactory.getRuntimeMXBean;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link ClassPathUtils} {@link Test}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see ClassPathUtilsTest
 * @since 1.0.0
 */
class ClassPathUtilsTest extends AbstractTestCase {

    @Test
    void testGetBootstrapClassPaths() {
        Set<String> bootstrapClassPaths = getBootstrapClassPaths();
        assertNotNull(bootstrapClassPaths);
        RuntimeMXBean runtimeMXBean = getRuntimeMXBean();
        assertEquals(runtimeMXBean.isBootClassPathSupported(), !bootstrapClassPaths.isEmpty());
        log(bootstrapClassPaths);
    }

    @Test
    void testGetClassPaths() {
        Set<String> classPaths = getClassPaths();
        assertNotNull(classPaths);
        assertFalse(classPaths.isEmpty());
        log(classPaths);
    }

    @Test
    void testGetRuntimeClassLocation() {
        URL location = getRuntimeClassLocation(String.class);
        assertNotNull(location);
        log(location);

        location = getRuntimeClassLocation(getClass());
        assertNotNull(location);
        log(location);

        //Primitive type
        location = getRuntimeClassLocation(int.class);
        assertNull(location);

        //Array type
        location = getRuntimeClassLocation(int[].class);
        assertNull(location);


        Set<String> classNames = ClassDataRepository.INSTANCE.getAllClassNamesInClassPaths();
        for (String className : classNames) {
            if (!isLoadedClass(TEST_CLASS_LOADER, className)) {
                location = getRuntimeClassLocation(className);
                assertNull(location);
            }
        }

    }

    @Test
    void testGetRuntimeClassLocationWithClassName() {
        URL location = getRuntimeClassLocation(String.class.getName());
        assertNotNull(location);
        log(location);
    }
}
