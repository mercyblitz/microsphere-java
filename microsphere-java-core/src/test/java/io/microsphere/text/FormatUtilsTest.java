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
package io.microsphere.text;

import org.junit.jupiter.api.Test;

import static io.microsphere.constants.SymbolConstants.SPACE;
import static io.microsphere.text.FormatUtils.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * {@link FormatUtils} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
class FormatUtilsTest {

    @Test
    void testFormat() {
        assertNull(format(null));

        assertEquals("", format(""));

        assertEquals(SPACE, format(SPACE));

        String message = format("A,{},C,{},E", "B", "D");
        assertEquals("A,B,C,D,E", message);

        message = format("A,{},C,{},E", "B");
        assertEquals("A,B,C,{},E", message);

        message = format("A,{},C,{},E");
        assertEquals("A,{},C,{},E", message);

        message = format("A,{},C,{},E", 1, 2, 3);
        assertEquals("A,1,C,2,E", message);
    }
}
