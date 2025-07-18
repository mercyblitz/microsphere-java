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
package io.microsphere.convert.multiple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.microsphere.util.ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY;
import static java.lang.Integer.MAX_VALUE;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link StringToArrayConverter} Test
 *
 * @since 1.0.0
 */
class StringToArrayConverterTest {

    private StringToArrayConverter converter;

    @BeforeEach
    void init() {
        converter = new StringToArrayConverter();
    }

    @Test
    void testAccept() {
        assertTrue(converter.accept(String.class, char[].class));
        assertTrue(converter.accept(null, char[].class));
        assertFalse(converter.accept(null, String.class));
        assertFalse(converter.accept(null, String.class));
        assertFalse(converter.accept(null, null));
    }

    @Test
    void testConvert() {
        assertArrayEquals(new Integer[]{123}, (Integer[]) converter.convert("123", Integer[].class, Integer.class));
        assertArrayEquals(new Integer[]{1, 2, 3}, (Integer[]) converter.convert("1,2,3", Integer[].class, null));
        assertArrayEquals(EMPTY_INTEGER_OBJECT_ARRAY, (Integer[]) converter.convert("", Integer[].class, null));
        assertNull(converter.convert(null, Integer[].class, null));
    }

    @Test
    void testGetSourceType() {
        assertEquals(String.class, converter.getSourceType());
    }

    @Test
    void testGetPriority() {
        assertEquals(MAX_VALUE, converter.getPriority());
    }
}
