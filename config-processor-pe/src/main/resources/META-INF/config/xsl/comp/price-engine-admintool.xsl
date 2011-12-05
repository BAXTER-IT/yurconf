<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/admintool"
    xmlns:udpprice="http://baxter-it.com/config/pe/udpprice"
    xmlns:pedb="http://baxter-it.com/config/pe/db" xmlns="http://baxter-it.com/config/pe/properties"
    exclude-result-prefixes="xs t pedb comp udpprice" version="2.0">

    <xsl:import href="../imp/udp-price.xsl"/>
    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:template match="t:price-engine-admintool" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'comp/admintool'"/>
        </xsl:call-template>
        <xsl:call-template name="add-udp-price-config-to-root"/>
    </xsl:template>

    <xsl:template match="t:price-engine-admintool" mode="component-specific">
        <xsl:param name="root"/>
        <xsl:apply-templates select="$root/comp:configuration/*"/>
        <xsl:apply-templates select="$root/udpprice:configuration"/>
    </xsl:template>

    <xsl:template match="comp:ip">
        <xsl:text>"</xsl:text>
        <xsl:apply-templates/>
        <xsl:text>"</xsl:text>
        <xsl:if test="position() != last()">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template match="comp:snmp">
        <group key="snmp">
            <entry key="StorageWarningLevel">
                <xsl:value-of select="@storageWarningLevel"/>
            </entry>
            <entry key="Interval">
                <xsl:value-of select="@interval"/>
            </entry>
            <entry key="IPsComponents">
                <xsl:apply-templates select="comp:components/comp:ip"/>
            </entry>
            <entry key="IPsHardwares">
                <xsl:apply-templates select="comp:hardwares/comp:ip"/>
            </entry>
        </group>
    </xsl:template>

</xsl:stylesheet>
