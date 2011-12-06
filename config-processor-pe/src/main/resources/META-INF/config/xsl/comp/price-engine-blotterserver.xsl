<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:comp="http://baxter-it.com/config/pe/blotterserver"
    xmlns:broadcast="http://baxter-it.com/config/pe/broadcast"
    xmlns:pedb="http://baxter-it.com/config/pe/db" xmlns="http://baxter-it.com/config/pe/properties"
    xmlns:c="http://baxter-it.com/config/component"
    exclude-result-prefixes="xs t pedb comp broadcast  c" version="2.0">

    <xsl:import href="../imp/udp-price.xsl"/>
    <xsl:import href="../imp/config-ref.xsl"/>
    <xsl:import href="../imp/params.xsl"/>
    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:param name="configurationVariant"/>
    <xsl:param name="configurationProductId"/>
    <xsl:param name="configurationVersion"/>
    <xsl:param name="configurationComponentId"/>

    <xsl:template match="t:price-engine-blotterserver" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'broadcast'"/>
        </xsl:call-template>
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'comp/blotterserver'"/>
        </xsl:call-template>
        <xsl:call-template name="add-jdbc-config-to-root"/>
    </xsl:template>

    <xsl:template match="t:price-engine-blotterserver" mode="component-specific">
        <xsl:param name="root"/>
        <xsl:apply-templates
            select="$root/pedb:configuration/pedb:connection[@id='mmm.dbConnection']"/>
        <xsl:apply-templates select="$root/comp:configuration/comp:mmm"/>
        <xsl:apply-templates select="$root/comp:configuration/comp:abos"/>
    </xsl:template>

    <xsl:template match="comp:abos">
        <group key="abos">
            <xsl:for-each select="@*[name() != 'id']">
                <entry>
                    <xsl:attribute name="key">
                        <xsl:value-of select="name()"/>
                    </xsl:attribute>
                    <xsl:value-of select="."/>
                </entry>
            </xsl:for-each>
        </group>
    </xsl:template>

    <xsl:template match="comp:mmm">
        <group key="mmm">
            <entry key="priceBufferSize">
                <xsl:value-of select="@priceBuffereSize"/>
            </entry>
            <group key="broadcastMMM">
                <xsl:apply-templates select="comp:broadcastChannel[@id='1']"/>
                <xsl:apply-templates select="comp:broadcastChannel[@id='2']">
                    <xsl:with-param name="suffix">2</xsl:with-param>
                </xsl:apply-templates>
            </group>
        </group>
    </xsl:template>

    <xsl:template match="comp:broadcastChannel">
        <xsl:param name="suffix" select="''" />
        <entry>
            <xsl:attribute name="key">
                <xsl:text>clientPort</xsl:text>
                <xsl:value-of select="$suffix" />
            </xsl:attribute>
            <xsl:call-template name="parameter">
                <xsl:with-param name="name">clientPort</xsl:with-param>
            </xsl:call-template>
        </entry>
        <entry>
            <xsl:attribute name="key">
                <xsl:text>serverHost</xsl:text>
                <xsl:value-of select="$suffix" />
            </xsl:attribute>
            <xsl:call-template name="parameter">
                <xsl:with-param name="name">serverHost</xsl:with-param>
            </xsl:call-template>
        </entry>
        <entry>
            <xsl:attribute name="key">
                <xsl:text>udpServerPort</xsl:text>
                <xsl:value-of select="$suffix" />
            </xsl:attribute>
            <xsl:call-template name="parameter">
                <xsl:with-param name="name">udpServerPort</xsl:with-param>
            </xsl:call-template>            
        </entry>
    </xsl:template>


</xsl:stylesheet>
