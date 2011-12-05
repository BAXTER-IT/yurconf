<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:pecp="http://baxter-it.com/config/pe/cp"
    exclude-result-prefixes="xs pecp"
    xmlns="http://baxter-it.com/config/pe/properties" version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl"/>
    <xsl:import href="baxterxsl:text-fmt.xsl" />
        
    <xsl:output method="text" encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:variable name="currencies">
            <xsl:call-template name="load-merged-repo-document">
                <xsl:with-param name="prefix" select="'currencies'"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:apply-templates select="$currencies/pecp:configuration"/>
    </xsl:template>
    
    <xsl:template match="pecp:port">
      <xsl:value-of select="../../@id" />
      <xsl:text> </xsl:text>
      <xsl:value-of select="." />
      <xsl:if test="position() != last()">
        <xsl:call-template name="CRLF" />
      </xsl:if>
    </xsl:template>

</xsl:stylesheet>
