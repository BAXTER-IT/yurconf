<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:conf="http://baxter-it.com/config" exclude-result-prefixes="xs conf" version="2.0">

    <xsl:param name="configurationProductId" />
    <xsl:param name="configurationVersion" />
    <xsl:param name="configurationComponentId" />

    <!--
        Renders the configuration URL for current reference element.
        Note: the original "request" element shall be available at current context's root. 
    -->
    <xsl:template match="conf:reference" mode="url">
        <!-- first render the base URL (path to rest servlet) -->
        <xsl:value-of select="/conf:request/@base" />
        <xsl:text>/</xsl:text>
        <!-- now put the productId -->
        <xsl:choose>
            <xsl:when test="@productId">
                <xsl:value-of select="@productId" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$configurationProductId" />
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>/</xsl:text>
        <!-- now put the componentId -->
        <xsl:choose>
            <xsl:when test="@componentId">
                <xsl:value-of select="@componentId" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$configurationComponentId" />
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>/</xsl:text>
        <!-- Is there any variant? -->
        <xsl:choose>
            <xsl:when test="conf:variant">
                <xsl:value-of select="conf:variant/@id" separator="," />
                <xsl:text>/</xsl:text>
            </xsl:when>
            <xsl:when test="/conf:request/conf:variant">
                <xsl:value-of select="/conf:request/conf:variant/@id" separator="," />
                <xsl:text>/</xsl:text>
            </xsl:when>
        </xsl:choose>
        <!-- config type. Mandatory parameter -->
        <xsl:value-of select="@type" />
        <!-- Anyway render a version -->
        <xsl:text>?version=</xsl:text>
        <xsl:value-of select="$configurationVersion" />
        <!-- Add config parameters -->
        <xsl:for-each select="conf:param">
            <xsl:text disable-output-escaping="yes">&amp;</xsl:text>
            <xsl:value-of select="@name" />
            <xsl:text>=</xsl:text>
            <xsl:value-of select="encode-for-uri(text())" />
        </xsl:for-each>
    </xsl:template>


</xsl:stylesheet>
