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

import static io.microsphere.convert.ObjectToCharacterConverter.INSTANCE;
import static java.lang.Character.valueOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link ObjectToCharacterConverter} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ObjectToCharacterConverter
 * @since 1.0.0
 */
class ObjectToCharacterConverterTest extends BaseConverterTest<Object, Character> {

    @Override
    protected AbstractConverter<Object, Character> createConverter() {
        return INSTANCE;
    }

    @Override
    protected Object getSource() {
        return valueOf((char) 1);
    }

    @Override
    protected Character getTarget() {
        return valueOf((char) 1);
    }

    @Test
    void testCovertMore() {
        assertEquals(getTarget(), this.converter.convert(1));
    }
}
