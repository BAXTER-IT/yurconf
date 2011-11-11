<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    exclude-result-prefixes="xs xd" version="2.0">
    
    <xsl:import href="inc/common-plain.xsl" />
    <xsl:import href="baxterxsl:log4j.xsl" />

    <xsl:output encoding="UTF-8" media-type="text/plain" omit-xml-declaration="yes"/>

    <xsl:param name="configurationProductId" select="'N/A'"/>

    <xsl:param name="configurationVersion" select="'N/A'"/>

    <xsl:param name="configurationComponentId" select="'N/A'"/>

    <xsl:param name="configurationVariant" select="'N/A'"/>

    <xsl:template match="/">
        <xsl:text>Hello!</xsl:text>
        <xsl:call-template name="CR"/>
        <xsl:text>You requested following configuration:</xsl:text>
        <xsl:call-template name="CR"/>
        <xsl:call-template name="CR"/>
        <xsl:text>ProductID: </xsl:text>
        <xsl:value-of select="$configurationProductId" />
        <xsl:call-template name="CR"/>
        <xsl:text>ComponentID: </xsl:text>
        <xsl:value-of select="$configurationComponentId" />
        <xsl:call-template name="CR"/>
        <xsl:text>Variant: </xsl:text>
        <xsl:value-of select="$configurationVariant" />
        <xsl:call-template name="CR"/>
        <xsl:text>Version: </xsl:text>
        <xsl:value-of select="$configurationVersion" />
    </xsl:template>
    
</xsl:stylesheet>
