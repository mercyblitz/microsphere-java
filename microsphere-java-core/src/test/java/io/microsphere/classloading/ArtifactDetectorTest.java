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
package io.microsphere.classloading;


import org.junit.jupiter.api.Test;

import java.util.List;

import static io.microsphere.util.ClassLoaderUtils.getDefaultClassLoader;
import static java.util.Collections.emptySet;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link ArtifactDetector} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see ArtifactDetector
 * @since 1.0.0
 */
class ArtifactDetectorTest {

    @Test
    void testDetect() {
        ArtifactDetector instance = new ArtifactDetector();
        List<Artifact> artifacts = instance.detect();
        assertNotNull(artifacts);
    }

    @Test
    void testDetectOnNullSet() {
        ArtifactDetector instance = new ArtifactDetector(getDefaultClassLoader());
        assertTrue(instance.detect(null).isEmpty());
    }

    @Test
    void testDetectOnEmptySet() {
        ArtifactDetector instance = new ArtifactDetector(null);
        assertTrue(instance.detect(emptySet()).isEmpty());
    }
}
