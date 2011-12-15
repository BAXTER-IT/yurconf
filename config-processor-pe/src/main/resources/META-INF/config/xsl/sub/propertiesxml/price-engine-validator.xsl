<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:cidt="http://baxter-it.com/config/component/id-template" xmlns:conf="http://baxter-it.com/config"
    xmlns="http://baxter-it.com/price-engine/conf/properties" exclude-result-prefixes="xs cidt conf" version="2.0">

    <xsl:template match="cidt:price-engine-validator">
        <xsl:if test="/conf:request/conf:parameter[@id='epp']/text() = 'true'">
            <entry key="validatorForEpp">true</entry>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
