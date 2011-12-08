<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:pecp="http://baxter-it.com/config/pe/cp"
    xmlns:c="http://baxter-it.com/config/component" exclude-result-prefixes="xs pecp c"
    xmlns="http://baxter-it.com/config/pe/properties" version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl"/>
    <xsl:import href="baxterxsl:text-fmt.xsl"/>

    <xsl:output method="text" encoding="UTF-8" omit-xml-declaration="yes"/>

    <xsl:template match="/">
        <xsl:variable name="root">
            <xsl:copy-of select="configuration-source/request"/>
        </xsl:variable>

        <xsl:variable name="currencies">
            <xsl:call-template name="load-merged-repo-document">
                <xsl:with-param name="prefix" select="'currencies'"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:apply-templates select="$currencies/pecp:configuration">
            <xsl:with-param name="root" select="$root" />
        </xsl:apply-templates>
        
    </xsl:template>

    <xsl:template match="pecp:configuration">
        <xsl:param name="root" />

        <xsl:choose>
            <xsl:when test="$configurationComponentId='price-engine-validator'">

                <xsl:choose>
                    <xsl:when test="$root/request/parameter[@id='epp']/text() = 'true'">
                        <xsl:apply-templates
                            select="pecp:*/c:component[@id=$configurationComponentId]/pecp:port[@epp='true']"
                        />
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates
                            select="pecp:*/c:component[@id=$configurationComponentId]/pecp:port[not(@epp)]"
                        />
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:apply-templates
                    select="pecp:*/c:component[@id=$configurationComponentId]/pecp:port"/>
            </xsl:otherwise>
        </xsl:choose>


    </xsl:template>

    <xsl:template match="pecp:port">
        <xsl:value-of select="../../@id"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="."/>
        <xsl:if test="position() != last()">
            <xsl:call-template name="CRLF"/>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>
