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
package io.microsphere.util;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import static io.microsphere.util.ClassUtils.isArray;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * {@link ClassUtils} Benchmark
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see StackTraceUtils
 * @since 1.0.0
 */
@Warmup(iterations = 5, time = 1, timeUnit = SECONDS)
@Measurement(iterations = 20, time = 1, timeUnit = SECONDS)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(NANOSECONDS)
@State(Scope.Thread)
public class ClassUtilsBenchmark {

    private static final Class<int[]> type = int[].class;

    @Benchmark
    public void testClassIsArray() {
        type.isArray();
    }

    @Benchmark
    public void testClassUtilsIsArray() {
        isArray(type);
    }
}
