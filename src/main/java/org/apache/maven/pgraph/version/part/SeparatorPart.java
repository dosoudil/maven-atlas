package org.apache.maven.pgraph.version.part;

public class SeparatorPart
    extends VersionPart
{

    private final VersionPartSeparator type;

    public SeparatorPart( final VersionPartSeparator type )
    {
        this.type = type;
    }

    @Override
    public String renderStandard()
    {
        return type.getRenderedString();
    }

    public VersionPartSeparator getValue()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return String.format( "SEP[%s]", type.getRenderedString() );
    }

    public int compareTo( final VersionPart o )
    {
        if ( o instanceof SeparatorPart )
        {
            return 0;
        }

        // TODO: punting...shouldn't happen, but not sure what the "right" thing is.
        return 0;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
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
        final SeparatorPart other = (SeparatorPart) obj;
        if ( type != other.type )
        {
            return false;
        }
        return true;
    }

}