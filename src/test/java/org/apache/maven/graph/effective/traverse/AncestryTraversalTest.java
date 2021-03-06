package org.apache.maven.graph.effective.traverse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.maven.graph.common.ref.ArtifactRef;
import org.apache.maven.graph.common.ref.ProjectVersionRef;
import org.apache.maven.graph.common.version.InvalidVersionSpecificationException;
import org.apache.maven.graph.effective.EProjectGraph;
import org.apache.maven.graph.effective.EProjectRelationships;
import org.apache.maven.graph.effective.rel.DependencyRelationship;
import org.apache.maven.graph.effective.rel.ExtensionRelationship;
import org.apache.maven.graph.effective.rel.PluginRelationship;
import org.apache.maven.graph.effective.traverse.AncestryTraversal;
import org.junit.Test;

public class AncestryTraversalTest
{

    @Test
    public void traverseTwoAncestors()
        throws InvalidVersionSpecificationException
    {
        final EProjectGraph.Builder pgBuilder =
            new EProjectGraph.Builder( new ProjectVersionRef( "my.group", "my-artifact", "1.0" ) );
        final ProjectVersionRef parentRef = new ProjectVersionRef( "my.group", "my-dad", "1" );
        pgBuilder.withParent( parentRef );

        final ProjectVersionRef grandRef = new ProjectVersionRef( "other.group", "grandpa", "20120821" );
        final EProjectRelationships parentRels =
            new EProjectRelationships.Builder( parentRef ).withParent( grandRef )
                                                               .build();

        pgBuilder.withDirectProjectRelationships( parentRels );

        final EProjectGraph graph = pgBuilder.build();

        final AncestryTraversal ancestry = new AncestryTraversal( graph.getRoot() );
        graph.traverse( ancestry );

        final List<ProjectVersionRef> ancestorRefs = ancestry.getAncestry();

        assertThat( ancestorRefs.size(), equalTo( 3 ) );

        int idx = 0;
        ProjectVersionRef ref = ancestorRefs.get( idx++ );

        assertThat( ref.getGroupId(), equalTo( "my.group" ) );
        assertThat( ref.getArtifactId(), equalTo( "my-artifact" ) );

        ref = ancestorRefs.get( idx++ );

        assertThat( ref.getGroupId(), equalTo( "my.group" ) );
        assertThat( ref.getArtifactId(), equalTo( "my-dad" ) );

        ref = ancestorRefs.get( idx++ );

        assertThat( ref.getGroupId(), equalTo( "other.group" ) );
        assertThat( ref.getArtifactId(), equalTo( "grandpa" ) );

    }

    @Test
    public void traverseTwoAncestors_IgnoreNonParentRelationships()
        throws InvalidVersionSpecificationException
    {
        final ProjectVersionRef myRef = new ProjectVersionRef( "my.group", "my-artifact", "1.0" );

        final EProjectGraph.Builder pgBuilder = new EProjectGraph.Builder( myRef );

        final ProjectVersionRef parentRef = new ProjectVersionRef( "my.group", "my-dad", "1" );
        pgBuilder.withParent( parentRef );

        pgBuilder.withDependencies( new DependencyRelationship(
                                                                myRef,
                                                                new ArtifactRef(
                                                                                 new ProjectVersionRef( "some.group",
                                                                                                          "foo", "1.0" ),
                                                                                 null, null, false ), null, 0, false ),
                                    new DependencyRelationship( myRef,
                                                                new ArtifactRef( new ProjectVersionRef( "some.group",
                                                                                                          "bar",
                                                                                                          "1.2.1" ),
                                                                                 null, null, false ), null, 1, false ) );

        pgBuilder.withPlugins( new PluginRelationship( myRef,
                                                       new ProjectVersionRef( "org.apache.maven.plugins",
                                                                                "maven-compiler-plugin", "2.5.1" ), 0,
                                                       false ),
                               new PluginRelationship( myRef, new ProjectVersionRef( "org.apache.maven.plugins",
                                                                                       "maven-jar-plugin", "2.2" ), 1,
                                                       false ) );

        pgBuilder.withExtensions( new ExtensionRelationship(
                                                             myRef,
                                                             new ProjectVersionRef( "org.apache.maven.plugins",
                                                                                      "maven-compiler-plugin", "2.5.1" ),
                                                             0 ) );

        final ProjectVersionRef grandRef = new ProjectVersionRef( "other.group", "grandpa", "20120821" );
        final EProjectRelationships parentRels =
            new EProjectRelationships.Builder( parentRef ).withParent( grandRef )
                                                               .withDependencies( new DependencyRelationship(
                                                                                                              parentRef,
                                                                                                              new ArtifactRef(
                                                                                                                               new ProjectVersionRef(
                                                                                                                                                        "other.group",
                                                                                                                                                        "utils",
                                                                                                                                                        "3-1" ),
                                                                                                                               null,
                                                                                                                               null,
                                                                                                                               false ),
                                                                                                              null, 0,
                                                                                                              false ) )
                                                               .build();

        pgBuilder.withDirectProjectRelationships( parentRels );

        final EProjectGraph graph = pgBuilder.build();

        final AncestryTraversal ancestry = new AncestryTraversal( graph.getRoot() );
        graph.traverse( ancestry );

        final List<ProjectVersionRef> ancestorRefs = ancestry.getAncestry();

        assertThat( ancestorRefs.size(), equalTo( 3 ) );

        int idx = 0;
        ProjectVersionRef ref = ancestorRefs.get( idx++ );

        assertThat( ref.getGroupId(), equalTo( "my.group" ) );
        assertThat( ref.getArtifactId(), equalTo( "my-artifact" ) );

        ref = ancestorRefs.get( idx++ );

        assertThat( ref.getGroupId(), equalTo( "my.group" ) );
        assertThat( ref.getArtifactId(), equalTo( "my-dad" ) );

        ref = ancestorRefs.get( idx++ );

        assertThat( ref.getGroupId(), equalTo( "other.group" ) );
        assertThat( ref.getArtifactId(), equalTo( "grandpa" ) );

    }

}
