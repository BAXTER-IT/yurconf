<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns="http://baxter-it.com/config/pe/properties"
    exclude-result-prefixes="xs" version="2.0">

    <xsl:param name="configurationVariant"/>
    <xsl:param name="configurationProductId"/>
    <xsl:param name="configurationVersion"/>
    <xsl:param name="configurationComponentId"/>

    <xsl:template name="config-reference">
        <xsl:param name="refName"/>
        <xsl:param name="refVariant" select="$configurationVariant"/>
        <xsl:param name="refProductId" select="$configurationProductId"/>
        <xsl:param name="refType"/>
        <xsl:param name="refComponentId"/>
        <group>
            <xsl:attribute name="key">
                <xsl:value-of select="$refName"/>
            </xsl:attribute>
            <xsl:if test="$refVariant">
                <entry key="variant">
                    <xsl:value-of select="$refVariant"/>
                </entry>
            </xsl:if>
            <entry key="type">
                <xsl:value-of select="$refType"/>
            </entry>
            <entry key="componentId">
                <xsl:value-of select="$refComponentId"/>
            </entry>
            <xsl:if test="$refProductId">
                <entry key="productId">
                    <xsl:value-of select="$refProductId"/>
                </entry>
            </xsl:if>
        </group>
    </xsl:template>


</xsl:stylesheet>
