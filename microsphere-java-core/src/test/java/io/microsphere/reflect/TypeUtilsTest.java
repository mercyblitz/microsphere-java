/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.microsphere.reflect;

import io.microsphere.AbstractTestCase;
import io.microsphere.convert.Converter;
import io.microsphere.convert.StringToIntegerConverter;
import io.microsphere.convert.StringToStringConverter;
import io.microsphere.test.A;
import io.microsphere.test.B;
import io.microsphere.test.C;
import io.microsphere.test.D;
import io.microsphere.test.E;
import io.microsphere.test.StringBooleanToInteger;
import io.microsphere.test.StringIntegerBooleanHashMap;
import io.microsphere.test.StringIntegerToBoolean;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static io.microsphere.collection.Lists.ofList;
import static io.microsphere.reflect.MethodUtils.findMethod;
import static io.microsphere.reflect.TypeUtils.GENERIC_ARRAY_TYPE_FILTER;
import static io.microsphere.reflect.TypeUtils.NON_OBJECT_CLASS_FILTER;
import static io.microsphere.reflect.TypeUtils.NON_OBJECT_TYPE_FILTER;
import static io.microsphere.reflect.TypeUtils.PARAMETERIZED_TYPE_FILTER;
import static io.microsphere.reflect.TypeUtils.TYPE_VARIABLE_FILTER;
import static io.microsphere.reflect.TypeUtils.WILDCARD_TYPE_FILTER;
import static io.microsphere.reflect.TypeUtils.asClass;
import static io.microsphere.reflect.TypeUtils.asGenericArrayType;
import static io.microsphere.reflect.TypeUtils.asParameterizedType;
import static io.microsphere.reflect.TypeUtils.asTypeVariable;
import static io.microsphere.reflect.TypeUtils.asWildcardType;
import static io.microsphere.reflect.TypeUtils.doResolveActualTypeArguments;
import static io.microsphere.reflect.TypeUtils.doResolveActualTypeArgumentsInFastPath;
import static io.microsphere.reflect.TypeUtils.findAllGenericInterfaces;
import static io.microsphere.reflect.TypeUtils.findAllGenericSuperclasses;
import static io.microsphere.reflect.TypeUtils.findAllParameterizedTypes;
import static io.microsphere.reflect.TypeUtils.findAllTypes;
import static io.microsphere.reflect.TypeUtils.findHierarchicalTypes;
import static io.microsphere.reflect.TypeUtils.findParameterizedTypes;
import static io.microsphere.reflect.TypeUtils.getAllGenericInterfaces;
import static io.microsphere.reflect.TypeUtils.getAllGenericSuperclasses;
import static io.microsphere.reflect.TypeUtils.getAllParameterizedTypes;
import static io.microsphere.reflect.TypeUtils.getAllTypes;
import static io.microsphere.reflect.TypeUtils.getClassName;
import static io.microsphere.reflect.TypeUtils.getClassNames;
import static io.microsphere.reflect.TypeUtils.getComponentType;
import static io.microsphere.reflect.TypeUtils.getHierarchicalTypes;
import static io.microsphere.reflect.TypeUtils.getParameterizedTypes;
import static io.microsphere.reflect.TypeUtils.getRawClass;
import static io.microsphere.reflect.TypeUtils.getRawType;
import static io.microsphere.reflect.TypeUtils.getTypeName;
import static io.microsphere.reflect.TypeUtils.getTypeNames;
import static io.microsphere.reflect.TypeUtils.isActualType;
import static io.microsphere.reflect.TypeUtils.isAssignableFrom;
import static io.microsphere.reflect.TypeUtils.isClass;
import static io.microsphere.reflect.TypeUtils.isGenericArrayType;
import static io.microsphere.reflect.TypeUtils.isObjectClass;
import static io.microsphere.reflect.TypeUtils.isObjectType;
import static io.microsphere.reflect.TypeUtils.isParameterizedType;
import static io.microsphere.reflect.TypeUtils.isTypeVariable;
import static io.microsphere.reflect.TypeUtils.isWildcardType;
import static io.microsphere.reflect.TypeUtils.resolveActualTypeArgument;
import static io.microsphere.reflect.TypeUtils.resolveActualTypeArgumentClass;
import static io.microsphere.reflect.TypeUtils.resolveActualTypeArgumentClasses;
import static io.microsphere.reflect.TypeUtils.resolveActualTypeArguments;
import static io.microsphere.reflect.TypeUtils.resolveTypeArgumentClasses;
import static io.microsphere.reflect.TypeUtils.resolveTypeArguments;
import static io.microsphere.reflect.generics.ParameterizedTypeImpl.of;
import static io.microsphere.util.ArrayUtils.EMPTY_STRING_ARRAY;
import static io.microsphere.util.ArrayUtils.EMPTY_TYPE_ARRAY;
import static io.microsphere.util.ArrayUtils.ofArray;
import static io.microsphere.util.ClassUtils.PRIMITIVE_TYPES;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link TypeUtils} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
class TypeUtilsTest extends AbstractTestCase {

