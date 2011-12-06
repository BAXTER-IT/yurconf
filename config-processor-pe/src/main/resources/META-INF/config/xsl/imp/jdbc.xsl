<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:pedb="http://baxter-it.com/config/pe/db"
    xmlns="http://baxter-it.com/config/pe/properties" exclude-result-prefixes="xs pedb" version="2.0">

    <xsl:import href="merge-redundant-groups.xsl"/>

    <xsl:template name="add-jdbc-config-to-root">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'jdbc'"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="pedb:connection" mode="entries-only">
        <xsl:param name="suffix" select="''"/>
        <entry>
            <xsl:attribute name="key">
                <xsl:text>dbDriver</xsl:text>
                <xsl:value-of select="$suffix"/>
            </xsl:attribute>
            <xsl:value-of select="@driver"/>
        </entry>
        <entry>
            <xsl:attribute name="key">
                <xsl:text>dbAddress</xsl:text>
                <xsl:value-of select="$suffix"/>
            </xsl:attribute>
            <xsl:value-of select="@url"/>
        </entry>
        <xsl:choose>
            <xsl:when test="@encodedPassword">
                <entry>
                    <xsl:attribute name="key">
                        <xsl:text>dbPasswdEnc</xsl:text>
                        <xsl:value-of select="$suffix"/>
                    </xsl:attribute>
                    <xsl:value-of select="@encodedPassword"/>
                </entry>
            </xsl:when>
            <xsl:otherwise>
                <entry>
                    <xsl:attribute name="key">
                        <xsl:text>password</xsl:text>
                        <xsl:value-of select="$suffix"/>
                    </xsl:attribute>
                    <xsl:value-of select="@password"/>
                </entry>
                <entry>
                    <xsl:attribute name="key">
                        <xsl:text>username</xsl:text>
                        <xsl:value-of select="$suffix"/>
                    </xsl:attribute>
                    <xsl:value-of select="@username"/>
                </entry>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="pedb:connection">
        <xsl:param name="path" select="@id"/>
        <xsl:choose>
            <xsl:when test="contains($path,'.')">
                <xsl:call-template name="in-container">
                    <xsl:with-param name="path" select="$path"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <group>
                    <xsl:attribute name="key">
                        <xsl:value-of select="$path"/>
                    </xsl:attribute>
                    <xsl:apply-templates select="." mode="entries-only"/>
                </group>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
