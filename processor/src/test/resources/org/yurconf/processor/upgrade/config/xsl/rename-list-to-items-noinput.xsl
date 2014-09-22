<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0" xmlns:conf="http://yurconf.org">

    <!--
        must load predefined document structure1.xml and convert it and save result to same file.
    -->

    <!-- xsl:import href="yurconf:repo-base.xsl"/ -->
    <!-- xsl:import href="yurconf:text-fmt.xsl"/ -->

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:output name="resultXml" method="xml" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/">
        <xsl:variable name="structure1" select="doc('yurconf:///structure1.xml')"/>
        <xsl:apply-templates select="$structure1/list"/>
    </xsl:template>

    <xsl:template match="conf:sources">
    </xsl:template>

    <xsl:template match="list">
        <xsl:result-document href="yurconf:///structure1.xml" format="resultXml">
            <items>
                <xsl:copy-of select="*"/>
            </items>
        </xsl:result-document>
    </xsl:template>

</xsl:stylesheet>