    @Test
    void testConstantFilters() {
        assertFalse(NON_OBJECT_TYPE_FILTER.test(null));
        assertFalse(NON_OBJECT_CLASS_FILTER.test(null));

        assertFalse(NON_OBJECT_TYPE_FILTER.test(Object.class));
        assertFalse(NON_OBJECT_CLASS_FILTER.test(Object.class));

        assertTrue(NON_OBJECT_TYPE_FILTER.test(String.class));
        assertTrue(NON_OBJECT_CLASS_FILTER.test(String.class));

        assertFalse(PARAMETERIZED_TYPE_FILTER.test(null));
        assertFalse(PARAMETERIZED_TYPE_FILTER.test(Object.class));
        assertFalse(PARAMETERIZED_TYPE_FILTER.test(D.class));

        assertTrue(PARAMETERIZED_TYPE_FILTER.test(D.class.getGenericSuperclass()));

        assertFalse(TYPE_VARIABLE_FILTER.test(null));
        assertFalse(TYPE_VARIABLE_FILTER.test(null));
        assertFalse(TYPE_VARIABLE_FILTER.test(Object.class));
        assertFalse(TYPE_VARIABLE_FILTER.test(D.class));
        assertFalse(TYPE_VARIABLE_FILTER.test(D.class.getGenericSuperclass()));

        assertTrue(TYPE_VARIABLE_FILTER.test(Comparable.class.getTypeParameters()[0]));
    }

    @Test
    void testIsClass() {
        assertTrue(isClass(String.class));
    }

    @Test
    void testIsClassOnNull() {
        assertFalse(isClass(null));
    }

    @Test
    void testIsClassOnObject() {
        assertFalse(isClass(1));
        assertFalse(isClass("test"));
    }

    @Test
    void testIsObjectClass() {
        assertTrue(isObjectClass(Object.class));
    }

    @Test
    void testIsObjectClassOnNull() {
        assertFalse(isObjectClass(null));
    }

    @Test
    void testIsObjectClassOnNotObjectClass() {
        assertFalse(isObjectClass(Integer.class));
        assertFalse(isObjectClass(String.class));
    }


    @Test
    void testIsObjectType() {
        assertTrue(isObjectType(Object.class));
    }

    @Test
    void testIsObjectTypeOnNull() {
        assertFalse(isObjectType(null));
    }

    @Test
    void testIsObjectTypeOnNotObjectClass() {
        assertFalse(isObjectType(Integer.class));
        assertFalse(isObjectType(String.class));
    }

    @Test
    void testIsParameterizedType() {
        assertTrue(isParameterizedType(D.class.getGenericSuperclass()));
        assertTrue(isParameterizedType(of(C.class, String.class)));
    }

    @Test
    void testIsParameterizedTypeOnNull() {
        assertFalse(isParameterizedType(null));
    }

    @Test
    void testIsParameterizedTypeOnNotParameterizedType() {
        assertFalse(isParameterizedType(Object.class));
        assertFalse(isParameterizedType(String.class));
    }

    @Test
    void testIsTypeVariable() {
        assertTrue(isTypeVariable(getTypeVariable()));
    }

    @Test
    void testIsTypeVariableOnNull() {
        assertFalse(isTypeVariable(null));
    }

    @Test
    void testIsTypeVariableOnNotTypeVariable() {
        assertFalse(isTypeVariable(Object.class));
        assertFalse(isTypeVariable(String.class));
    }

    @Test
    void testIsWildcardType() {
        assertTrue(isWildcardType(getWildcardType()));
    }

    @Test
    void testIsWildcardTypeOnNull() {
        assertFalse(isWildcardType(null));
    }

    @Test
    void testIsWildcardTypeOnNotWildcardType() {
        assertFalse(isWildcardType(Object.class));
        assertFalse(isWildcardType(String.class));
    }

    @Test
    void testIsGenericArrayType() {
        Type genericArrayType = getGenericArrayType();
        assertTrue(isGenericArrayType(genericArrayType));
    }

    @Test
    void testIsGenericArrayTypeOnNull() {
        assertFalse(isGenericArrayType(null));
    }

    @Test
    void testIsGenericArrayTypeOnNotGenericArrayType() {
        assertFalse(isGenericArrayType(Object.class));
        assertFalse(isGenericArrayType(String.class));
    }

    @Test
    void testIsActualTypeOnClass() {
        assertTrue(isActualType(int.class));
        assertTrue(isActualType(String.class));
        assertTrue(isActualType(String[].class));
        assertTrue(isActualType(String[][].class));
        assertTrue(isActualType(String[][][].class));
        assertTrue(isActualType(String[][][][].class));
        assertTrue(isActualType(String[][][][][].class));
    }

    @Test
    void testIsActualTypeOnParameterizedType() {
        assertTrue(isActualType(of(C.class, String.class)));
        assertTrue(isActualType(D.class.getGenericSuperclass()));
    }

