<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:curr="http://baxter-it.com/price-engine/conf/currencies" xmlns:c="http://baxter-it.com/config/component"
    exclude-result-prefixes="xs curr c" version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl" />
    <xsl:import href="baxterxsl:text-fmt.xsl" />

    <xsl:output method="text" encoding="UTF-8" omit-xml-declaration="yes" />

    <xsl:template match="/">
        <xsl:variable name="root">
            <xsl:copy-of select="configuration-source/request" />
            <xsl:call-template name="load-merged-repo-document">
                <xsl:with-param name="prefix" select="'currencies'" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:apply-templates select="$root/curr:configuration" />
    </xsl:template>

    <xsl:template
        match="curr:configuration[$configurationComponentId='price-engine-validator'][/request/parameter[@id='epp']/text() = 'true']">
        <xsl:apply-templates select="curr:currencyPair/c:component[@id=$configurationComponentId]/curr:port[@id='epp']"
         />
    </xsl:template>

    <xsl:template match="curr:configuration">
        <xsl:apply-templates select="curr:*/c:component[@id=$configurationComponentId]/curr:port[@id='default']" />
    </xsl:template>

    <xsl:template match="curr:port">
        <xsl:value-of select="../../@id" />
        <xsl:text> </xsl:text>
        <xsl:value-of select="." />
        <xsl:if test="position() != last()">
            <xsl:call-template name="CRLF" />
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
