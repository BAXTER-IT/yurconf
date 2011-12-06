<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/tradingtool"
    xmlns="http://baxter-it.com/config/pe/properties"
    xmlns:cp="http://baxter-it.com/config/pe/cp" xmlns:c="http://baxter-it.com/config/component"
    exclude-result-prefixes="xs t comp cp c" version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:param name="configurationVariant"/>
    <xsl:param name="configurationProductId"/>
    <xsl:param name="configurationVersion"/>
    <xsl:param name="configurationComponentId"/>

    <xsl:template match="t:trading-tool" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'currencies'"/>
        </xsl:call-template>
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'comp/tradingtool'"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="t:trading-tool" mode="component-specific">
        <xsl:param name="root"/>
        <xsl:apply-templates select="$root/comp:configuration/comp:servlet" />
        <entry key="priceRefreshTime"><xsl:value-of select="$root/comp:configuration/comp:priceRefreshTime"/></entry>
        <xsl:if test="$root/cp:configuration/cp:currencyPair6[c:component[@id=$configurationComponentId]]">
            <group key="userData">
                <entry key="cps">
                    <xsl:apply-templates mode="userData"
                        select="$root/cp:configuration/cp:currencyPair6[c:component[@id=$configurationComponentId]]"
                    />
                </entry>
            </group>
        </xsl:if>        
    </xsl:template>
    
    <xsl:template match="comp:servlet">
        <entry key="ServletUrl" ><xsl:value-of select="@url"/></entry>
        <entry key="ServletUrl2" ><xsl:value-of select="@url2"/></entry>
    </xsl:template>

    <xsl:template match="cp:currencyPair6" mode="userData">
        <xsl:value-of select="@id"/>
        <xsl:if test="position() != last()">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>
    
</xsl:stylesheet>
