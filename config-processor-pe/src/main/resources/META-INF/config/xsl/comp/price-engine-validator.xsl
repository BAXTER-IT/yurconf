<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/validator"
    xmlns="http://baxter-it.com/config/pe/properties"
    xmlns:c="http://baxter-it.com/config/component" exclude-result-prefixes="xs t  comp  c"
    version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:param name="configurationVariant"/>
    <xsl:param name="configurationProductId"/>
    <xsl:param name="configurationVersion"/>
    <xsl:param name="configurationComponentId"/>

    <xsl:template match="t:price-engine-validator" mode="component-specific-sources"> </xsl:template>

    <xsl:template match="t:price-engine-validator" mode="component-specific">
        <xsl:param name="root"/>
        <xsl:if test="$root/request/parameter[@id='epp']/text() = 'true'">
            <entry key="validatorForEpp">true</entry>
        </xsl:if>
    </xsl:template>


</xsl:stylesheet>
