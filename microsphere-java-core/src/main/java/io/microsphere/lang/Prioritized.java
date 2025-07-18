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
package io.microsphere.lang;

import java.util.Comparator;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Integer.compare;

/**
 * {@code Prioritized} interface can be implemented by objects that
 * should be sorted, for example the tasks in executable queue.
 *
 * <p>Implementing classes should typically also implement the {@link #getPriority()} method
 * to define their priority value. The priority is used to determine ordering via the
 * {@link #compareTo(Prioritized)} method, which compares based on the returned priority values.
 *
 * <h3>Example Usage</h3>
 * <pre>{@code
 * public class MyTask implements Prioritized {
 *     private final int priority;
 *
 *     public MyTask(int priority) {
 *         this.priority = priority;
 *     }
 *
 *     public int getPriority() {
 *         return priority;
 *     }
 * }
 *
 * // Sorting a list of Prioritized objects
 * List<Prioritized> tasks = new ArrayList<>();
 * tasks.add(new MyTask(5));
 * tasks.add(new MyTask(1));
 * Collections.sort(tasks);
 * }</pre>
 *
 * @since 1.0.0
 */
public interface Prioritized extends Comparable<Prioritized> {

    /**
     * The {@link Comparator} of {@link Prioritized}
     */
    Comparator<Object> COMPARATOR = (one, two) -> {
        boolean b1 = one instanceof Prioritized;
        boolean b2 = two instanceof Prioritized;
        if (b1 && !b2) {        // one is Prioritized, two is not
            return -1;
        } else if (b2 && !b1) { // two is Prioritized, one is not
            return 1;
        } else if (b1 && b2) {  //  one and two both are Prioritized
            return ((Prioritized) one).compareTo((Prioritized) two);
        } else {                // no different
            return 0;
        }
    };

    /**
     * The maximum priority
     */
    int MAX_PRIORITY = MIN_VALUE;

    /**
     * The minimum priority
     */
    int MIN_PRIORITY = MAX_VALUE;

    /**
     * Normal Priority
     */
    int NORMAL_PRIORITY = 0;

    /**
     * Get the priority
     *
     * @return the default is {@link #MIN_PRIORITY minimum one}
     */
    default int getPriority() {
        return NORMAL_PRIORITY;
    }

    @Override
    default int compareTo(Prioritized that) {
        return compare(this.getPriority(), that.getPriority());
    }
}
