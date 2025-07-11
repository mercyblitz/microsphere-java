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

import static java.lang.String.valueOf;

/**
 * The class to convert {@link String} to {@link String}
 *
 * @since 1.0.0
 */
public class StringToStringConverter extends AbstractConverter<String, String> implements StringConverter<String> {

    /**
     * Singleton instance of {@link StringToStringConverter}.
     */
    public static final StringToStringConverter INSTANCE = new StringToStringConverter();

    @Override
    protected String doConvert(String source) {
        return valueOf(source);
    }
}
