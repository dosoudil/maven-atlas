package org.apache.maven.graph.common.version.part;

import java.util.Date;

public class SnapshotPart
    extends VersionPart
{

    private final Date timestamp;

    private final Integer buildNumber;

    private final String literal;

    // TODO: Parse into date and buildnumber for comparison
    public SnapshotPart( final Date timestamp, final int buildNumber, final String literal )
    {
        this.timestamp = timestamp;
        this.buildNumber = buildNumber;
        this.literal = literal;
    }

    public SnapshotPart( final String literal )
    {
        timestamp = null;
        buildNumber = null;
        this.literal = literal;
    }

    public String getLiteral()
    {
        return literal;
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public int getBuildNumber()
    {
        return buildNumber;
    }

    public boolean isLocalSnapshot()
    {
        return timestamp == null;
    }

    @Override
    public String toString()
    {
        return "SNAP[" + ( timestamp == null ? "local" : "remote;" + renderStandard() ) + "]";
    }

    @Override
    public String renderStandard()
    {
        return literal;
    }

    public String getValue()
    {
        return renderStandard();
    }

    public int compareTo( final VersionPart o )
    {
        if ( o instanceof SnapshotPart )
        {
            final SnapshotPart oSnap = (SnapshotPart) o;
            if ( !isLocalSnapshot() && !oSnap.isLocalSnapshot() )
            {
                final int comp = getTimestamp().compareTo( oSnap.getTimestamp() );
                if ( comp == 0 )
                {
                    return getBuildNumber() - oSnap.getBuildNumber();
                }
            }
            else if ( isLocalSnapshot() && !oSnap.isLocalSnapshot() )
            {
                return -1;
            }
            else if ( !isLocalSnapshot() && oSnap.isLocalSnapshot() )
            {
                return 1;
            }

            return 0;
        }
        else if ( o instanceof StringPart )
        {
            return -1 * o.compareTo( this );
        }
        else
        {
            return -1;
        }
    }

    public boolean isPaddingPrepended()
    {
        return true;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( buildNumber == null ) ? 0 : buildNumber.hashCode() );
        result = prime * result + ( ( timestamp == null ) ? 0 : timestamp.hashCode() );
        return result;
    }

    @Override
    public boolean equals( final Object obj )
    {
        if ( this == obj )
        {
            return true;
        }
        if ( obj == null )
        {
            return false;
        }
        if ( getClass() != obj.getClass() )
        {
            return false;
        }
        final SnapshotPart other = (SnapshotPart) obj;
        if ( buildNumber == null )
        {
            if ( other.buildNumber != null )
            {
                return false;
            }
        }
        else if ( !buildNumber.equals( other.buildNumber ) )
        {
            return false;
        }
        if ( timestamp == null )
        {
            if ( other.timestamp != null )
            {
                return false;
            }
        }
        else if ( !timestamp.equals( other.timestamp ) )
        {
            return false;
        }
        return true;
    }

}