    @Test
    void testIsActualTypeOnNull() {
        assertFalse(isActualType(null));
    }

    @Test
    void testIsActualTypeOnNotActualType() {
        assertFalse(isActualType(Comparable.class.getTypeParameters()[0]));
    }

    @Test
    void testGetRawTypeOnParameterizedType() {
        assertEquals(C.class, getRawType(D.class.getGenericSuperclass()));
    }

    @Test
    void testGetRawTypeOnNotParameterizedType() {
        assertEquals(int.class, getRawType(int.class));
        assertEquals(Object.class, getRawType(Object.class));
    }

    @Test
    void testGetRawTypeOnNull() {
        assertNull(getRawType(null));
    }

    @Test
    void testGetRawClass() {
        assertEquals(Object.class, getRawClass(Object.class));
        assertEquals(C.class, getRawClass(D.class.getGenericSuperclass()));
    }

    @Test
    void testGetRawClassOnNull() {
        assertNull(getRawClass(null));
    }

    @Test
    void testIsAssignableFromOnType() {
        assertTrue(isAssignableFrom(D.class.getGenericSuperclass(), D.class.getGenericSuperclass()));
    }

    @Test
    void testIsAssignableFromOnClassAndParameterizedType() {
        assertTrue(isAssignableFrom(C.class, D.class.getGenericSuperclass()));
        assertTrue(isAssignableFrom(B.class, D.class.getGenericSuperclass()));
        assertTrue(isAssignableFrom(Comparable.class, B.class.getGenericInterfaces()[0]));
    }

    @Test
    void testIsAssignableFromOnClass() {
        assertTrue(isAssignableFrom(Object.class, Object.class));
        assertTrue(isAssignableFrom(Object.class, String.class));
    }

    @Test
    void testResolveActualTypeArguments() {
        List<Type> actualTypeArguments = resolveActualTypeArguments(B.class, Comparable.class);
        assertValues(actualTypeArguments, B.class);

        actualTypeArguments = resolveActualTypeArguments(C.class, Comparable.class);
        assertValues(actualTypeArguments, B.class);

        actualTypeArguments = resolveActualTypeArguments(D.class, C.class);
        assertValues(actualTypeArguments, String.class);

        actualTypeArguments = resolveActualTypeArguments(E.class, Comparable.class);
        assertValues(actualTypeArguments, B.class);

        actualTypeArguments = resolveActualTypeArguments(StringToStringConverter.class, Converter.class);
        assertValues(actualTypeArguments, String.class, String.class);

        actualTypeArguments = resolveActualTypeArguments(StringIntegerBooleanHashMap.class, Map.class);
        assertValues(actualTypeArguments, String.class, Integer.class);
    }

    @Test
    void testResolveActualTypeArgument() {
        assertSame(String.class, resolveActualTypeArgument(D.class, C.class, 0));
        assertSame(B.class, resolveActualTypeArgument(D.class, Comparable.class, 0));
    }

    @Test
    void testResolveActualTypeArgumentClasses() {
        List<Class> actualTypeArgumentClasses = resolveActualTypeArgumentClasses(D.class, C.class);
        assertEquals(1, actualTypeArgumentClasses.size());
        assertSame(String.class, actualTypeArgumentClasses.get(0));

        actualTypeArgumentClasses = resolveActualTypeArgumentClasses(D.class, B.class);
        assertTrue(actualTypeArgumentClasses.isEmpty());

        actualTypeArgumentClasses = resolveActualTypeArgumentClasses(D.class, Comparable.class);
        assertEquals(1, actualTypeArgumentClasses.size());
        assertSame(B.class, actualTypeArgumentClasses.get(0));
    }

    @Test
    void testResolveActualTypeArgumentClass() {
        assertSame(String.class, resolveActualTypeArgumentClass(D.class, C.class, 0));
        assertSame(B.class, resolveActualTypeArgumentClass(D.class, Comparable.class, 0));
    }

    @Test
    void testGetAllGenericSuperclasses() {
        List<Type> types = getAllGenericSuperclasses(A.class);
        assertEquals(1, types.size());
        assertAGenericSuperclasses(types);

        types = getAllGenericSuperclasses(B.class);
        assertEquals(2, types.size());
        assertBGenericSuperclasses(types);

        types = getAllGenericSuperclasses(C.class);
        assertEquals(3, types.size());
        assertCGenericSuperclasses(types);

        types = getAllGenericSuperclasses(D.class);
        assertEquals(4, types.size());
        assertDGenericSuperclasses(types);

        types = getAllGenericSuperclasses(E.class);
        assertEquals(4, types.size());
        assertEGenericSuperclasses(types);
    }


