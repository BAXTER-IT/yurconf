<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" xmlns:yc="http://yurconf.org" exclude-result-prefixes="xs xd" version="2.0">

	<xsl:import href="yurconf://@org.yurconf.base/text-fmt.xsl" />

	<xsl:param name="configurationProductId" select="'N/A'" />
	<xsl:param name="configurationVersion" select="'N/A'" />
	<xsl:param name="configurationComponentId" select="'N/A'" />

	<xsl:template name="input-dump">
		<xsl:text>Hello!</xsl:text>
		<xsl:call-template name="CR" />
		<xsl:text>You requested following configuration:</xsl:text>
		<xsl:call-template name="CR" />
		<xsl:call-template name="CR" />
		<xsl:text>ProductID: </xsl:text>
		<xsl:value-of select="$configurationProductId" />
		<xsl:call-template name="CR" />
		<xsl:text>ComponentID: </xsl:text>
		<xsl:value-of select="$configurationComponentId" />
		<xsl:call-template name="CR" />
		<xsl:text>Version: </xsl:text>
		<xsl:value-of select="$configurationVersion" />
		<xsl:apply-templates select="yc:configuration-source" />
	</xsl:template>

	<xsl:template match="yc:configuration-source">
		<xsl:call-template name="CR" />
		<xsl:call-template name="CR" />
		<xsl:text>Some more content from configuration-source here:</xsl:text>
		<xsl:call-template name="CR" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="yc:request">
		<xsl:text>Base: </xsl:text>
		<xsl:value-of select="@base" />
		<xsl:call-template name="CR" />
		<xsl:text>Params: </xsl:text>
		<xsl:apply-templates select="yc:parameter" />
		<xsl:call-template name="CR" />
		<xsl:text>Variants: </xsl:text>
		<xsl:apply-templates select="yc:variant" />
		<xsl:call-template name="CR" />
	</xsl:template>

	<xsl:template match="yc:parameter">
		<xsl:call-template name="CR" />
		<xsl:text> - </xsl:text>
		<xsl:value-of select="@id" />
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="yc:variant">
		<xsl:choose>
			<xsl:when test="position()=1">
				<xsl:text> = </xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>,</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:value-of select="@id" />
	</xsl:template>

</xsl:stylesheet>