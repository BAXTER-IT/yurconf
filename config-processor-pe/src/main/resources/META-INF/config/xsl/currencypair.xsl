<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:pecp="http://baxter-it.com/config/pe/cp"
    xmlns:c="http://baxter-it.com/config/component"
    exclude-result-prefixes="xs pecp c"
    xmlns="http://baxter-it.com/config/pe/properties" version="2.0">

    <xsl:import href="imp/currency-base.xsl" />

     <xsl:template match="pecp:configuration">
         <xsl:apply-templates select="pecp:currencyPair/c:component[@id=$configurationComponentId]/pecp:port" />   
    </xsl:template>

</xsl:stylesheet>
