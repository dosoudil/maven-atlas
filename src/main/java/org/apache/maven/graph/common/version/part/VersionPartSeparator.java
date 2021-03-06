package org.apache.maven.graph.common.version.part;

public enum VersionPartSeparator
{

    BLANK( "" ), DASH( "-" ), UNDERSCORE( "_" ), DOT( "." );

    private String rendered;

    private VersionPartSeparator( final String rendered )
    {
        this.rendered = rendered;
    }

    public String getRenderedString()
    {
        return rendered;
    }

}