    @Test
    void testGetAllGenericInterfaces() {
        List<Type> types = getAllGenericInterfaces(A.class);
        assertEquals(1, types.size());
        assertAGenericInterfaces(types);

        types = getAllGenericInterfaces(B.class);
        assertEquals(2, types.size());
        assertBGenericInterfaces(types);

        types = getAllGenericInterfaces(C.class);
        assertEquals(3, types.size());
        assertCGenericInterfaces(types);

        types = getAllGenericInterfaces(D.class);
        assertEquals(3, types.size());
        assertDGenericInterfaces(types);

        types = getAllGenericInterfaces(E.class);
        assertEquals(3, types.size());
        assertEGenericInterfaces(types);
    }

    @Test
    void testGetParameterizedTypes() {
        List<ParameterizedType> genericTypes = getParameterizedTypes(A.class);
        assertSame(emptyList(), genericTypes);

        genericTypes = getParameterizedTypes(B.class);
        assertEquals(1, genericTypes.size());
        assertEquals(of(Comparable.class, B.class), genericTypes.get(0));

        genericTypes = getParameterizedTypes(C.class);
        assertSame(emptyList(), genericTypes);

        genericTypes = getParameterizedTypes(D.class);
        assertEquals(1, genericTypes.size());
        assertEquals(of(C.class, String.class), genericTypes.get(0));

        genericTypes = getParameterizedTypes(E.class);
        assertSame(emptyList(), genericTypes);
    }

    @Test
    void testGetParameterizedTypesOnNull() {
        assertSame(emptyList(), getParameterizedTypes(null));
    }

    @Test
    void testGetParameterizedTypesOnObjectClass() {
        assertSame(emptyList(), getParameterizedTypes(Object.class));
    }

    @Test
    void testGetAllParameterizedTypes() {
        List<ParameterizedType> genericTypes = getAllParameterizedTypes(A.class);
        assertSame(emptyList(), genericTypes);

        genericTypes = getAllParameterizedTypes(B.class);
        assertEquals(1, genericTypes.size());
        assertEquals(of(Comparable.class, B.class), genericTypes.get(0));

        genericTypes = getAllParameterizedTypes(C.class);
        assertEquals(1, genericTypes.size());
        assertEquals(of(Comparable.class, B.class), genericTypes.get(0));

        genericTypes = getAllParameterizedTypes(D.class);
        assertEquals(2, genericTypes.size());
        assertEquals(of(C.class, String.class), genericTypes.get(0));
        assertEquals(of(Comparable.class, B.class), genericTypes.get(1));

        genericTypes = getAllParameterizedTypes(E.class);
        assertEquals(1, genericTypes.size());
        assertEquals(of(Comparable.class, B.class), genericTypes.get(0));
    }

    @Test
    void testGetAllParameterizedTypesOnNull() {
        assertSame(emptyList(), getAllParameterizedTypes(null));
    }

    @Test
    void testGetAllParameterizedTypesOnObjectClass() {
        assertSame(emptyList(), getAllParameterizedTypes(Object.class));
    }

    @Test
    void testGetHierarchicalTypes() {
        List<Type> types = getHierarchicalTypes(A.class);
        assertValues(types, Object.class, Serializable.class);

        types = getHierarchicalTypes(B.class);
        assertValues(types, A.class, of(Comparable.class, B.class), Object.class, Serializable.class);

        types = getHierarchicalTypes(C.class);
        assertValues(types, B.class, RandomAccess.class, A.class, of(Comparable.class, B.class), Object.class, Serializable.class);

        types = getHierarchicalTypes(D.class);
        assertValues(types, of(C.class, String.class), B.class, RandomAccess.class, A.class, of(Comparable.class, B.class), Object.class, Serializable.class);

        types = getHierarchicalTypes(D.class);
        assertValues(types, of(C.class, String.class), B.class, RandomAccess.class, A.class, of(Comparable.class, B.class), Object.class, Serializable.class);

        types = getHierarchicalTypes(E.class);
        assertValues(types, C.class, Serializable.class, B.class, RandomAccess.class, A.class, of(Comparable.class, B.class), Object.class);
    }

    @Test
    void testGetAllTypes() {
        List<Type> types = getAllTypes(E.class);
        assertEquals(8, types.size());
        assertTrue(types.contains(E.class));
        assertTrue(types.contains(C.class));
        assertTrue(types.contains(B.class));
        assertTrue(types.contains(A.class));
        assertTrue(types.contains(Object.class));
        assertTrue(types.contains(Serializable.class));
        assertFalse(types.contains(Comparable.class));
        assertTrue(types.contains(RandomAccess.class));
    }


    @Test
    void testFindAllGenericSuperclasses() {
        List<Type> types = findAllGenericSuperclasses(E.class);
        assertEquals(4, types.size());
        assertTrue(types.contains(A.class));
        assertTrue(types.contains(B.class));
        assertTrue(types.contains(C.class));
        assertTrue(types.contains(Object.class));


        types = findAllGenericSuperclasses(D.class);

        assertEquals(4, types.size());
        assertTrue(types.contains(A.class));
        assertTrue(types.contains(B.class));
        assertFalse(types.contains(C.class));
        assertTrue(types.contains(Object.class));

        Iterator<Type> iterator = types.iterator();
        while (iterator.hasNext()) {
            Type type = iterator.next();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                assertEquals(C.class, parameterizedType.getRawType());
                assertEquals(String.class, parameterizedType.getActualTypeArguments()[0]);
            }
        }

