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
package io.microsphere.annotation.processor.util;

import org.junit.jupiter.api.Test;

import static io.microsphere.annotation.processor.util.LoggerUtils.LOGGER;
import static io.microsphere.annotation.processor.util.LoggerUtils.debug;
import static io.microsphere.annotation.processor.util.LoggerUtils.error;
import static io.microsphere.annotation.processor.util.LoggerUtils.info;
import static io.microsphere.annotation.processor.util.LoggerUtils.trace;
import static io.microsphere.annotation.processor.util.LoggerUtils.warn;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link LoggerUtils} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
class LoggerUtilsTest {

    @Test
    void testLogger() {
        assertNotNull(LOGGER);
    }

    @Test
    void testTrace() {
        trace("Hello,World");
        trace("Hello,{}", "World");
        trace("{},{}", "Hello", "World");
    }

    @Test
    void testDebug() {
        debug("Hello,World");
        debug("Hello,{}", "World");
        debug("{},{}", "Hello", "World");
    }

    @Test
    void testInfo() {
        info("Hello,World");
        info("Hello,{}", "World");
        info("{},{}", "Hello", "World");
    }

    @Test
    void testWarn() {
        warn("Hello,World");
        warn("Hello,{}", "World");
        warn("{},{}", "Hello", "World");
    }

    @Test
    void testError() {
        error("Hello,World");
        error("Hello,{}", "World");
        error("{},{}", "Hello", "World");
    }
}
