<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:pejms="http://baxter-it.com/config/pe/jms"
    xmlns:peprop="http://baxter-it.com/config/pe/properties"
    xmlns:c="http://baxter-it.com/config/component" exclude-result-prefixes="xs pejms peprop c"
    xmlns="http://baxter-it.com/config/pe/properties" version="2.0">

    <xsl:import href="baxterxsl:repo-base.xsl"/>

    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <xsl:variable name="jmsPrefix" select="'jms'"/>
    <xsl:variable name="propPrefix" select="'properties'"/>

    <xsl:param name="configurationProductId"/>
    <xsl:param name="configurationVersion"/>
    <xsl:param name="configurationComponentId"/>
    <xsl:param name="configurationVariant"/>

    <xsl:template match="/">
        <xsl:variable name="xmlDocLocation">
            <xsl:call-template name="document-repo-location">
                <xsl:with-param name="prefix" select="$jmsPrefix"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="xmlVariantLocation">
            <xsl:call-template name="variant-repo-location">
                <xsl:with-param name="prefix" select="$jmsPrefix"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="jms">
            <xsl:call-template name="merge-document-with-variant">
                <xsl:with-param name="xmlLocation" select="$xmlDocLocation"/>
                <xsl:with-param name="xmlVariantLocation" select="$xmlVariantLocation"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="xmlPropDocLocation">
            <xsl:call-template name="document-repo-location">
                <xsl:with-param name="prefix" select="$propPrefix"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="xmlPropVariantLocation">
            <xsl:call-template name="variant-repo-location">
                <xsl:with-param name="prefix" select="$propPrefix"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:variable name="properties">
            <xsl:call-template name="merge-document-with-variant">
                <xsl:with-param name="xmlLocation" select="$xmlPropDocLocation"/>
                <xsl:with-param name="xmlVariantLocation" select="$xmlPropVariantLocation"/>
            </xsl:call-template>
        </xsl:variable>
        <properties>
            <xsl:attribute name="version">
                <xsl:value-of select="$configurationVersion"/>
            </xsl:attribute>

            <xsl:apply-templates
                select="$properties/peprop:properties/(peprop:property)[c:component/@id=$configurationComponentId]"/>

            <xsl:apply-templates
                select="$jms/pejms:configuration/(pejms:topic|pejms:queue)[c:component/@id=$configurationComponentId]"/>

        </properties>
    </xsl:template>

    <xsl:template match="pejms:authentication">
        <entry key="UserName">
            <xsl:value-of select="@username"/>
        </entry>
        <entry key="Password">
            <xsl:value-of select="@password"/>
        </entry>
    </xsl:template>

    <xsl:template match="pejms:node" mode="jmsLocation">
        <entry key="Host">
            <xsl:value-of select="@host"/>
        </entry>
        <entry key="Port">
            <xsl:value-of select="@port"/>
        </entry>
    </xsl:template>

    <xsl:template name="in-container">
        <xsl:param name="path"/>
        <group>
            <xsl:attribute name="key">
                <xsl:value-of select="substring-before($path,'.')"/>
            </xsl:attribute>
            <xsl:apply-templates select=".">
                <xsl:with-param name="path" select="substring-after($path,'.')"/>
            </xsl:apply-templates>
        </group>
    </xsl:template>

    <xsl:template match="pejms:topic">
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
                        <xsl:value-of select="@id"/>
                    </xsl:attribute>
                    <entry key="TName">
                        <xsl:value-of select="@name"/>
                    </entry>
                    <xsl:apply-templates select="../pejms:node[@id=current()/@node]"
                        mode="jmsLocation"/>
                    <xsl:apply-templates
                        select="../pejms:node[@id=current()/@node]/pejms:authentication"/>
                </group>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="pejms:queue">
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
                    <entry key="QName">
                        <xsl:value-of select="@name"/>
                    </entry>
                    <entry key="QRouter">
                        <xsl:value-of select="../pejms:node[@id=current()/@node]/@router"/>
                    </entry>
                    <xsl:apply-templates select="../pejms:node[@id=current()/@node]"
                        mode="jmsLocation"/>
                    <xsl:apply-templates
                        select="../pejms:node[@id=current()/@node]/pejms:authentication"/>
                </group>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="peprop:property">
        <xsl:param name="path" select="@id"/>
        <xsl:choose>
            <xsl:when test="contains($path,'.')">
                <xsl:call-template name="in-container">
                    <xsl:with-param name="path" select="$path"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <entry>
                    <xsl:attribute name="key">
                        <xsl:value-of select="$path"/>
                    </xsl:attribute>
                    <xsl:value-of select="@value"/>
                </entry>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
