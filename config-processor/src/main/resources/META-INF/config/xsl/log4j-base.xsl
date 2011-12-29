<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:bcl="http://baxter-it.com/config/log"
  xmlns:c="http://baxter-it.com/config/component"
  xmlns:log4j="http://jakarta.apache.org/log4j/" xmlns:xs="http://www.w3.org/2001/XMLSchema"
  exclude-result-prefixes="xs bcl c " version="2.0">

  <xsl:output encoding="UTF-8" method="xml" doctype-system="log4j.dtd"/>

  <xsl:param name="configurationComponentId"/>

  <xsl:template match="bcl:configuration">
    <log4j:configuration debug="true">
      <xsl:apply-templates select="bcl:console-appender[c:component[@id=$configurationComponentId]]"/>
      <xsl:apply-templates select="bcl:rolling-file-appender[c:component[@id=$configurationComponentId]]"/>
      <xsl:apply-templates select="bcl:logger[c:component[@id=$configurationComponentId]][@name!='ROOT']"/>
      <xsl:apply-templates select="bcl:logger[c:component[@id=$configurationComponentId]][@name='ROOT']"/>
    </log4j:configuration>
  </xsl:template>

  <xsl:template match="bcl:console-appender">
    <appender class="org.apache.log4j.ConsoleAppender">
      <xsl:attribute name="name">
        <xsl:value-of select="@name"/>
      </xsl:attribute>
      <param name="Target" value="System.out"/>
      <xsl:apply-templates select="bcl:layout"/>
    </appender>
  </xsl:template>

  <xsl:template name="appender-pseudo-name">
    <xsl:param name="name"/>
    <xsl:attribute name="{$name}">
      <xsl:text>PSEUDO_</xsl:text>
      <xsl:choose>
        <xsl:when test="@id">
          <xsl:value-of select="@id"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="."/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:attribute>
  </xsl:template>

  <xsl:template name="async-appender">
    <appender class="org.apache.log4j.AsyncAppender">
      <xsl:attribute name="name">
        <xsl:value-of select="@name"/>
      </xsl:attribute>
      <appender-ref>
        <xsl:call-template name="appender-pseudo-name">
          <xsl:with-param name="name">
            <xsl:text>ref</xsl:text>
          </xsl:with-param>
        </xsl:call-template>
      </appender-ref>
    </appender>
  </xsl:template>

  <xsl:template match="bcl:layout">
    <layout class="org.apache.log4j.PatternLayout">
      <param name="ConversionPattern">
        <xsl:attribute name="value">
          <xsl:value-of select="."/>
        </xsl:attribute>
      </param>
    </layout>
  </xsl:template>


  <xsl:template name="build-log-directory-name">
    <xsl:text>--NOT IMPLEMENTED--</xsl:text>
  </xsl:template>

  <xsl:template match="bcl:rolling-file-appender/@file | bcl:file-appender/@file">
    <param name="File">
      <xsl:attribute name="value">
        <xsl:call-template name="build-log-directory-name"/>
        <xsl:value-of select="."/>
      </xsl:attribute>
    </param>
  </xsl:template>
  
  <xsl:template match="bcl:rolling-file-appender/@name | bcl:file-appender/@name">
    <xsl:call-template name="appender-pseudo-name">
      <xsl:with-param name="name">
        <xsl:text>name</xsl:text>
      </xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template match="bcl:rolling-file-appender/@maxSize">
    <param name="MaxFileSize">
      <xsl:attribute name="value">
        <xsl:value-of select="."/>
      </xsl:attribute>
    </param>
  </xsl:template>
  
  <xsl:template match="bcl:rolling-file-appender/@backupIndex">
    <param name="MaxBackupIndex">
      <xsl:attribute name="value">
        <xsl:value-of select="."/>
      </xsl:attribute>
    </param>
  </xsl:template>
  
  <xsl:template match="bcl:rolling-file-appender">
    <xsl:call-template name="async-appender"/>
    <appender class="org.apache.log4j.RollingFileAppender">
      <xsl:apply-templates select="@name"/>
      <xsl:apply-templates select="@file"/>
      <xsl:apply-templates select="@maxSize"/>
      <xsl:apply-templates select="@backupIndex"/>
      <xsl:apply-templates />
    </appender>
  </xsl:template>

  <xsl:template match="bcl:file-appender">
    <xsl:call-template name="async-appender"/>
    <appender class="org.apache.log4j.FileAppender">
      <xsl:apply-templates select="@name"/>
      <xsl:apply-templates select="@file"/>
      <xsl:apply-templates />
    </appender>
  </xsl:template>

  <xsl:template match="bcl:logger[@name='ROOT']">
    <root>
      <xsl:apply-templates select="@level"/>
      <xsl:apply-templates select="bcl:appender-ref"/>
    </root>
  </xsl:template>

  <xsl:template match="bcl:logger">
    <category>
      <xsl:apply-templates select="@name"/>
      <xsl:apply-templates select="@additivity"/>
      <xsl:apply-templates select="@level"/>
      <xsl:apply-templates select="bcl:appender-ref"/>
    </category>
  </xsl:template>

  <xsl:template match="bcl:logger/@name">
    <xsl:attribute name="name">
      <xsl:value-of select="."/>
    </xsl:attribute>
  </xsl:template>

  <xsl:template match="bcl:logger/@additivity">
    <xsl:attribute name="additivity">
      <xsl:value-of select="."/>
    </xsl:attribute>
  </xsl:template>

  <xsl:template match="bcl:logger/@level">
    <level>
      <xsl:attribute name="value">
        <xsl:value-of select="."/>
      </xsl:attribute>
    </level>
  </xsl:template>

  <xsl:template match="bcl:appender-ref">
    <appender-ref>
      <xsl:attribute name="ref">
        <xsl:value-of select="@ref"/>
      </xsl:attribute>
    </appender-ref>
  </xsl:template>

</xsl:stylesheet>
