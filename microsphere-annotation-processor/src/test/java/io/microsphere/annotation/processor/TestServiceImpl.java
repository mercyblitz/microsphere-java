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
package io.microsphere.annotation.processor;

import io.microsphere.annotation.ConfigurationProperty;
import io.microsphere.annotation.Since;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.io.Serializable;

import static java.util.concurrent.TimeUnit.HOURS;
import static org.springframework.context.annotation.FilterType.ASPECTJ;
import static org.springframework.context.annotation.ScopedProxyMode.INTERFACES;

/**
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @since 1.0.0
 */
@Service("testService")
@ServiceMode
@ComponentScans(value = {
        @ComponentScan(
                basePackages = "io.microsphere.annotation.processor.model",
                scopedProxy = INTERFACES
        ),
        @ComponentScan(
                basePackages = "io.microsphere.annotation.processor.util",
                includeFilters = {
                        @ComponentScan.Filter(
                                type = ASPECTJ,
                                classes = {Object.class, CharSequence.class}
                        )
                })
})
@TestAnnotation(
        z = true,
        c = 'b',
        b = 1,
        s = 1,
        i = 1,
        l = 1,
        f = 1,
        d = 1,
        string = "testService",
        type = GenericTestService.class,
        types = {TestService.class, AutoCloseable.class, Serializable.class},
        timeUnit = HOURS,
        since = @Since("1.0.0"),
        properties = {
                @ConfigurationProperty(name = "key", type = String.class, defaultValue = "default-value", required = true, description = "description"),
                @ConfigurationProperty(name = "key2", type = Integer.class, defaultValue = "default-value2", required = true, description = "description2"),
                @ConfigurationProperty(name = "key3", type = Class.class, defaultValue = "default-value3", required = true, description = "description3")
        }
)
public class TestServiceImpl extends GenericTestService implements TestService, AutoCloseable, Serializable {

    @Autowired
    private ApplicationContext context;

    private Environment environment;

    public TestServiceImpl() {
        this(null);
    }

    public TestServiceImpl(@Autowired Environment environment) {
        this.environment = environment;
    }

    @Override
    @Cacheable(cacheNames = {"cache-1", "cache-2"})
    public String echo(String message) {
        return "[ECHO] " + message;
    }

    @Override
    public void close() throws Exception {
    }
}
