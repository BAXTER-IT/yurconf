<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:p="http://proba"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">

    <xsl:output indent="yes" method="xml"/>

    <xsl:template match="/">
        <xsl:variable name="in1" select="document('input1.xml')/node()"/>
        <xsl:variable name="in2" select="document('input2.xml')/node()"/>
        <xsl:apply-templates select="$in1" mode="mergeNodes">
            <xsl:with-param name="other" select="$in2"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="*" mode="mergeNodes">
        <xsl:param name="other"/>
        <xsl:choose>
            <xsl:when test="$other">
                <xsl:element name="{name()}" namespace="{namespace-uri()}">
                    <xsl:copy-of select="namespace::*"/>
                    <xsl:copy-of select="$other/namespace::*"/>
                    <xsl:copy-of select="@*"/>
                    <xsl:copy-of select="$other/@*"/>
                    <xsl:choose>
                        <xsl:when test="count(*)=0">
                            <xsl:copy-of select="normalize-unicode(text())"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <!-- copy/merge from source -->
                            <xsl:for-each select="*">
                                <xsl:variable name="otherNode"
                                    select="$other/*[name()=name(current()) and @id=current()/@id]"/>
                                <xsl:apply-templates select="current()" mode="mergeNodes">
                                    <xsl:with-param name="other">
                                        <xsl:value-of select="$otherNode"/>
                                    </xsl:with-param>
                                </xsl:apply-templates>
                            </xsl:for-each>
                            <!-- copy target nodes -->
                            <xsl:variable name="src" select="."/>
                            <xsl:for-each select="$other/*">
                                <xsl:if
                                    test="not(@id) or not($src/*[name()=name(current()) and @id=current()/@id])">
                                    <xsl:copy-of select="current()"/>
                                </xsl:if>
                            </xsl:for-each>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:element>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
