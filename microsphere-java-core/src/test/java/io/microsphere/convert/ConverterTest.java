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
package io.microsphere.convert;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static io.microsphere.convert.Converter.convertIfPossible;
import static io.microsphere.convert.Converter.getConverter;
import static io.microsphere.util.ServiceLoaderUtils.loadServicesList;
import static java.lang.Boolean.FALSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * {@link Converter} Test-Cases
 *
 * @since 1.0.0
 */
class ConverterTest {

    @Test
    void testGetConverter() {
        loadServicesList(Converter.class, Converter.class.getClassLoader()).stream().sorted().forEach(converter -> {
            assertSame(converter.getClass(), getConverter(converter.getSourceType(), converter.getTargetType()).getClass());
        });
    }

    @Test
    void testConvertIfPossible() {
        assertEquals(Integer.valueOf(2), convertIfPossible("2", Integer.class));
        assertEquals(FALSE, convertIfPossible("false", Boolean.class));
        assertEquals(Double.valueOf(1), convertIfPossible("1", Double.class));
        assertNull(convertIfPossible("1", Date.class));
    }
}
