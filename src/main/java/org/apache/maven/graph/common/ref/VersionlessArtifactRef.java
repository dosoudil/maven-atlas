package org.apache.maven.graph.common.ref;

import org.apache.maven.graph.common.version.InvalidVersionSpecificationException;
import org.apache.maven.graph.common.version.SingleVersion;
import org.apache.maven.graph.common.version.part.NumericPart;

/**
 * Special implementation of {@link ArtifactRef} that forces all versions to ZERO, to allow calculation of transitive
 * dependency graphs, where version collisions of the same project are likely.
 * 
 * @author jdcasey
 */
public class VersionlessArtifactRef
    extends ArtifactRef
{

    private static SingleVersion DUMMY_VERSION;

    static
    {
        try
        {
            DUMMY_VERSION = new SingleVersion( "1", new NumericPart( 1 ) );
        }
        catch ( final InvalidVersionSpecificationException e )
        {
            // TODO: What do I do with this? It should NEVER happen.
        }
    }

    private ArtifactRef realRef;

    public VersionlessArtifactRef( final ArtifactRef ref )
    {
        super( new ProjectVersionRef( ref.getGroupId(), ref.getArtifactId(), DUMMY_VERSION ), ref.getType(),
               ref.getClassifier(), ref.isOptional() );

        this.realRef = ref;
    }

    public void replaceRealRef( final ArtifactRef ref )
    {
        if ( realRef.versionlessEquals( ref ) )
        {
            this.realRef = ref;
        }
    }

    public ArtifactRef getRealRef()
    {
        return realRef;
    }

    /**
     * Leave out the version!
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( getArtifactId() == null ) ? 0 : getArtifactId().hashCode() );
        result = prime * result + ( ( getGroupId() == null ) ? 0 : getGroupId().hashCode() );
        result = prime * result + ( ( getClassifier() == null ) ? 0 : getClassifier().hashCode() );
        result = prime * result + ( ( getType() == null ) ? 0 : getType().hashCode() );
        result = prime * result + Boolean.valueOf( isOptional() )
                                         .hashCode();
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( getClass() != obj.getClass() )
        {
            return false;
        }

        final ArtifactRef other = (ArtifactRef) obj;
        return versionlessEquals( other );
    }

    @Override
    public String toString()
    {
        return String.format( "%s:%s:*:%s%s", getGroupId(), getArtifactId(), getType(), ( getClassifier() == null ? ""
                        : ":" + getClassifier() ) );
    }
}
