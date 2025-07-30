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
package io.microsphere.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.microsphere.collection.Lists.ofList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link DirectEventDispatcher} Test
 *
 * @since 1.0.0
 */
class DirectEventDispatcherTest {

    private DirectEventDispatcher dispatcher;

    private EchoEventListener echoEventListener;

    private EchoEventListener2 echoEventListener2;

    @BeforeEach
    void setUp() {
        dispatcher = new DirectEventDispatcher();
        dispatcher.removeAllEventListeners();
        echoEventListener = new EchoEventListener();
        echoEventListener2 = new EchoEventListener2();
    }

    @Test
    void testGetExecutor() {
        assertNotNull(dispatcher.getExecutor());
    }

    @Test
    void testGetAllListeners() {
        assertTrue(dispatcher.getAllEventListeners().isEmpty());
    }

    @Test
    void testSingleListener() {
        // add two listeners
        dispatcher.addEventListener(echoEventListener);
        dispatcher.addEventListener(echoEventListener2);
        assertEquals(ofList(echoEventListener2, echoEventListener), dispatcher.getAllEventListeners());

        // add a duplicated listener
        dispatcher.addEventListener(echoEventListener);
        assertEquals(ofList(echoEventListener2, echoEventListener), dispatcher.getAllEventListeners());

        // remove
        dispatcher.removeEventListener(echoEventListener);
        assertEquals(ofList((EventListener) echoEventListener2), dispatcher.getAllEventListeners());

        dispatcher.removeEventListener(echoEventListener2);
        assertEquals(emptyList(), dispatcher.getAllEventListeners());
    }

    @Test
    void testMultipleListeners() {

        // add two listeners
        dispatcher.addEventListeners(echoEventListener, echoEventListener2);
        assertEquals(ofList(echoEventListener2, echoEventListener), dispatcher.getAllEventListeners());

        // remove all listeners
        dispatcher.removeAllEventListeners();
        assertEquals(emptyList(), dispatcher.getAllEventListeners());

        // add the duplicated listeners
        dispatcher.addEventListeners(echoEventListener, echoEventListener, echoEventListener2);
        assertEquals(ofList(echoEventListener2, echoEventListener), dispatcher.getAllEventListeners());

        // remove all listeners
        dispatcher.removeAllEventListeners();
        assertEquals(emptyList(), dispatcher.getAllEventListeners());

        dispatcher.addEventListeners(ofList(echoEventListener, echoEventListener, echoEventListener2));
        assertEquals(ofList(echoEventListener2, echoEventListener), dispatcher.getAllEventListeners());

        dispatcher.removeEventListeners(ofList(echoEventListener, echoEventListener, echoEventListener2));
        assertEquals(emptyList(), dispatcher.getAllEventListeners());
    }

    @Test
    void testDispatchEvent() {

        dispatcher.addEventListener(echoEventListener);

        // dispatch a Event
        dispatcher.dispatch(new Event("Test") {
        });

        // no-op occurs
        assertEquals(0, echoEventListener.getEventOccurs());

        // dispatch a EchoEvent
        dispatcher.dispatch(new EchoEvent("Hello,World"));

        // event has been handled
        assertEquals(1, echoEventListener.getEventOccurs());

        dispatcher.addEventListener(echoEventListener2);

        // reset the listeners
        setUp();
        dispatcher.addEventListeners(echoEventListener, echoEventListener2);

        // dispatch a Event
        dispatcher.dispatch(new Event("Test") {
        });

        // echoEventListener will be not triggered + 0
        // echoEventListener2 will be triggered    + 1
        assertEquals(0, echoEventListener.getEventOccurs());
        assertEquals(1, echoEventListener2.getEventOccurs());

        // dispatch a EchoEvent
        // echoEventListener and echoEventListener2 are triggered both (+1)
        dispatcher.dispatch(new EchoEvent("Hello,World"));
        assertEquals(1, echoEventListener.getEventOccurs());
        assertEquals(2, echoEventListener2.getEventOccurs());

        // both +1
        dispatcher.dispatch(new EchoEvent("2019"));
        assertEquals(2, echoEventListener.getEventOccurs());
        assertEquals(3, echoEventListener2.getEventOccurs());
    }
}
