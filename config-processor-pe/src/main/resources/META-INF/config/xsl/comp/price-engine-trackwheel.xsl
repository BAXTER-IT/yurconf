<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/trackwheel"
    xmlns="http://baxter-it.com/config/pe/properties" xmlns:cp="http://baxter-it.com/config/pe/cp"
    xmlns:c="http://baxter-it.com/config/component" exclude-result-prefixes="xs t  comp   cp c"
    version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:param name="configurationVariant"/>
    <xsl:param name="configurationProductId"/>
    <xsl:param name="configurationVersion"/>
    <xsl:param name="configurationComponentId"/>

    <xsl:template match="t:price-engine-trackwheel" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'comp/trackwheel'"/>
        </xsl:call-template>
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'currencies'"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="t:price-engine-trackwheel" mode="component-specific">
        <xsl:param name="root"/>
        <xsl:apply-templates select="$root/cp:configuration" mode="TW-UDBPorts"/>
        <entry key="soundState">
            <xsl:value-of select="$root/comp:configuration/comp:ui/comp:soundState"/>
        </entry>
        <entry key="type">
            <xsl:value-of select="$root/comp:configuration/comp:ui/comp:type"/>
        </entry>
        <group key="colors">
            <xsl:apply-templates select="$root/comp:configuration/comp:ui/comp:color"/>
        </group>
    </xsl:template>

    <xsl:template match="comp:color">
        <group>
            <xsl:attribute name="key">
                <xsl:value-of select="@id"/>
            </xsl:attribute>
            <entry key="red">
                <xsl:value-of select="@red"/>
            </entry>
            <entry key="green">
                <xsl:value-of select="@green"/>
            </entry>
            <entry key="blue">
                <xsl:value-of select="@blue"/>
            </entry>
        </group>
    </xsl:template>

    <xsl:template match="cp:configuration" mode="TW-UDBPorts">
        <group key="uDBPort">
            <xsl:apply-templates
                select="cp:currencyPair/c:component[@id=$configurationComponentId]/cp:port"
                mode="TW-UDBPorts"/>
        </group>
    </xsl:template>

    <xsl:template match="cp:port" mode="TW-UDBPorts">
        <entry>
            <xsl:attribute name="key">
                <xsl:value-of select="../../@id"/>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </entry>
    </xsl:template>

</xsl:stylesheet>
