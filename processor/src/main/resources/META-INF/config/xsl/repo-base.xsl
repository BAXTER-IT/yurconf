<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:conf="http://yurconf.org" exclude-result-prefixes="xs conf" version="2.0">

    <xsl:param name="configurationComponentId" />

    <xsl:template name="baxterrepo-protocol">
        <xsl:text>baxterrepo:</xsl:text>
    </xsl:template>

    <xsl:template name="xml-extension">
        <xsl:text>.xml</xsl:text>
    </xsl:template>

    <xsl:template match="conf:request" mode="load-document-with-variants">
        <xsl:param name="prefix" />
        <xsl:variable name="work">
            <conf:request>
                <xsl:attribute name="prefix" select="$prefix" />
                <xsl:copy-of select="conf:variant" />
                <conf:input>
                    <xsl:call-template name="load-repo-document">
                        <xsl:with-param name="prefix" select="$prefix" />
                    </xsl:call-template>
                </conf:input>
            </conf:request>
        </xsl:variable>
        <xsl:apply-templates select="$work/conf:request" mode="merge-with-first-variant" />
    </xsl:template>

    <xsl:template match="conf:request" mode="merge-with-first-variant">
        <xsl:choose>
            <xsl:when test="conf:variant">
                <xsl:variable name="xmlVariantLocation">
                    <xsl:call-template name="variant-repo-location">
                        <xsl:with-param name="prefix" select="@prefix" />
                        <xsl:with-param name="variant" select="conf:variant[1]/@id" />
                    </xsl:call-template>
                </xsl:variable>
                <xsl:variable name="work">
                    <conf:request>
                        <xsl:attribute name="prefix" select="@prefix" />
                        <xsl:copy-of select="conf:variant[position() != 1]" />
                        <conf:input>
                            <xsl:variable name="xmlDoc" select="conf:input/*" />
                            <xsl:choose>
                                <xsl:when test="$xmlVariantLocation and doc-available($xmlVariantLocation)">
                                    <xsl:variable name="xmlVariant" select="doc($xmlVariantLocation)/node()" />
                                    <xsl:apply-templates select="$xmlDoc" mode="merge-nodes-by-id">
                                        <xsl:with-param name="other" select="$xmlVariant" />
                                    </xsl:apply-templates>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:copy-of select="$xmlDoc" />
                                </xsl:otherwise>
                            </xsl:choose>
                        </conf:input>
                    </conf:request>
                </xsl:variable>
                <xsl:apply-templates select="$work/conf:request" mode="merge-with-first-variant" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy-of select="conf:input/*" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--
        Builds the location of a document as "baxterrepo:<prefix>.xml"
    -->
    <xsl:template name="document-repo-location">
        <xsl:param name="prefix" />
        <xsl:param name="suffix" />
        <xsl:call-template name="baxterrepo-protocol" />
        <xsl:value-of select="$prefix" />
        <xsl:if test="$suffix">
            <xsl:value-of select="$suffix" />
        </xsl:if>
        <xsl:call-template name="xml-extension" />
    </xsl:template>

    <!--
        Builds the location of a variant document as "baxterrepo:<prefix>(<variant>).xml"
    -->
    <xsl:template name="variant-repo-location">
        <xsl:param name="prefix" />
        <xsl:param name="variant" />
        <xsl:if test="$variant">
            <xsl:call-template name="document-repo-location">
                <xsl:with-param name="prefix" select="$prefix" />
                <xsl:with-param name="suffix">
                    <xsl:text>(</xsl:text>
                    <xsl:value-of select="$variant" />
                    <xsl:text>)</xsl:text>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <xsl:template name="load-repo-document">
        <xsl:param name="prefix" />
        <xsl:variable name="xmlLocation">
            <xsl:call-template name="document-repo-location">
                <xsl:with-param name="prefix" select="$prefix" />
            </xsl:call-template>
        </xsl:variable>
        <xsl:copy-of select="doc($xmlLocation)/node()" />
    </xsl:template>

    <xsl:template match="*" mode="merge-nodes-by-id">
        <!-- Left operand of a merge is current context node, right operand - parameter "other" -->
        <xsl:param name="other" />
        <xsl:choose>
            <xsl:when test="$other/@conf:skip='all'">
                <!-- Do not output other element if it is marked to skip -->
            </xsl:when>
            <!-- If the right operand is supplied, then try to merge -->
            <xsl:when test="$other">
                <!-- Create node that corresponds to left operand -->
                <xsl:element name="{name()}" namespace="{namespace-uri()}">
                    <!-- Build all namespaces from both operands, right namespaces will override ones from left -->
                    <xsl:copy-of select="namespace::*" />
                    <xsl:copy-of select="$other/namespace::*" />
                    <!-- Build attributes from both operands, right attributes will override ones from left -->
                    <xsl:copy-of select="@*" />
                    <xsl:copy-of select="$other/@*" />
                    <xsl:choose>
                        <!-- There are no more nested elements in both operands, just copy normalized text -->
                        <xsl:when test="count(*)=0 and count($other/*)=0">
                            <xsl:variable name="otherText" select="normalize-unicode($other/text())" />
                            <xsl:choose>
                                <!-- has right node any text? -->
                                <xsl:when test="string-length($otherText)=0">
                                    <xsl:copy-of select="normalize-unicode(text())" />
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:copy-of select="$otherText" />
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                        <!-- We have some nested elements on left side or on right, try to merge -->
                        <xsl:otherwise>
                            <!-- Iterate left elements and look for corresponding elements from right. Merge them. -->
                            <xsl:for-each select="*">
                                <xsl:variable name="otherNode"
                                    select="$other/*[name()=name(current()) and @id=current()/@id]" />
                                <xsl:apply-templates select="current()" mode="merge-nodes-by-id">
                                    <xsl:with-param name="other" select="$otherNode" />
                                </xsl:apply-templates>
                            </xsl:for-each>
                            <!-- Iterate elements et the right side, which do not have corresponding elements at left, and copy them -->
                            <xsl:variable name="src" select="." />
                            <xsl:for-each select="$other/*">
                                <xsl:if test="not(@id) or not($src/*[name()=name(current()) and @id=current()/@id])">
                                    <xsl:copy-of select="current()" />
                                </xsl:if>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:element>
            </xsl:when>
            <!-- Right operand has not been supplied, just make a copy of left operand -->
            <xsl:otherwise>
                <xsl:copy-of select="." />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
