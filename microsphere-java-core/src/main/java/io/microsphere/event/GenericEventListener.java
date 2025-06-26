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

import io.microsphere.lang.Prioritized;
import io.microsphere.lang.function.ThrowableFunction;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import static io.microsphere.lang.function.ThrowableConsumer.execute;
import static java.util.Collections.emptySet;
import static java.util.stream.Stream.of;

/**
 * A generic implementation of the {@link EventListener} interface that supports multiple event handling methods.
 *
 * <p>This class provides a flexible way to handle events by allowing subclasses to define additional event handler methods,
 * beyond the mandatory {@link EventListener#onEvent(Event)} method, which is declared as final and cannot be overridden.
 * Subclasses can define any number of custom event handler methods, provided they meet certain criteria:
 *
 * <ul>
 *   <li>The method must not be the same as the {@link #onEvent(Event)} method.</li>
 *   <li>It must be declared as <code>public</code>.</li>
 *   <li>It must return <code>void</code>.</li>
 *   <li>It must not declare any exceptions.</li>
 *   <li>It must accept exactly one parameter, which must be a subclass of {@link Event} or an instance of it.</li>
 * </ul>
 *
 * <h3>Example</h3>
 * Here's how you might extend this class to handle specific types of events:
 *
 * <pre>{@code
 * public class MyGenericEventListener extends GenericEventListener {
 *
 *     // Custom event handler method
 *     public void onMyEvent(MyEvent event) {
 *         System.out.println("Received MyEvent: " + event);
 *     }
 *
 *     // Another event handler for a different type of event
 *     public void onAnotherEvent(AnotherEvent event) {
 *         System.out.println("Received AnotherEvent: " + event);
 *     }
 * }
 * }</pre>
 *
 * When an event is received via the final {@link #onEvent(Event)} method, it will automatically invoke all matching
 * handler methods defined in the subclass. This allows for a dynamic dispatch mechanism based on the runtime type of the event.
 *
 * <p>For example, if the above listener receives a {@code MyEvent}, it will call the <code>onMyEvent()</code> method.
 * Similarly, for an instance of {@code AnotherEvent}, it will call the <code>onAnotherEvent()</code> method.
 *
 * <p>This class also supports priority-based ordering through its implementation of the {@link Prioritized} interface,
 * allowing instances to be sorted based on their priority values.
 *
 * @see Event
 * @see EventListener
 * @see Prioritized
 * @since 1.0.0
 */
public abstract class GenericEventListener implements EventListener<Event> {

    private final Method onEventMethod;

    private final Map<Class<?>, Set<Method>> handleEventMethods;

    protected GenericEventListener() {
        this.onEventMethod = findOnEventMethod();
        this.handleEventMethods = findHandleEventMethods();
    }

    private Method findOnEventMethod() {
        return ThrowableFunction.execute(getClass(), listenerClass -> listenerClass.getMethod("onEvent", Event.class));
    }

    private Map<Class<?>, Set<Method>> findHandleEventMethods() {
        // Event class for key, the eventMethods' Set as value
        Map<Class<?>, Set<Method>> eventMethods = new HashMap<>();
        of(getClass().getMethods()).filter(this::isHandleEventMethod).forEach(method -> {
            Class<?> paramType = method.getParameterTypes()[0];
            Set<Method> methods = eventMethods.computeIfAbsent(paramType, key -> new LinkedHashSet<>());
            methods.add(method);
        });
        return eventMethods;
    }

    public final void onEvent(Event event) {
        Class<?> eventClass = event.getClass();
        handleEventMethods.getOrDefault(eventClass, emptySet()).forEach(method -> {
            execute(method, m -> {
                m.invoke(this, event);
            });
        });
    }

    /**
     * The {@link Event event} handle methods must meet following conditions:
     * <ul>
     * <li>not {@link #onEvent(Event)} method</li>
     * <li><code>public</code> accessibility</li>
     * <li><code>void</code> return type</li>
     * <li>no {@link Exception exception} declaration</li>
     * <li>only one {@link Event} type argument</li>
     * </ul>
     *
     * @param method
     * @return
     */
    protected boolean isHandleEventMethod(Method method) {

        if (onEventMethod.equals(method)) { // not {@link #onEvent(Event)} method
            return false;
        }

        if (!void.class.equals(method.getReturnType())) { // void return type
            return false;
        }

        Class[] exceptionTypes = method.getExceptionTypes();

        if (exceptionTypes.length > 0) { // no exception declaration
            return false;
        }

        Class[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 1) { // not only one argument
            return false;
        }

        // not Event type argument
        return Event.class.isAssignableFrom(paramTypes[0]);
    }
}
