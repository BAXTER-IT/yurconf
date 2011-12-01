<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">

  <!-- gets list of source files as input document. load each file and move element's text into attribute value save result 
    files with prefix "converted-" read catalogue/input.xml -> write catalogue/converted-input1.xml same for variant -->

  <xsl:import href="baxterxsl:repo-base.xsl"/>
  <xsl:import href="baxterxsl:text-fmt.xsl"/>

  <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
  <xsl:output name="catalogue" method="xml" encoding="UTF-8" indent="yes"/>

  <xsl:template match="/">
    <xsl:copy-of select="."/>
    <xsl:apply-templates select="configuration-source/sources/source"/>
  </xsl:template>
  
  <xsl:template name="after-slash">
    <xsl:param name="path" />
    <xsl:choose>
      <xsl:when test="contains($path,'/')">
        <xsl:call-template name="after-slash">
          <xsl:with-param name="path" select="substring-after($path,'/')" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$path"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="source">
    <xsl:variable name="sourceUrl" select="."/>
    <xsl:variable name="source" select="doc($sourceUrl)"/>
    <xsl:variable name="targetUrl">
      <xsl:value-of select="../@repo" />
      <xsl:text>catalogue/converted-</xsl:text>
      <xsl:call-template name="after-slash">
        <xsl:with-param name="path" select="$sourceUrl" />
      </xsl:call-template>
    </xsl:variable>
    <debug>
      <source><xsl:value-of select="$sourceUrl"/></source>
      <target><xsl:value-of select="$targetUrl"/></target>
    </debug>
    <xsl:result-document href="{$targetUrl}" format="catalogue">
      <catalogue>
        <xsl:for-each select="$source/catalogue/element">
          <element>
            <xsl:attribute name="value">
              <xsl:value-of select="normalize-space(.)"/>
            </xsl:attribute>
            <xsl:copy-of select="@*"/>
            <xsl:copy-of select="*"/>
          </element>
        </xsl:for-each>
      </catalogue>
    </xsl:result-document>
  </xsl:template>

</xsl:stylesheet>
