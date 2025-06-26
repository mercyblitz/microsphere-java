/**
 *
 */
package io.microsphere.io.scanner;

import io.microsphere.filter.PackageNameClassNameFilter;
import io.microsphere.lang.ClassDataRepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static io.microsphere.filter.FilterUtils.filter;
import static io.microsphere.lang.function.Streams.filterAll;
import static io.microsphere.net.URLUtils.ofURL;
import static io.microsphere.net.URLUtils.resolveArchiveFile;
import static io.microsphere.util.ClassLoaderUtils.ResourceType.PACKAGE;
import static io.microsphere.util.ClassLoaderUtils.findLoadedClass;
import static io.microsphere.util.ClassLoaderUtils.getResources;
import static io.microsphere.util.ClassLoaderUtils.loadClass;
import static io.microsphere.util.ClassUtils.findClassNamesInClassPath;
import static io.microsphere.util.StringUtils.substringBefore;
import static java.util.Collections.unmodifiableSet;

/**
 * A simple scanner for scanning {@link Class} objects under a specified package or archive.
 *
 * <p>This class provides various methods to scan and retrieve classes either by package name,
 * resource URL, or archive file. It supports filtering based on classloader, package recursion,
 * and custom predicates.</p>
 *
 * <h3>Examples</h3>
 *
 * <p>Scan all classes in a package without loading them:</p>
 * <pre>{@code
 * SimpleClassScanner scanner = new SimpleClassScanner();
 * Set<Class<?>> classes = scanner.scan(classLoader, "com.example.package");
 * }</pre>
 *
 * <p>Scan and load all classes recursively in a package:</p>
 * <pre>{@code
 * Set<Class<?>> loadedClasses = scanner.scan(classLoader, "com.example.package", true, true);
 * }</pre>
 *
 * <p>Scan classes from an archive file with filters:</p>
 * <pre>{@code
 * File archiveFile = new File("path/to/archive.jar");
 * Predicate<Class<?>> myFilter = cls -> cls.isAnnotationPresent(MyAnnotation.class);
 * Set<Class<?>> filteredClasses = scanner.scan(classLoader, archiveFile, false, myFilter);
 * }</pre>
 *
 * @author <a href="mercyblitz@gmail.com">Mercy<a/>
 * @see SimpleClassScanner
 * @since 1.0.0
 */
public class SimpleClassScanner {

    /**
     * Singleton
     */
    public final static SimpleClassScanner INSTANCE = new SimpleClassScanner();

    public SimpleClassScanner() {
    }

    /**
     * It's equal to invoke {@link #scan(ClassLoader, String, boolean, boolean)} method with
     * <code>requiredLoad=false</code> and <code>recursive=false</code>
     *
     * @param classLoader {@link ClassLoader}
     * @param packageName the name of package
     * @return {@link #scan(ClassLoader, String, boolean, boolean)} method with <code>requiredLoad=false</code>
     * @throws IllegalArgumentException scanned source is not legal
     * @throws IllegalStateException    scanned source's state is not valid
     */
    public Set<Class<?>> scan(ClassLoader classLoader, String packageName) throws IllegalArgumentException, IllegalStateException {
        return scan(classLoader, packageName, false);
    }

    /**
     * It's equal to invoke {@link #scan(ClassLoader, String, boolean, boolean)} method with
     * <code>requiredLoad=false</code>
     *
     * @param classLoader {@link ClassLoader}
     * @param packageName the name of package
     * @param recursive   included sub-package
     * @return {@link #scan(ClassLoader, String, boolean, boolean)} method with <code>requiredLoad=false</code>
     * @throws IllegalArgumentException scanned source is not legal
     * @throws IllegalStateException    scanned source's state is not valid
     */
    public Set<Class<?>> scan(ClassLoader classLoader, String packageName, boolean recursive) throws IllegalArgumentException, IllegalStateException {
        return scan(classLoader, packageName, recursive, false);
    }


    /**
     * scan {@link Class} set under specified package name or its' sub-packages in {@link ClassLoader}, if
     * <code>requiredLoad</code> indicates <code>true</code> , try to load those classes.
     *
     * @param classLoader  {@link ClassLoader}
     * @param packageName  the name of package
     * @param recursive    included sub-package
     * @param requiredLoad try to load those classes or not
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public Set<Class<?>> scan(ClassLoader classLoader, String packageName, final boolean recursive, boolean requiredLoad) throws IllegalArgumentException, IllegalStateException {
        Set<Class<?>> classesSet = new LinkedHashSet();

        final String packageResourceName = PACKAGE.resolve(packageName);

        try {
            Set<String> classNames = new LinkedHashSet();
            // Find in class loader
            Set<URL> resourceURLs = getResources(classLoader, PACKAGE, packageName);

            if (resourceURLs.isEmpty()) {
                //Find in class path
                ClassDataRepository repository = ClassDataRepository.INSTANCE;
                List<String> classNamesInPackage = new ArrayList<>(repository.getClassNamesInPackage(packageName));

                if (!classNamesInPackage.isEmpty()) {
                    String classPath = repository.findClassPath(classNamesInPackage.get(0));
                    URL resourceURL = new File(classPath).toURI().toURL();
                    resourceURLs = new HashSet();
                    resourceURLs.add(resourceURL);
                }
            }

            for (URL resourceURL : resourceURLs) {
                URL classPathURL = resolveClassPathURL(resourceURL, packageResourceName);
                String classPath = classPathURL.getFile();
                Set<String> classNamesInClassPath = findClassNamesInClassPath(classPath, true);
                classNames.addAll(filterClassNames(classNamesInClassPath, packageName, recursive));
            }

            for (String className : classNames) {
                Class<?> class_ = requiredLoad ? loadClass(classLoader, className) : findLoadedClass(classLoader, className);
                if (class_ != null) {
                    classesSet.add(class_);
                }
            }

        } catch (IOException e) {

        }
        return unmodifiableSet(classesSet);
    }

    public Set<Class<?>> scan(ClassLoader classLoader, URL resourceInArchive, boolean requiredLoad,
                              Predicate<? super Class<?>>... classFilters) {
        File archiveFile = resolveArchiveFile(resourceInArchive);
        return scan(classLoader, archiveFile, requiredLoad, classFilters);
    }

    public Set<Class<?>> scan(ClassLoader classLoader, File archiveFile, boolean requiredLoad,
                              Predicate<? super Class<?>>... classFilters) {
        Set<String> classNames = findClassNamesInClassPath(archiveFile, true);
        Set<Class<?>> classesSet = new LinkedHashSet<>();
        for (String className : classNames) {
            Class<?> class_ = requiredLoad ? loadClass(classLoader, className) : findLoadedClass(classLoader, className);
            if (class_ != null) {
                classesSet.add(class_);
            }
        }
        return filterAll(classesSet, classFilters);
    }

    private Set<String> filterClassNames(Set<String> classNames, String packageName, boolean recursive) {
        PackageNameClassNameFilter packageNameClassNameFilter = new PackageNameClassNameFilter(packageName, recursive);
        Set<String> filterClassNames = new LinkedHashSet(filter(classNames, packageNameClassNameFilter));
        return filterClassNames;
    }

    private URL resolveClassPathURL(URL resourceURL, String packageResourceName) {
        String resource = resourceURL.toExternalForm();
        String classPath = substringBefore(resource, packageResourceName);
        return ofURL(classPath);
    }
}
