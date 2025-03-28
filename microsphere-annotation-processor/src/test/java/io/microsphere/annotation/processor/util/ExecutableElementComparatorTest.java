package io.microsphere.annotation.processor.util;

import io.microsphere.annotation.processor.AbstractAnnotationProcessingTest;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.lang.reflect.Type;

import static io.microsphere.annotation.processor.util.ExecutableElementComparator.INSTANCE;
import static io.microsphere.annotation.processor.util.MethodUtils.findMethod;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link ExecutableElementComparator} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see ExecutableElementComparator
 * @since 1.0.0
 */
public class ExecutableElementComparatorTest extends AbstractAnnotationProcessingTest {

    private final ExecutableElementComparator comparator = INSTANCE;

    @Test
    public void testCompareOnSameMethods() {
        String methodName = "toString";
        ExecutableElement method = getMethod(methodName);
        assertEquals(0, comparator.compare(method, method));
    }

    @Test
    public void testCompareOnDifferentMethods() {
        assertEquals("toString".compareTo("hashCode"), comparator.compare(getMethod("toString"), getMethod("hashCode")));
    }

    @Test
    public void testCompareOnOverloadMethods() {
        // Integer#valueOf(int) | Integer#valueOf(String)
        TypeElement typeElement = getTypeElement(Integer.class);
        String methodName = "valueOf";
        assertEquals(int.class.getName().compareTo(String.class.getName()), comparator.compare(findMethod(typeElement, methodName, int.class), findMethod(typeElement, methodName, String.class)));
    }

    @Test
    public void testCompare() {
        // Object#equals
        assertEquals(0, comparator.compare(getMethod("equals", Object.class), getMethod("equals", Object.class)));
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    private ExecutableElement getMethod(String methodName, Type... parameterTypes) {
        return findMethod(testTypeElement, methodName, parameterTypes);
    }
}