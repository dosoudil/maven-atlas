package org.apache.maven.graph.effective;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Set;

import org.apache.maven.graph.common.DependencyScope;
import org.apache.maven.graph.common.ref.ArtifactRef;
import org.apache.maven.graph.common.ref.ProjectVersionRef;
import org.apache.maven.graph.common.version.InvalidVersionSpecificationException;
import org.apache.maven.graph.effective.rel.DependencyRelationship;
import org.apache.maven.graph.effective.rel.ExtensionRelationship;
import org.apache.maven.graph.effective.rel.ParentRelationship;
import org.apache.maven.graph.effective.rel.PluginRelationship;
import org.apache.maven.graph.effective.rel.ProjectRelationship;
import org.junit.Test;

public class EProjectRelationshipsTest
{

    @Test
    public void builderWith2Dependencies2PluginsAParentAndAnExtension()
        throws InvalidVersionSpecificationException
    {
        final ProjectVersionRef p = new ProjectVersionRef( "org.apache.maven", "maven-core", "3.0.3" );

        final EProjectRelationships.Builder prb = new EProjectRelationships.Builder( p );

        final ProjectVersionRef parent = new ProjectVersionRef( "org.apache.maven", "maven", "3.0.3" );
        final ParentRelationship parentRel = new ParentRelationship( p, parent );

        int idx = 0;
        int pidx = 0;
        final DependencyRelationship papi =
            new DependencyRelationship( p, new ArtifactRef( "org.apache.maven", "maven-plugin-api", "3.0.3", null,
                                                            null, false ), DependencyScope.compile, idx++, false );
        final DependencyRelationship art =
            new DependencyRelationship( p, new ArtifactRef( "org.apache.maven", "maven-artifact", "3.0.3", null, null,
                                                            false ), DependencyScope.compile, idx++, false );
        final PluginRelationship jarp =
            new PluginRelationship( p, new ProjectVersionRef( "org.apache.maven.plugins", "maven-jar-plugin", "2.2" ),
                                    pidx++, false );
        final PluginRelationship comp =
            new PluginRelationship( p, new ProjectVersionRef( "org.apache.maven.plugins", "maven-compiler-plugin",
                                                              "2.3.2" ), pidx++, false );
        final ExtensionRelationship wag =
            new ExtensionRelationship( p, new ProjectVersionRef( "org.apache.maven.wagon", "wagon-provider-webdav",
                                                                 "1.0" ), 0 );

        prb.withParent( parentRel );
        prb.withDependencies( papi, art );
        prb.withPlugins( jarp, comp );
        prb.withExtensions( wag );

        final EProjectRelationships rels = prb.build();

        final Set<ProjectRelationship<?>> all = rels.getAll();

        assertThat( all.size(), equalTo( 6 ) );

        assertThat( all.contains( parentRel ), equalTo( true ) );
        assertThat( all.contains( papi ), equalTo( true ) );
        assertThat( all.contains( art ), equalTo( true ) );
        assertThat( all.contains( jarp ), equalTo( true ) );
        assertThat( all.contains( comp ), equalTo( true ) );
        assertThat( all.contains( wag ), equalTo( true ) );
    }

}
