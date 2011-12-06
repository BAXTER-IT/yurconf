<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/blotter"
    xmlns:udpprice="http://baxter-it.com/config/pe/udpprice"
    xmlns:broadcast="http://baxter-it.com/config/pe/broadcast"
    xmlns:pedb="http://baxter-it.com/config/pe/db" xmlns="http://baxter-it.com/config/pe/properties"
    xmlns:cp="http://baxter-it.com/config/pe/cp" xmlns:c="http://baxter-it.com/config/component"
    exclude-result-prefixes="xs t pedb comp udpprice broadcast cp c" version="2.0">

    <xsl:import href="../imp/udp-price.xsl"/>
    <xsl:import href="../imp/config-ref.xsl"/>
    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:param name="configurationVariant"/>
    <xsl:param name="configurationProductId"/>
    <xsl:param name="configurationVersion"/>
    <xsl:param name="configurationComponentId"/>

    <xsl:template match="t:price-engine-blotter" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'broadcast'"/>
        </xsl:call-template>
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'comp/blotter'"/>
        </xsl:call-template>
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'currencies'"/>
        </xsl:call-template>
        <xsl:call-template name="add-udp-price-config-to-root"/>
    </xsl:template>

    <xsl:template match="t:price-engine-blotter" mode="component-specific">
        <xsl:param name="root"/>
        <xsl:apply-templates select="$root/udpprice:configuration"/>
        <!-- uDPClient2Server extension -->
        <group key="uDPClient2Server">
            <entry key="numberOfUDPConnection">
                <xsl:value-of select="count($root/broadcast:configuration/broadcast:node)"/>
            </entry>
        </group>
        <!-- popForward -->
        <group key="popForward">
            <xsl:for-each select="$root/comp:configuration/comp:ui/comp:flag[starts-with(@id,'At')]">
                <entry>
                    <xsl:attribute name="key">
                        <xsl:value-of select="@id"/>
                    </xsl:attribute>
                    <xsl:value-of select="."/>
                </entry>
            </xsl:for-each>
        </group>
        <!-- priceDisplay -->
        <entry key="pricedisplay">
            <xsl:value-of select="$root/comp:configuration/comp:priceDisplay/@active"/>
        </entry>
        <group key="pricedisplay">
            <entry key="CPs_per_row">
                <xsl:value-of
                    select="$root/comp:configuration/comp:priceDisplay/@currencyPairsPerRow"/>
            </entry>
            <entry key="CurrencyPairs">
                <xsl:value-of select="$root/comp:configuration/comp:priceDisplay/@currencyPairs"/>
            </entry>
        </group>
        <!-- Sounds -->
        <group key="sound">
            <xsl:for-each select="$root/comp:configuration/comp:ui/comp:sound">
                <entry>
                    <xsl:attribute name="key">
                        <xsl:value-of select="@id"/>
                    </xsl:attribute>
                    <xsl:value-of select="."/>
                </entry>
            </xsl:for-each>
        </group>
        <!-- Failed Trade -->
        <xsl:choose>
            <xsl:when test="$root/comp:configuration/comp:ui/comp:failedTrade">
                <entry key="showFailedTradePopup">true</entry>
                <entry key="failedTradePopupPosition">
                    <xsl:value-of
                        select="$root/comp:configuration/comp:ui/comp:failedTrade/@position"/>
                </entry>
            </xsl:when>
            <xsl:otherwise>
                <entry key="showFailedTradePopup">false</entry>
            </xsl:otherwise>
        </xsl:choose>
        <!-- More UI -->
        <entry key="salesBlotter">
            <xsl:value-of select="$root/comp:configuration/comp:ui/comp:flag[@id='salesBlotter']"/>
        </entry>
        <entry key="countDownSeconds">
            <xsl:value-of select="$root/comp:configuration/comp:ui/comp:countdownSeconds"/>
        </entry>
        <group key="screenClose">
            <entry key="di">
                <xsl:value-of select="$root/comp:configuration/comp:ui/comp:tabAfterDI"/>
            </entry>
        </group>

        <!-- TODO something is still wrong -->
        <xsl:if test="$root/cp:configuration/cp:currency[c:component[@id='risk-manager']]">
            <group key="riskManager">
                <entry key="enableRiskManager">true</entry>
                <entry key="currenciesForRiskManager">
                    <xsl:apply-templates
                        select="$root/cp:configuration/cp:currency[c:component[@id='risk-manager']]"
                    />
                </entry>
            </group>
        </xsl:if>
        <!-- references -->
        <xsl:if
            test="$root/comp:configuration/comp:ui/comp:flag[@id='tradingTool']/text() = 'true'">
            <xsl:call-template name="config-reference">
                <xsl:with-param name="refName">tradingToolConfig</xsl:with-param>
                <xsl:with-param name="refType">propertiesxml</xsl:with-param>
                <xsl:with-param name="refComponentId">trading-tool</xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template match="cp:currency[c:component[@id='risk-manager']]">
        <xsl:value-of select="@id"/>
        <xsl:if test="position() != last()">
            <xsl:text>,</xsl:text>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
