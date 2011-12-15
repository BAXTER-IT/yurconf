<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:jms="http://baxter-it.com/price-engine/conf/jms" xmlns:conf="http://baxter-it.com/config"
    xmlns:gprop="http://baxter-it.com/price-engine/conf/generic-properties"
    xmlns:c="http://baxter-it.com/config/component" exclude-result-prefixes="xs jms" version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl" />
    <xsl:import href="baxterxsl:text-fmt.xsl" />

    <xsl:output method="text" encoding="UTF-8" omit-xml-declaration="yes" />

    <xsl:variable name="cid-request">dcpk-request</xsl:variable>
    <xsl:variable name="cid-receiver">dcpk-receiver</xsl:variable>

    <xsl:template match="/">
        <xsl:variable name="root">
            <xsl:copy-of select="conf:configuration-source/conf:request" />
            <xsl:apply-templates select="conf:configuration-source/conf:request" mode="load-document-with-variants">
                <xsl:with-param name="prefix" select="'properties'" />
            </xsl:apply-templates>
            <xsl:apply-templates select="conf:configuration-source/conf:request" mode="load-document-with-variants">
                <xsl:with-param name="prefix" select="'jms'" />
            </xsl:apply-templates>
        </xsl:variable>
        <xsl:apply-templates select="$root/gprop:configuration/gprop:property[c:component[@id=$cid-request]]" />
        <xsl:apply-templates select="$root/jms:configuration/jms:authentication[@id='global']" />
        <xsl:apply-templates select="$root/jms:configuration" mode="num-dcpk" />
        <xsl:apply-templates select="$root/jms:configuration/jms:queue[c:component[@id=$cid-receiver]]" mode="receiver" />
        <xsl:apply-templates select="$root/jms:configuration/jms:queue[c:component[@id=$cid-request]]" mode="request" />
    </xsl:template>

    <xsl:template match="jms:configuration" mode="num-dcpk">
        <xsl:text>NUM_DCPK_RECEIVERS=</xsl:text>
        <xsl:value-of select="count(jms:queue[c:component[@id=$cid-receiver]])" />
        <xsl:call-template name="CRLF" />
    </xsl:template>

    <xsl:template name="channel">
        <xsl:apply-templates select="../jms:node[@id=current()/@node]" />
        <xsl:text>,</xsl:text>
        <xsl:choose>
            <xsl:when test="@node2">
                <xsl:apply-templates select="../jms:node[@id=current()/@node2]" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:text>null,0</xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:text>,</xsl:text>
        <xsl:value-of select="@name" />
        <xsl:text>;</xsl:text>
        <xsl:value-of select="../jms:node[@id=current()/@node]/@router" />
        <xsl:text>,</xsl:text>
        <xsl:apply-templates select="../jms:authentication[@id=current()/@authentication]" mode="channel" />
    </xsl:template>

    <xsl:template match="jms:node">
        <xsl:value-of select="@host" />
        <xsl:text>,</xsl:text>
        <xsl:value-of select="@port" />
    </xsl:template>

    <xsl:template match="jms:queue" mode="request">
        <xsl:text>DCPK_REQUEST=</xsl:text>
        <xsl:call-template name="channel" />
        <xsl:call-template name="CRLF" />
    </xsl:template>

    <xsl:template match="jms:queue" mode="receiver">
        <xsl:text>STP_QUEUE</xsl:text>
        <xsl:value-of select="position()" />
        <xsl:text>=</xsl:text>
        <xsl:call-template name="channel" />
        <xsl:call-template name="CRLF" />
    </xsl:template>

    <xsl:template match="gprop:property">
        <xsl:value-of select="@id" />
        <xsl:text>=</xsl:text>
        <xsl:value-of select="@value" />
        <xsl:call-template name="CRLF" />
    </xsl:template>

    <xsl:template match="jms:authentication" mode="channel">
        <xsl:value-of select="@username" />
        <xsl:text>,</xsl:text>
        <xsl:value-of select="@password" />
    </xsl:template>

    <xsl:template match="jms:authentication">
        <xsl:text>GlobalJMSUserName=</xsl:text>
        <xsl:value-of select="@username" />
        <xsl:call-template name="CRLF" />
        <xsl:text>GlobalJMSPassword=</xsl:text>
        <xsl:value-of select="@password" />
        <xsl:call-template name="CRLF" />
    </xsl:template>

</xsl:stylesheet>
