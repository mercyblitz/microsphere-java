package io.microsphere.classloading;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link ManifestArtifactResourceResolver} Test
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see ManifestArtifactResourceResolver
 * @since 1.0.0
 */
public class ManifestArtifactResourceResolverTest extends AbstractArtifactResourceResolverTest<ManifestArtifactResourceResolver> {

    @Override
    protected void assertArtifact(Artifact artifact) throws Throwable {
        assertEquals("FindBugs-jsr305", artifact.getArtifactId());
        assertEquals("3.0.2", artifact.getVersion());
    }
}