        types = findAllGenericSuperclasses(D.class, TypeUtils::isParameterizedType);
        assertEquals(1, types.size());

        // null
        types = findAllGenericSuperclasses(null);
        assertTrue(types.isEmpty());
    }

    @Test
    void testFindAllGenericInterfaces() {
        List<Type> types = findAllGenericInterfaces(C.class);
        assertEquals(3, types.size());

        types = findAllGenericInterfaces(C.class, TypeUtils::isParameterizedType);

        Iterator<Type> iterator = types.iterator();
        while (iterator.hasNext()) {
            Type type = iterator.next();
            ParameterizedType parameterizedType = (ParameterizedType) type;
            assertEquals(Comparable.class, parameterizedType.getRawType());
            assertEquals(B.class, parameterizedType.getActualTypeArguments()[0]);
        }
    }

    @Test
    void testFindAllGenericInterfacesOnNull() {
        assertSame(emptyList(), findAllGenericInterfaces(null));
    }

    @Test
    void testFindParameterizedTypes() {
        List<ParameterizedType> genericTypes = findParameterizedTypes(A.class, PARAMETERIZED_TYPE_FILTER);
        assertSame(emptyList(), genericTypes);

        genericTypes = findParameterizedTypes(B.class, PARAMETERIZED_TYPE_FILTER);
        assertEquals(1, genericTypes.size());
        assertEquals(of(Comparable.class, B.class), genericTypes.get(0));

        genericTypes = findParameterizedTypes(C.class, PARAMETERIZED_TYPE_FILTER);
        assertSame(emptyList(), genericTypes);

        genericTypes = findParameterizedTypes(D.class, PARAMETERIZED_TYPE_FILTER);
        assertEquals(1, genericTypes.size());
        assertEquals(of(C.class, String.class), genericTypes.get(0));

        genericTypes = findParameterizedTypes(E.class, PARAMETERIZED_TYPE_FILTER);
        assertSame(emptyList(), genericTypes);
    }

    @Test
    void testFindParameterizedTypesOnNull() {
        assertSame(emptyList(), findParameterizedTypes(null, PARAMETERIZED_TYPE_FILTER));
    }

    @Test
    void testFindParameterizedTypesOnObjectClass() {
        assertSame(emptyList(), findParameterizedTypes(Object.class, PARAMETERIZED_TYPE_FILTER));
    }

    @Test
    void testFindAllParameterizedTypes() {
        List<ParameterizedType> genericTypes = findAllParameterizedTypes(A.class, PARAMETERIZED_TYPE_FILTER);
        assertSame(emptyList(), genericTypes);

        genericTypes = findAllParameterizedTypes(B.class, PARAMETERIZED_TYPE_FILTER);
        assertEquals(1, genericTypes.size());
        assertEquals(of(Comparable.class, B.class), genericTypes.get(0));

        genericTypes = findAllParameterizedTypes(C.class, PARAMETERIZED_TYPE_FILTER);
        assertEquals(1, genericTypes.size());
        assertEquals(of(Comparable.class, B.class), genericTypes.get(0));

        genericTypes = findAllParameterizedTypes(D.class, PARAMETERIZED_TYPE_FILTER);
        assertEquals(2, genericTypes.size());
        assertEquals(of(C.class, String.class), genericTypes.get(0));
        assertEquals(of(Comparable.class, B.class), genericTypes.get(1));

        genericTypes = findAllParameterizedTypes(E.class, PARAMETERIZED_TYPE_FILTER);
        assertEquals(1, genericTypes.size());
        assertEquals(of(Comparable.class, B.class), genericTypes.get(0));
    }

    @Test
    void testFindAllParameterizedTypesOnNull() {
        assertSame(emptyList(), findAllParameterizedTypes(null, PARAMETERIZED_TYPE_FILTER));
    }

    @Test
    void testFindAllParameterizedTypesOnObjectClass() {
        assertSame(emptyList(), findAllParameterizedTypes(Object.class, PARAMETERIZED_TYPE_FILTER));
    }

    @Test
    void testFindHierarchicalTypes() {
        List<Type> types = findHierarchicalTypes(A.class, NON_OBJECT_TYPE_FILTER);
        assertValues(types, Serializable.class);

        types = findHierarchicalTypes(E.class, WILDCARD_TYPE_FILTER);
        assertTrue(types.isEmpty());

        types = findHierarchicalTypes(D.class, GENERIC_ARRAY_TYPE_FILTER);
        assertTrue(types.isEmpty());
    }


    @Test
    void testFindAllTypes() {
        List<Type> types = findAllTypes(D.class, TypeUtils::isParameterizedType);
        assertEquals(2, types.size());
        assertTrue(types.contains(of(C.class, String.class)));
        assertTrue(types.contains(of(Comparable.class, B.class)));
    }

    @Test
    void testFindAllTypesWithoutPredicate() {
        List<Type> types = findAllTypes(D.class);
        assertEquals(8, types.size());
        assertTrue(types.contains(D.class));
        assertTrue(types.contains(of(C.class, String.class)));
        assertTrue(types.contains(RandomAccess.class));
        assertTrue(types.contains(B.class));
        assertTrue(types.contains(of(Comparable.class, B.class)));
        assertTrue(types.contains(A.class));
        assertTrue(types.contains(Serializable.class));
        assertTrue(types.contains(Object.class));
    }

    @Test
    void testFindAllTypesOnNull() {
        List<Type> types = findAllTypes(null);
        assertSame(emptyList(), types);
    }

    @Test
    void testGetClassName() {
        assertEquals("java.lang.String", getClassName(String.class));
    }

    @Test
    void testGetClassNameOnNull() {
        assertNull(getClassName(null));
    }

    @Test
    void testGetClassNames() {
        Set<String> classNames = getClassNames(ofList(String.class, Integer.class));
        assertEquals(2, classNames.size());
        assertTrue(classNames.contains("java.lang.String"));
        assertTrue(classNames.contains("java.lang.Integer"));
    }

    @Test
    void testResolveTypeArguments() {
        List<Type> typeArguments = resolveTypeArguments(A.class);
        assertTrue(typeArguments.isEmpty());

        typeArguments = resolveTypeArguments(B.class);
        assertEquals(1, typeArguments.size());
        assertEquals(B.class, typeArguments.get(0));

        typeArguments = resolveTypeArguments(C.class);
        assertEquals(1, typeArguments.size());
        assertEquals(B.class, typeArguments.get(0));

        typeArguments = resolveTypeArguments(D.class);
        assertEquals(2, typeArguments.size());
        assertEquals(String.class, typeArguments.get(0));
        assertEquals(B.class, typeArguments.get(1));

        typeArguments = resolveTypeArguments(E.class);
        assertEquals(1, typeArguments.size());
        assertEquals(B.class, typeArguments.get(0));
    }

    @Test
    void testResolveTypeArgumentsOnNull() {
        assertSame(emptyList(), resolveTypeArguments((Class) null));
    }

    @Test
    void testResolveTypeArgumentsOnPrimitiveType() {
        PRIMITIVE_TYPES.forEach(primitiveType -> {
            assertSame(emptyList(), resolveTypeArguments(primitiveType));
        });
    }

    @Test
    void testResolveTypeArgumentsOnArrayType() {
        assertSame(emptyList(), resolveTypeArguments(int[].class));
        assertSame(emptyList(), resolveTypeArguments(String[].class));
        assertSame(emptyList(), resolveTypeArguments(String[][].class));
    }

    @Test
    void testResolveTypeArgumentClasses() {
        List<Class<?>> typeArguments = resolveTypeArgumentClasses(A.class);
        assertTrue(typeArguments.isEmpty());

        typeArguments = resolveTypeArgumentClasses(B.class);
        assertEquals(1, typeArguments.size());
        assertEquals(B.class, typeArguments.get(0));

    }

    @Test
    void testDoResolveActualTypeArguments() {
        List<Type> actualTypeArguments = null;

        actualTypeArguments = doResolveActualTypeArguments(StringIntegerToBoolean.class, BiFunction.class);
        assertValues(actualTypeArguments, String.class, Integer.class, Boolean.class);

        actualTypeArguments = doResolveActualTypeArguments(StringBooleanToInteger.class, BiFunction.class);
        assertValues(actualTypeArguments, String.class, Boolean.class, Integer.class);

        actualTypeArguments = doResolveActualTypeArguments(StringToIntegerConverter.class, Converter.class);
        assertValues(actualTypeArguments, String.class, Integer.class);

        actualTypeArguments = doResolveActualTypeArguments(B.class, Comparable.class);
        assertValues(actualTypeArguments, B.class);

        actualTypeArguments = doResolveActualTypeArguments(C.class, Comparable.class);
        assertValues(actualTypeArguments, B.class);

        actualTypeArguments = doResolveActualTypeArguments(D.class, C.class);
        assertValues(actualTypeArguments, String.class);

        actualTypeArguments = doResolveActualTypeArguments(E.class, Comparable.class);
        assertValues(actualTypeArguments, B.class);

        actualTypeArguments = doResolveActualTypeArguments(StringIntegerBooleanHashMap.class, Map.class);
        assertValues(actualTypeArguments, String.class, Integer.class);


        actualTypeArguments = doResolveActualTypeArguments(StringToStringConverter.class, Converter.class);
        assertValues(actualTypeArguments, String.class, String.class);

    }

    @Test
    void testDoResolveActualTypeArgumentsOnNull() {
        assertSame(emptyList(), doResolveActualTypeArguments(null, BiFunction.class));
        assertSame(emptyList(), doResolveActualTypeArguments(BiFunction.class, null));
    }

    @Test
    void testDoResolveActualTypeArgumentsInFastPathOnNull() {
        assertSame(emptyList(), doResolveActualTypeArgumentsInFastPath(null));
    }

    @Test
    void testAsClassOnClass() {
        assertSame(String.class, asClass(String.class));
    }

    @Test
    void testAsClassOnParameterizedType() {
        assertSame(Comparable.class, asClass(of(Comparable.class, String.class)));
        assertSame(Comparable.class, asClass(B.class.getGenericInterfaces()[0]));
        assertSame(C.class, asClass(D.class.getGenericSuperclass()));
    }

    @Test
    void testAsClassOnNull() {
        assertNull(asClass(null));
    }

    @Test
    void testAsClassOnGenericArrayType() {
        assertNull(asClass(getGenericArrayType()));
    }

    @Test
    void testAsClassOnTypeVariable() {
        assertNull(asClass(getTypeVariable()));
    }

    @Test
    void testAsClassOnWildcardType() {
        assertNull(asClass(getWildcardType()));
    }

    @Test
    void testAsGenericArrayTypeOnClass() {
        assertNull(asGenericArrayType(String.class));
    }

    @Test
    void testAsGenericArrayTypeOnParameterizedType() {
        assertNull(asGenericArrayType(of(Comparable.class, String.class)));
        assertNull(asGenericArrayType(B.class.getGenericInterfaces()[0]));
        assertNull(asGenericArrayType(D.class.getGenericSuperclass()));
    }

    @Test
    void testAsGenericArrayTypeOnNull() {
        assertNull(asGenericArrayType(null));
    }

    @Test
    void testAsGenericArrayTypeOnGenericArrayType() {
        assertSame(getGenericArrayType(), asGenericArrayType(getGenericArrayType()));
    }

    @Test
    void testAsGenericArrayTypeOnTypeVariable() {
        assertNull(asGenericArrayType(getTypeVariable()));
    }

    @Test
    void testAsGenericArrayTypeOnWildcardType() {
        assertNull(asGenericArrayType(getWildcardType()));
    }

    @Test
    void testAsParameterizedTypeOnClass() {
        assertNull(asParameterizedType(String.class));
    }

    @Test
    void testAsParameterizedTypeOnParameterizedType() {
        assertEquals(of(Comparable.class, String.class), asParameterizedType(of(Comparable.class, String.class)));
        assertEquals(of(Comparable.class, B.class), asParameterizedType(B.class.getGenericInterfaces()[0]));
        assertEquals(of(C.class, String.class), asParameterizedType(D.class.getGenericSuperclass()));
    }

    @Test
    void testAsParameterizedTypeOnNull() {
        assertNull(asParameterizedType(null));
    }

    @Test
    void testAsParameterizedTypeOnGenericArrayType() {
        assertNull(asParameterizedType(getGenericArrayType()));
    }

    @Test
    void testAsParameterizedTypeOnTypeVariable() {
        assertNull(asParameterizedType(getTypeVariable()));
    }

    @Test
    void testAsParameterizedTypeOnWildcardType() {
        assertNull(asParameterizedType(getWildcardType()));
    }

    @Test
    void testAsTypeVariableOnClass() {
        assertNull(asTypeVariable(String.class));
    }

    @Test
    void testAsTypeVariableOnParameterizedType() {
        assertNull(asTypeVariable(of(Comparable.class, String.class)));
        assertNull(asTypeVariable(B.class.getGenericInterfaces()[0]));
        assertNull(asTypeVariable(D.class.getGenericSuperclass()));
    }

    @Test
    void testAsTypeVariableOnNull() {
        assertNull(asTypeVariable(null));
    }

    @Test
    void testAsTypeVariableOnGenericArrayType() {
        assertNull(asTypeVariable(getGenericArrayType()));
    }

    @Test
    void testAsTypeVariableOnTypeVariable() {
        assertSame(getTypeVariable(), asTypeVariable(getTypeVariable()));
    }

    @Test
    void testAsTypeVariableOnWildcardType() {
        assertNull(asTypeVariable(getWildcardType()));
    }

    @Test
    void testAsWildcardTypeOnClass() {
        assertNull(asWildcardType(String.class));
    }

    @Test
    void testAsWildcardTypeOnParameterizedType() {
        assertNull(asWildcardType(of(Comparable.class, String.class)));
        assertNull(asWildcardType(B.class.getGenericInterfaces()[0]));
        assertNull(asWildcardType(D.class.getGenericSuperclass()));
    }

    @Test
    void testAsWildcardTypeOnNull() {
        assertNull(asWildcardType(null));
    }

    @Test
    void testAsWildcardTypeOnGenericArrayType() {
        assertNull(asWildcardType(getGenericArrayType()));
    }

    @Test
    void testAsWildcardTypeOnTypeVariable() {
        assertNull(asWildcardType(getTypeVariable()));
    }

    @Test
    void testAsWildcardTypeOnWildcardType() {
        assertSame(getWildcardType(), asWildcardType(getWildcardType()));
    }

    @Test
    void testGetComponentTypeOnClass() {
        assertNull(getComponentType(String.class));
    }

    @Test
    void testGetComponentTypeOnClassArray() {
        assertSame(String.class, getComponentType(String[].class));
    }

    @Test
    void testGetComponentTypeOnParameterizedType() {
        assertNull(getComponentType(of(Comparable.class, String.class)));
        assertNull(getComponentType(B.class.getGenericInterfaces()[0]));
        assertNull(getComponentType(D.class.getGenericSuperclass()));
    }

    @Test
    void testGetComponentTypeOnNull() {
        assertNull(getComponentType(null));
    }

    @Test
    void testGetComponentTypeOnGenericArrayType() {
        assertTrue(isTypeVariable(getComponentType(getGenericArrayType())));
    }

    @Test
    void testGetComponentTypeOnTypeVariable() {
        assertNull(getComponentType(getTypeVariable()));
    }

    @Test
    void testGetComponentTypeOnWildcardType() {
        assertNull(getComponentType(getWildcardType()));
    }

    @Test
    void testGetTypeName() {
        assertEquals("java.lang.String", getTypeName(String.class));
        assertEquals("java.lang.Comparable<java.lang.String>", getTypeName(of(Comparable.class, String.class)));
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", getTypeName(of(Map.class, String.class, Integer.class)));
    }

    @Test
    void testGetTypeNameOnNull() {
        assertNull(getTypeName(null));
    }

    @Test
    void testGetTypeNames() {
        assertArrayEquals(ofArray("java.lang.String"), getTypeNames(String.class));
        assertArrayEquals(ofArray("java.lang.String", "java.lang.Comparable<java.lang.String>"),
                getTypeNames(String.class, of(Comparable.class, String.class)));
        assertArrayEquals(ofArray("java.lang.String", "java.lang.Comparable<java.lang.String>", "java.util.Map<java.lang.String, java.lang.Integer>"),
                getTypeNames(String.class, of(Comparable.class, String.class), of(Map.class, String.class, Integer.class)));
    }

    @Test
    void testGetTypeNamesOnNull() {
        assertArrayEquals(EMPTY_STRING_ARRAY, getTypeNames(null));
    }

    @Test
    void testGetTypeNamesOnEmptyTypeArray() {
        assertArrayEquals(EMPTY_STRING_ARRAY, getTypeNames(EMPTY_TYPE_ARRAY));
    }

    @Test
    void testGetTypeNamesOnNullElementTypeArray() {
        assertThrows(IllegalArgumentException.class, () -> getTypeNames(ofArray((Type) null)));
    }

    private Type getGenericArrayType() {
        Method toArrayMethod = findMethod(Collection.class, "toArray", Object[].class);
        return toArrayMethod.getGenericReturnType();
    }

    private Type getTypeVariable() {
        return Comparable.class.getTypeParameters()[0];
    }

    private Type getWildcardType() {
        Method andMethod = findMethod(Predicate.class, "and", Predicate.class);
        Type[] genericParameterTypes = andMethod.getGenericParameterTypes();
        Type genericParameterType = genericParameterTypes[0];
        ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;
        return parameterizedType.getActualTypeArguments()[0];
    }

    private void assertAGenericSuperclasses(List<Type> types) {
        assertTrue(types.contains(Object.class));
    }

    private void assertBGenericSuperclasses(List<Type> types) {
        assertAGenericSuperclasses(types);
        assertTrue(types.contains(A.class));
    }

    private void assertCGenericSuperclasses(List<Type> types) {
        assertBGenericSuperclasses(types);
        assertTrue(types.contains(B.class));
    }

    private void assertDGenericSuperclasses(List<Type> types) {
        assertCGenericSuperclasses(types);
        assertTrue(types.contains(of(C.class, String.class)));
    }

    private void assertEGenericSuperclasses(List<Type> types) {
        assertCGenericSuperclasses(types);
        assertTrue(types.contains(C.class));
    }

    private void assertAGenericInterfaces(List<Type> types) {
        assertTrue(types.contains(Serializable.class));
    }

    private void assertBGenericInterfaces(List<Type> types) {
        assertAGenericInterfaces(types);
        assertTrue(types.contains(of(Comparable.class, B.class)));
    }

    private void assertCGenericInterfaces(List<Type> types) {
        assertBGenericInterfaces(types);
        assertTrue(types.contains(RandomAccess.class));
    }

    private void assertDGenericInterfaces(List<Type> types) {
        assertCGenericInterfaces(types);
    }

    private void assertEGenericInterfaces(List<Type> types) {
        assertDGenericInterfaces(types);
    }

}


// MyHashMap<A, B> -> HashMap<A, B>
