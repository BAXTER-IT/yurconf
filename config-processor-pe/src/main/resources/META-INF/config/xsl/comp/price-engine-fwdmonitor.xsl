<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/admintool"
    xmlns:udpprice="http://baxter-it.com/config/pe/udpprice"
    xmlns:fwdm="http://baxter-it.com/config/pe/fwdmonitor"
    xmlns:c="http://baxter-it.com/config/component"
    xmlns:cp="http://baxter-it.com/config/pe/cp" xmlns:pedb="http://baxter-it.com/config/pe/db"
    xmlns="http://baxter-it.com/config/pe/properties"
    exclude-result-prefixes="xs t pedb comp udpprice c cp fwdm" version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:param name="configurationComponentId"/>

    <xsl:template match="t:price-engine-fwdmonitor" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'currencies'"/>
        </xsl:call-template>
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'comp/fwdmonitor'"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="t:price-engine-fwdmonitor" mode="component-specific">
        <xsl:param name="root"/>
        <xsl:apply-templates
            select="$root/cp:configuration[cp:*[c:component[@id=$configurationComponentId][cp:port]]]"
            mode="fwdmonitor-udp-ports"/>
        <xsl:apply-templates select="$root/fwdm:configuration/fwdm:ui" />
    </xsl:template>
    
    <xsl:template match="fwdm:ui">
        <group key="behavior">
            <entry key="popForwardAtWarning"><xsl:value-of select="fwdm:flag[@id='popForwardAtWarning']"/></entry>
            <entry key="popUpWithFocus"><xsl:value-of select="fwdm:flag[@id='popUpWithFocus']"/></entry>
        </group>
    </xsl:template>

    <xsl:template match="cp:configuration" mode="fwdmonitor-udp-ports">
        <group key="uDPPortForMon">
            <xsl:apply-templates select="cp:*/c:component[@id=$configurationComponentId]/cp:port"
                mode="fwdmonitor-udp-ports"/>
        </group>
    </xsl:template>

    <xsl:template match="cp:port" mode="fwdmonitor-udp-ports">
        <entry>
            <xsl:attribute name="key">
                <xsl:value-of select="../../@id"/>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </entry>
    </xsl:template>

</xsl:stylesheet>
