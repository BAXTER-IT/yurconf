<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">

    <!-- 
        must load predefined document structure1.xml and convert it and save result to same file.
    -->

    <xsl:import href="baxterxsl:repo-base.xsl"/>
    <xsl:import href="baxterxsl:text-fmt.xsl"/>

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    <xsl:output name="resultXml" method="xml" encoding="UTF-8" indent="yes"/>
    
    <xsl:template match="/">
        <xsl:variable name="structure1" select="doc('baxterrepo:structure1.xml')"/>
        <xsl:apply-templates select="$structure1/list"/>
    </xsl:template>

    <xsl:template match="list">
        <xsl:result-document href="baxterrepo:structure1.xml" format="resultXml">
            <items>
                <xsl:copy-of select="*"/>
            </items>
        </xsl:result-document>
    </xsl:template>

</xsl:stylesheet>
