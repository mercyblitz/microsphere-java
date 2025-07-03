package io.microsphere.classloading;

import io.microsphere.annotation.Nullable;
import io.microsphere.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static io.microsphere.constants.SymbolConstants.COLON;
import static io.microsphere.logging.LoggerFactory.getLogger;
import static io.microsphere.util.ClassLoaderUtils.removeClassPathURL;
import static io.microsphere.util.StringUtils.isBlank;
import static io.microsphere.util.StringUtils.split;
import static io.microsphere.util.SystemUtils.FILE_ENCODING;

/**
 * The executor for the banned artifacts that are loading by {@link ClassLoader}.
 * <p>
 * The banned list should be defined at the config resource that locates on the "META-INF/banned-artifacts" was loaded by
 * {@link ClassLoader}.
 * <p>
 * The config resource format :
 * <pre>${groupId}:${artifactId}:${version}</pre>
 *
 * <ul>
 *     <li>groupId  :  Artifact Maven groupId</li>
 *     <li>artifactId : Artifact Maven artifactId</li>
 *     <li>version : Artifact Maven version</li>
 * </ul>
 *
 * <h3>Example Usage</h3>
 * <pre>{@code
 * // Create a default instance and execute to remove banned artifacts from classpath
 * BannedArtifactClassLoadingExecutor executor = new BannedArtifactClassLoadingExecutor();
 * executor.execute();
 * }</pre>
 *
 * <p>This will load all the banned artifact definitions from "META-INF/banned-artifacts"
 * resources in the current classloader, detect all non-JDK artifacts in the classpath,
 * and remove URLs corresponding to any matched banned artifacts.</p>
 *
 * <p>You can also provide a custom {@link ClassLoader} if you want to inspect a specific one:</p>
 *
 * <pre>{@code
 * ClassLoader customClassLoader = ...; // your custom class loader
 * BannedArtifactClassLoadingExecutor executor = new BannedArtifactClassLoadingExecutor(customClassLoader);
 * executor.execute();
 * }</pre>
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy<a/>
 * @see ArtifactDetector
 * @see MavenArtifact
 * @see Artifact
 * @since 1.0.0
 */
public class BannedArtifactClassLoadingExecutor {

    public static final String CONFIG_LOCATION = "META-INF/banned-artifacts";

    private static final Logger logger = getLogger(BannedArtifactClassLoadingExecutor.class);

    private static final String ENCODING = FILE_ENCODING;

    private final ArtifactDetector artifactDetector;

    private final ClassLoader classLoader;

    public BannedArtifactClassLoadingExecutor() {
        this(null);
    }

    public BannedArtifactClassLoadingExecutor(@Nullable ClassLoader classLoader) {
        this.artifactDetector = new ArtifactDetector(classLoader);
        this.classLoader = this.artifactDetector.classLoader;
    }

    public void execute() {
        List<MavenArtifact> bannedArtifactConfigs = loadBannedArtifactConfigs();
        List<Artifact> artifacts = artifactDetector.detect(false);
        for (Artifact artifact : artifacts) {
            URL classPathURL = artifact.getLocation();
            if (classPathURL != null) {
                for (MavenArtifact bannedArtifactConfig : bannedArtifactConfigs) {
                    if (bannedArtifactConfig.matches(artifact)) {
                        removeClassPathURL(classLoader, classPathURL);
                    }
                }
            }
        }
    }

    private List<MavenArtifact> loadBannedArtifactConfigs() {
        List<MavenArtifact> bannedArtifactConfigs = new LinkedList<>();
        try {
            Enumeration<URL> configResources = classLoader.getResources(CONFIG_LOCATION);
            while (configResources.hasMoreElements()) {
                URL configResource = configResources.nextElement();
                List<MavenArtifact> configs = loadBannedArtifactConfigs(configResource);
                bannedArtifactConfigs.addAll(configs);
            }
        } catch (IOException e) {
            logger.error("The banned artifacts config resource[{}] can't be read", CONFIG_LOCATION, e);
        }
        return bannedArtifactConfigs;
    }

    private List<MavenArtifact> loadBannedArtifactConfigs(URL configResource) throws IOException {
        List<MavenArtifact> bannedArtifactConfigs = new LinkedList<>();
        try (InputStream inputStream = configResource.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING))
        ) {
            while (true) {
                String definition = reader.readLine();
                if (isBlank(definition)) {
                    break;
                }
                MavenArtifact bannedArtifactConfig = loadBannedArtifactConfig(definition);
                bannedArtifactConfigs.add(bannedArtifactConfig);
            }
        }
        return bannedArtifactConfigs;
    }

    /**
     * @param definition
     * @return
     */
    private MavenArtifact loadBannedArtifactConfig(String definition) {
        String[] gav = split(definition.trim(), COLON);
        if (gav.length != 3) {
            throw new RuntimeException("The definition of the banned artifact must contain groupId, artifactId and version : " + definition);
        }
        String groupId = gav[0];
        String artifactId = gav[1];
        String version = gav[2];
        return MavenArtifact.create(groupId, artifactId, version);
    }

}
