<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:bcl="http://baxter-it.com/config/log"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs bcl" version="2.0">

    <xsl:import href="baxterxsl:logback-base.xsl" />
    <xsl:import href="baxterxsl:repo-base.xsl" />

    <xsl:output indent="yes" />

    <xsl:template match="/">
        <xsl:variable name="root">
            <xsl:call-template name="load-merged-repo-document">
                <xsl:with-param name="prefix" select="'log'"/>
            </xsl:call-template>
        </xsl:variable>            
        <xsl:apply-templates select="$root/bcl:configuration" />
    </xsl:template>

    <xsl:template name="build-log-directory-name">
        <xsl:text>${pe.logs.dir}/</xsl:text>
    </xsl:template>

</xsl:stylesheet>
