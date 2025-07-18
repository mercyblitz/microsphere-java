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

package io.microsphere.lang;


import org.junit.jupiter.api.Test;

import static io.microsphere.lang.MutableInteger.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MutableIntegerTest {

    @Test
    void testConstructor() {
        MutableInteger adder = new MutableInteger(10);
        assertEquals(10, adder.get());
    }

    @Test
    void testOfFactoryMethod() {
        MutableInteger adder = of(5);
        assertEquals(5, adder.get());
    }

    @Test
    void testGet() {
        MutableInteger adder = of(7);
        assertEquals(7, adder.get());
    }

    @Test
    void testSet() {
        MutableInteger adder = of(0);
        MutableInteger returned = adder.set(100);
        assertSame(adder, returned); // 返回自身
        assertEquals(100, adder.get());
    }

    @Test
    void testGetAndSet() {
        MutableInteger adder = of(5);
        int oldValue = adder.getAndSet(20);
        assertEquals(5, oldValue);
        assertEquals(20, adder.get());
    }

    @Test
    void testIncrementAndGet() {
        MutableInteger adder = of(5);
        int newValue = adder.incrementAndGet();
        assertEquals(6, newValue);
        assertEquals(6, adder.get());
    }

    @Test
    void testDecrementAndGet() {
        MutableInteger adder = of(5);
        int newValue = adder.decrementAndGet();
        assertEquals(4, newValue);
        assertEquals(4, adder.get());
    }

    @Test
    void testGetAndIncrement() {
        MutableInteger adder = of(5);
        int oldValue = adder.getAndIncrement();
        assertEquals(5, oldValue);
        assertEquals(6, adder.get());
    }

    @Test
    void testGetAndDecrement() {
        MutableInteger adder = of(5);
        int oldValue = adder.getAndDecrement();
        assertEquals(5, oldValue);
        assertEquals(4, adder.get());
    }

    @Test
    void testAddAndGet() {
        MutableInteger adder = of(5);
        int newValue = adder.addAndGet(3);
        assertEquals(8, newValue);
        assertEquals(8, adder.get());
    }

    @Test
    void testGetAndAdd() {
        MutableInteger adder = of(5);
        int oldValue = adder.getAndAdd(3);
        assertEquals(5, oldValue);
        assertEquals(8, adder.get());
    }

    @Test
    void testNumberConversionMethods() {
        MutableInteger adder = of(10);
        assertEquals(10, adder.intValue());
        assertEquals(10L, adder.longValue());
        assertEquals(10.0f, adder.floatValue());
        assertEquals(10.0d, adder.doubleValue());
    }

    @Test
    void testToString() {
        MutableInteger adder = of(123);
        assertEquals("123", adder.toString());
    }

    @Test
    void testHashCode() {
        MutableInteger adder = of(42);
        assertEquals(42, adder.hashCode());
    }

    @Test
    void testEquals() {
        MutableInteger a = of(10);
        MutableInteger b = of(10);
        MutableInteger c = of(20);

        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertFalse(a.equals(null));
        assertFalse(a.equals("not an IntegerAdder"));
    }
}