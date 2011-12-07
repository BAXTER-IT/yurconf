<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:pejms="http://baxter-it.com/config/pe/jms"
  xmlns:peprop="http://baxter-it.com/config/pe/properties"
  xmlns:c="http://baxter-it.com/config/component"
  xmlns:clients="http://baxter-it.com/config/pe/clients" xmlns:t="urn:templates"
  exclude-result-prefixes="xs pejms peprop c t clients"
  xmlns="http://baxter-it.com/config/pe/properties" version="2.0">

  <xsl:import href="baxterxsl:repo-base.xsl"/>
  <xsl:import href="imp/merge-redundant-groups.xsl"/>

  <xsl:import href="comp/price-engine-dbserver.xsl"/>
  <xsl:import href="comp/price-engine-broadcast.xsl"/>
  <xsl:import href="comp/price-engine-admintool.xsl"/>
  <xsl:import href="comp/price-engine-blotter.xsl"/>
  <xsl:import href="comp/trading-tool.xsl"/>
  <xsl:import href="comp/price-engine-blotterserver.xsl"/>
  <xsl:import href="comp/price-engine-fwdmonitor.xsl"/>
  <xsl:import href="comp/price-engine-trackwheel.xsl"/>
  
  <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

  <xsl:param name="configurationProductId"/>
  <xsl:param name="configurationVersion"/>
  <xsl:param name="configurationComponentId"/>
  <xsl:param name="configurationVariant"/>

  <xsl:template match="/">
    <xsl:variable name="templateToCall">
      <xsl:element name="{$configurationComponentId}" namespace="urn:templates"/>
    </xsl:variable>
    <xsl:variable name="root">
      <xsl:call-template name="load-merged-repo-document">
        <xsl:with-param name="prefix" select="'jms'"/>
      </xsl:call-template>
      <xsl:call-template name="load-merged-repo-document">
        <xsl:with-param name="prefix" select="'properties'"/>
      </xsl:call-template>
      <xsl:call-template name="load-merged-repo-document">
        <xsl:with-param name="prefix" select="'clients'"/>
      </xsl:call-template>
      <xsl:apply-templates select="$templateToCall/*" mode="component-specific-sources"/>
    </xsl:variable>
    <xsl:variable name="redundantProperties">
      <properties>
        <xsl:attribute name="version">
          <xsl:value-of select="$configurationVersion"/>
        </xsl:attribute>
        <!-- Generic Clients processing -->
        <xsl:apply-templates
          select="$root/clients:configuration[c:component[@id=$configurationComponentId]]"
          mode="window-xml-import"/>
        <xsl:apply-templates
          select="$root/clients:configuration/c:component[@id=$configurationComponentId]/clients:port[@id='uniqueApp']"/>
        <!-- Render generic poroperties -->
        <xsl:apply-templates
          select="$root/peprop:properties/(peprop:property)[c:component/@id=$configurationComponentId]"/>
        <!-- Render generic channels -->
        <xsl:apply-templates
          select="$root/pejms:configuration/(pejms:topic|pejms:queue)[c:component/@id=$configurationComponentId]"/>
        <!-- Render global JMS Configuration -->
        <xsl:apply-templates select="$root/pejms:configuration/pejms:authentication[@id='global']"
          mode="global-jms"/>
        <xsl:apply-templates select="$root/pejms:configuration/pejms:ssl[@id='jmsSSL']"/>
        <!-- Now each component has its own processing -->
        <xsl:apply-templates select="$templateToCall/*" mode="component-specific">
          <xsl:with-param name="root" select="$root"/>
        </xsl:apply-templates>
      </properties>
    </xsl:variable>
    <!-- Consolidate the elements within same parent groups -->
    <xsl:apply-templates select="$redundantProperties/peprop:properties"/>
  </xsl:template>

  <xsl:template match="clients:port[@id='uniqueApp']">
    <entry key="applicationUniquePort">
      <xsl:apply-templates/>
    </entry>
  </xsl:template>

  <xsl:template match="clients:configuration" mode="window-xml-import">
    <import writeable="true">
      <xsl:attribute name="resource">
        <xsl:text>./</xsl:text>
        <xsl:value-of select="$configurationComponentId"/>
        <xsl:if test="$configurationVariant">
          <xsl:text>(</xsl:text>
          <xsl:value-of select="$configurationVariant"/>
          <xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:text>_window.xml</xsl:text>
      </xsl:attribute>
    </import>
    <group key="bounds">
      <entry key="X">0</entry>
      <entry key="Y">0</entry>
      <entry key="W">800</entry>
      <entry key="H">600</entry>
    </group>
  </xsl:template>

  <xsl:template match="pejms:authentication" mode="global-jms">
    <entry key="GlobalJMSUserName">
      <xsl:value-of select="@username"/>
    </entry>
    <entry key="GlobalJMSPassword">
      <xsl:value-of select="@password"/>
    </entry>
  </xsl:template>

  <xsl:template match="pejms:ssl">
    <group>
      <xsl:attribute name="key">
        <xsl:value-of select="@id"/>
      </xsl:attribute>
      <entry key="TrustStore">
        <xsl:value-of select="@store"/>
      </entry>
      <entry key="KeyStorePassword">
        <xsl:value-of select="@storePassword"/>
      </entry>
      <entry key="AnonCipher">
        <xsl:value-of select="@anonCipher"/>
      </entry>
    </group>
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

  <xsl:template match="pejms:node" mode="jmsHALocation">
    <entry key="Host2">
      <xsl:value-of select="@host"/>
    </entry>
    <entry key="Port2">
      <xsl:value-of select="@port"/>
    </entry>
  </xsl:template>

  <xsl:template match="pejms:topic[@nodeHA] | pejms:queue[@nodeHA]" mode="jms-for-HA">
    <xsl:comment>HA with nodeHA</xsl:comment>
    <xsl:apply-templates select="../pejms:node[@id=current()/@nodeHA]" mode="jmsHALocation"/>
  </xsl:template>

  <xsl:template match="pejms:topic | pejms:queue" mode="jms-for-HA">
    <xsl:comment>HA without nodeHA</xsl:comment>
    <entry key="Host2">null</entry>
    <entry key="Port2">0</entry>
  </xsl:template>

  <xsl:template name="channel-jms-reference">
    <xsl:apply-templates select="../pejms:node[@id=current()/@node]" mode="jmsLocation"/>
    <xsl:apply-templates select="." mode="jms-for-HA"/>
    <xsl:apply-templates select="../pejms:authentication[@id=current()/@authentication]"/>
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
            <xsl:value-of select="$path"/>
          </xsl:attribute>
          <entry key="TName">
            <xsl:value-of select="@name"/>
          </entry>
          <xsl:call-template name="channel-jms-reference"/>
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
          <xsl:call-template name="channel-jms-reference"/>
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
