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
package io.microsphere.junit.jupiter.api.extension.annotation;

import io.microsphere.junit.jupiter.api.extension.UtilsTestBeforeAllExtension;
import io.microsphere.util.Utils;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotation of jupiter extension for the utilities class.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see UtilsTestBeforeAllExtension
 * @see Utils
 * @since 1.0.0
 */
@Target({TYPE, METHOD})
@Retention(RUNTIME)
@ExtendWith({UtilsTestBeforeAllExtension.class})
public @interface UtilsTestExtension {
}
