<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl"
    exclude-result-prefixes="xs xd" version="2.0">

    <xsl:import href="baxterxsl:text-fmt.xsl"/>

    <xsl:output encoding="UTF-8" media-type="text/plain" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:text>Inputs:</xsl:text>
        <xsl:call-template name="CR"/>
        <xsl:call-template name="input-dump" />
        <xsl:call-template name="CR"/>
        <xsl:call-template name="CR"/>
        <xsl:apply-templates select="hello"/>
    </xsl:template>

    <xsl:template match="hello">
        <xsl:apply-templates select="*"/>
    </xsl:template>

    <xsl:template match="hello/*">
        <xsl:text>Hello </xsl:text>
        <xsl:value-of select="name()"/>
        <xsl:call-template name="CR"/>
    </xsl:template>

</xsl:stylesheet>
