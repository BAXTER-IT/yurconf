<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:bcl="http://baxter-it.com/config/log"
  xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs bcl" version="2.0">

  <xsl:import href="baxterxsl:log4j-base.xsl"/>

  <xsl:output indent="yes"/>

  <xsl:param name="configurationComponentId"/>

  <xsl:template match="/">
    <xsl:variable name="inputFile"
      select="document(concat('baxterrepo:log-',$configurationComponentId,'.xml'))"/>
    <xsl:apply-templates select="$inputFile/bcl:configuration"/>
  </xsl:template>

  <xsl:template name="build-log-directory-name">
    <xsl:text>${demo.logs.dir}/</xsl:text>
  </xsl:template>

</xsl:stylesheet>
