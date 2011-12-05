<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/blotter"
    xmlns:udpprice="http://baxter-it.com/config/pe/udpprice"
    xmlns:pedb="http://baxter-it.com/config/pe/db" xmlns="http://baxter-it.com/config/pe/properties"
    exclude-result-prefixes="xs t pedb comp udpprice" version="2.0">
    
    <xsl:import href="../imp/udp-price.xsl" />
    <xsl:import href="baxterxsl:repo-base.xsl"/>
    
    <xsl:template match="t:price-engine-blotter" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'broadcast'"/>
        </xsl:call-template>
        <xsl:call-template name="add-udp-price-config-to-root" />
    </xsl:template>

    <xsl:template match="t:price-engine-blotter" mode="component-specific">
        <xsl:param name="root" />
        <xsl:apply-templates select="$root/udpprice:configuration" />
    </xsl:template>

</xsl:stylesheet>
