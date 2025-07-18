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

package io.microsphere.collection;


import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.LinkedList;

import static io.microsphere.collection.QueueUtils.emptyDeque;
import static io.microsphere.collection.ReversedDeque.of;
import static io.microsphere.util.ArrayUtils.ofArray;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link ReversedDeque} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ReversedDeque
 * @since 1.0.0
 */
class ReversedDequeTest extends MutableDequeTest<Deque<Object>> {

    @Override
    protected Deque<Object> newInstance() {
        Deque<Object> reversedDeque = of(of(new LinkedList<>()));
        return reversedDeque;
    }

    @Test
    void testEquals() {
        super.testEquals();
        assertEquals(of(emptyDeque()), of(emptyDeque()));
    }

    @Test
    void testToString() {
        super.testToString();
        assertEquals("[]", of(emptyDeque()).toString());

        Deque<Object> deque = newInstance();
        deque.add(deque);
        assertEquals("[(this Collection)]", deque.toString());
    }

    @Test
    void testStream() {
        assertArrayEquals(ofArray("C", "B", "A"), this.instance.stream().toArray());
    }

    @Test
    void testParallelStream() {
        assertArrayEquals(ofArray("C", "B", "A"), this.instance.parallelStream().toArray());
    }
}