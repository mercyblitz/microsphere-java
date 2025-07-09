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
import io.microsphere.annotation.processor.model.util.ConfigurationPropertyJSONElementVisitor;
import io.microsphere.json.JSONArray;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Iterator;
import java.util.Set;

import static io.microsphere.annotation.processor.ConfigurationPropertyAnnotationProcessor.CONFIGURATION_PROPERTY_ANNOTATION_CLASS_NAME;
import static io.microsphere.annotation.processor.util.MessagerUtils.printNote;
import static io.microsphere.constants.SymbolConstants.COMMA_CHAR;
import static io.microsphere.constants.SymbolConstants.LEFT_SQUARE_BRACKET_CHAR;
import static io.microsphere.constants.SymbolConstants.RIGHT_SQUARE_BRACKET_CHAR;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.tools.StandardLocation.CLASS_OUTPUT;

/**
 * The {@link Processor} for the {@link ConfigurationProperty} annotation
 *
 * <p>This class processes the {@link ConfigurationProperty} annotations during the compilation phase,
 * collects metadata about annotated elements, and generates a JSON metadata file that contains
 * configuration property information.
 *
 * <ul>
 *     <li>{@link #init(ProcessingEnvironment)} initializes required utilities such as the Messager and ResourceProcessor.</li>
 *     <li>{@link #process(Set, RoundEnvironment)} handles each processing round:
 *         <ul>
 *             <li>During normal rounds, it resolves metadata from annotated elements.</li>
 *             <li>On the final round, it writes collected metadata into a resource file.</li>
 *         </ul>
 *     </li>
 *     <li>{@link #resolveMetadata(RoundEnvironment)} traverses all root elements to extract configuration property metadata.</li>
 *     <li>{@link #writeMetadata()} writes the generated metadata into a JSON file under
 *         {@value #CONFIGURATION_PROPERTY_METADATA_RESOURCE_NAME} using a writer.</li>
 * </ul>
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ConfigurationProperty
 * @since 1.0.0
 */
@SupportedAnnotationTypes(value = CONFIGURATION_PROPERTY_ANNOTATION_CLASS_NAME)
public class ConfigurationPropertyAnnotationProcessor extends AbstractProcessor {

    /**
     * The {@link Class class} name of {@link ConfigurationProperty}
     */
    public static final String CONFIGURATION_PROPERTY_ANNOTATION_CLASS_NAME = "io.microsphere.annotation.ConfigurationProperty";

    /**
     * The resource name of {@link ConfigurationProperty} metadata
     */
    public static final String CONFIGURATION_PROPERTY_METADATA_RESOURCE_NAME = "META-INF/microsphere/configuration-properties.json";

    private Messager messager;

    private StringBuilder jsonBuilder;

    private ResourceProcessor classPathResourceProcessor;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.jsonBuilder = new StringBuilder();
        this.classPathResourceProcessor = new ResourceProcessor(processingEnv, CLASS_OUTPUT);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            writeMetadata();
        } else {
            resolveMetadata(roundEnv);
        }
        return false;
    }

    private void resolveMetadata(RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getRootElements();
        if (!elements.isEmpty()) {
            ConfigurationPropertyJSONElementVisitor visitor = new ConfigurationPropertyJSONElementVisitor(processingEnv);

            Iterator<? extends Element> iterator = elements.iterator();
            jsonBuilder.append(LEFT_SQUARE_BRACKET_CHAR);
            while (iterator.hasNext()) {
                Element element = iterator.next();
                element.accept(visitor, jsonBuilder);
            }

            int lastIndex = jsonBuilder.length() - 1;
            if (COMMA_CHAR == jsonBuilder.charAt(lastIndex)) {
                jsonBuilder.setCharAt(lastIndex, RIGHT_SQUARE_BRACKET_CHAR);
            } else {
                jsonBuilder.append(RIGHT_SQUARE_BRACKET_CHAR);
            }
        }
    }

    private void writeMetadata() {
        classPathResourceProcessor.processInResourceWriter(CONFIGURATION_PROPERTY_METADATA_RESOURCE_NAME, writer -> {
            JSONArray jsonArray = new JSONArray(jsonBuilder.toString());
            String formatedJSON = jsonArray.toString(2);
            writer.write(formatedJSON);
            printNote(this.messager, "The generated metadata JSON of @{} : \n{}", CONFIGURATION_PROPERTY_ANNOTATION_CLASS_NAME, formatedJSON);
        });
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }
}
 