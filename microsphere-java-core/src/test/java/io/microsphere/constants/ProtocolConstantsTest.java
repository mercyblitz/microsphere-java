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
package io.microsphere.constants;

import org.junit.jupiter.api.Test;

import static io.microsphere.constants.ProtocolConstants.CLASSPATH_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.CONSOLE_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.EAR_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.FILE_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.FTP_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.HTTPS_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.HTTP_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.JAR_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.WAR_PROTOCOL;
import static io.microsphere.constants.ProtocolConstants.ZIP_PROTOCOL;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link ProtocolConstants} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 */
class ProtocolConstantsTest {

    @Test
    void test() {
        assertEquals("file", FILE_PROTOCOL);
        assertEquals("http", HTTP_PROTOCOL);
        assertEquals("https", HTTPS_PROTOCOL);
        assertEquals("ftp", FTP_PROTOCOL);
        assertEquals("zip", ZIP_PROTOCOL);
        assertEquals("jar", JAR_PROTOCOL);
        assertEquals("war", WAR_PROTOCOL);
        assertEquals("ear", EAR_PROTOCOL);
        assertEquals("classpath", CLASSPATH_PROTOCOL);
        assertEquals("console", CONSOLE_PROTOCOL);
    }
}
