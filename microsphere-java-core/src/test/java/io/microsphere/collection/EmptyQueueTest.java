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

import java.util.NoSuchElementException;
import java.util.Queue;

import static io.microsphere.collection.CollectionUtils.emptyIterator;
import static io.microsphere.collection.CollectionUtils.emptyQueue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Empty {@link Queue} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see EmptyDeque
 * @since 1.0.0
 */
class EmptyQueueTest {

    private Queue<String> emptyDeque = emptyQueue();

    @Test
    void testIterator() {
        assertSame(emptyIterator(), emptyDeque.iterator());
    }

    @Test
    void testAdd() {
        assertThrows(UnsupportedOperationException.class, () -> emptyDeque.add("test"));
    }

    @Test
    void testOffer() {
        assertThrows(UnsupportedOperationException.class, () -> emptyDeque.offer("test"));
    }

    @Test
    void testRemove() {
        assertThrows(NoSuchElementException.class, emptyDeque::remove);
        assertFalse(emptyDeque.remove("test"));
    }

    @Test
    void testPoll() {
        assertNull(emptyDeque.poll());
    }

    @Test
    void testElement() {
        assertThrows(NoSuchElementException.class, emptyDeque::element);
    }

    @Test
    void testPeek() {
        assertNull(emptyDeque.peek());
    }

    @Test
    void testSize() {
        assertEquals(0, emptyDeque.size());
    }
}