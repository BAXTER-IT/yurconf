<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bcl="http://baxter-it.com/config/log"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs bcl" version="2.0">

    <xsl:import href="baxterxsl:log4j-base.xsl" />
    <xsl:import href="baxterxsl:repo-base.xsl" />

    <xsl:output indent="yes" />

    <xsl:param name="configurationComponentId" />
    <xsl:param name="configurationVariant" />

    <xsl:template match="/">
        <xsl:variable name="xmlDocLocation">
            <xsl:call-template name="document-by-component-repo-location">
                <xsl:with-param name="prefix" select="'log-'" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="xmlVariantLocation">
            <xsl:call-template name="variant-by-component-repo-location">
                <xsl:with-param name="prefix" select="'log-'" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="configuration">
            <xsl:call-template name="merge-document-with-variant">
                <xsl:with-param name="xmlLocation" select="$xmlDocLocation" />
                <xsl:with-param name="xmlVariantLocation" select="$xmlVariantLocation" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:apply-templates select="$configuration/bcl:configuration" />
    </xsl:template>

    <xsl:template name="build-log-directory-name">
        <xsl:text>${pe.logs.dir}/</xsl:text>
    </xsl:template>

</xsl:stylesheet>
