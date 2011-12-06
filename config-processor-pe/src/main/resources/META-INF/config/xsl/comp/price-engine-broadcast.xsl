<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/broadcastcomp"
    xmlns:broadcast="http://baxter-it.com/config/pe/broadcast"
    xmlns:c="http://baxter-it.com/config/component"
    xmlns="http://baxter-it.com/config/pe/properties"
    exclude-result-prefixes="xs t c comp broadcast" version="2.0">

    <xsl:import href="../imp/config-ref.xsl"/>
    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:param name="configurationVariant"/>
    <xsl:param name="configurationProductId"/>
    <xsl:param name="configurationVersion"/>
    <xsl:param name="configurationComponentId"/>

    <xsl:template match="t:price-engine-broadcast" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'broadcast'"/>
        </xsl:call-template>
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'comp/broadcast'"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="t:price-engine-broadcast" mode="component-specific">
        <xsl:param name="root"/>
        <!-- DCPK configuration reference -->
        <xsl:call-template name="config-reference">
            <xsl:with-param name="refName">stpConfig</xsl:with-param>
            <!-- TODO complete impl for conf file -->
            <xsl:with-param name="refType">DUMMY</xsl:with-param>
            <xsl:with-param name="refComponentId">DUMMY</xsl:with-param>
        </xsl:call-template>
        <!-- General broadcast config -->
        <entry key="udpPort">
            <xsl:value-of
                select="$root/broadcast:configuration/broadcast:node[@id='broadcast1']/@pricePort"/>
        </entry>
        <entry key="uDPClientConnectionPort">
            <xsl:value-of
                select="$root/broadcast:configuration/broadcast:node[@id='broadcast1']/@channelPort"
            />
        </entry>
        <!-- TODO test rewriting of @id in variants -->
        <entry key="broadcastNode">
            <xsl:value-of select="$root/comp:configuration/@id"/>
        </entry>
        <xsl:apply-templates select="$root/comp:configuration/*"/>
    </xsl:template>

    <xsl:template match="comp:roundTripInterval">
        <group key="tradeEventManager">
            <xsl:call-template name="element-to-attribute"/>
        </group>
    </xsl:template>

    <xsl:template match="comp:handoffTimeout">
        <group key="handoffmanager">
            <group key="Upstream2HandOff">
                <entry key="timeout"><xsl:apply-templates /></entry>
            </group>                
        </group>
    </xsl:template>
    
    <xsl:template match="comp:creditCheck">
        <group key="creditcheckmanager">
            <entry key="timeout">
                <xsl:value-of select="@timeout"/>
            </entry>
            <xsl:apply-templates select="comp:heartbeat"/>
        </group>
    </xsl:template>

    <xsl:template match="comp:heartbeat">
        <group key="heartbeat">
            <entry key="NumberOfRequiredHeartbeat">
                <xsl:value-of select="@number"/>
            </entry>
            <entry key="HeartbeatInterval">
                <xsl:value-of select="@interval"/>
            </entry>
        </group>
    </xsl:template>

    <xsl:template match="comp:*">
        <xsl:call-template name="element-to-attribute"/>
    </xsl:template>

    <xsl:template name="element-to-attribute">
        <entry>
            <xsl:attribute name="key">
                <xsl:value-of select="local-name()"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </entry>
    </xsl:template>

</xsl:stylesheet>
