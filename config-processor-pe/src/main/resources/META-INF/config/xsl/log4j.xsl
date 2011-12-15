<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bcl="http://baxter-it.com/config/log" xmlns:conf="http://baxter-it.com/config"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs bcl conf" version="2.0">

    <xsl:import href="baxterxsl:log4j-base.xsl" />
    <xsl:import href="baxterxsl:repo-base.xsl" />

    <xsl:output indent="yes" />

    <xsl:template match="/">
        <xsl:variable name="root">
            <xsl:copy-of select="conf:configuration-source/conf:request" />
            <xsl:apply-templates select="conf:configuration-source/conf:request" mode="load-document-with-variants">
                <xsl:with-param name="prefix" select="'log'"/>
            </xsl:apply-templates>
        </xsl:variable>            
        <xsl:apply-templates select="$root/bcl:configuration" />
    </xsl:template>

    <xsl:template name="build-log-directory-name">
        <xsl:text>${pe.logs.dir}/</xsl:text>
    </xsl:template>

</xsl:stylesheet>
