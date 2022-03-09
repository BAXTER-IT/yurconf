<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:bcl="http://baxter-it.com/config/log" xmlns:c="http://baxter-it.com/config/component"
	xmlns:conf="http://baxter-it.com/config" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xs bcl c conf" version="2.0">

	<xsl:import href="repo-base.xsl" />
	<xsl:import href="conf-reference.xsl" />

	<xsl:output encoding="UTF-8" method="html" />

	<xsl:param name="configurationProductId" />
	<xsl:param name="configurationVersion" />

	<xsl:template match="/">
		<xsl:variable name="root">
			<xsl:copy-of select="conf:configuration-source/conf:request" />
			<xsl:call-template name="load-sources" />
		</xsl:variable>
		<html>
			<head>
				<xsl:call-template name="viewer-title" />
				<style type="text/css">
					<xsl:call-template name="css" />
				</style>
			</head>
			<body>
				<xsl:apply-templates select="$root/conf:request" />
			</body>
		</html>
	</xsl:template>

	<xsl:template name="css">
		<!-- Put your styles here -->
	</xsl:template>

	<xsl:template match="conf:request">
		<h1>
			<xsl:call-template name="viewer-header" />
		</h1>
		<p>
			<xsl:text>You see the configuration for variants list as specified on your client's config file {</xsl:text>
			<code>
				<xsl:value-of select="conf:variant/@id" separator="," />
			</code>
			<xsl:text>}</xsl:text>
			<form method="get">
				<p>
					You may change it by resubmitting the variants list (separate variant names by comma)
					<input type="text" name="variants">
						<xsl:attribute name="value"> <xsl:value-of select="conf:variant/@id"
							separator="," /> </xsl:attribute>
					</input>
					<xsl:call-template name="form" />
					<input type="submit" value="Refresh" />
				</p>
			</form>
		</p>
		<hr />
	</xsl:template>

	<xsl:template name="form">
		<!-- put extra controls here -->
	</xsl:template>

	<xsl:template name="load-sources">
		<!-- Load all sources needed for evaluation here -->
	</xsl:template>

	<xsl:template name="viewer-header">
		<xsl:text>Configuration Explorer for </xsl:text>
		<xsl:call-template name="product-title" />
		<xsl:text> v. </xsl:text>
		<xsl:value-of select="$configurationVersion" />
	</xsl:template>

	<xsl:template name="viewer-title">
		<title>
			<xsl:text>Configuration Explorer for </xsl:text>
			<xsl:call-template name="product-title" />
			<xsl:text> - Baxter Configuration Server</xsl:text>
		</title>
	</xsl:template>

	<xsl:template name="product-title">
		<xsl:value-of select="$configurationProductId" />
	</xsl:template>

	<xsl:template match="conf:reference" mode="anchor">
		<xsl:param name="productId" />
		<xsl:param name="componentId" />
		<xsl:param name="link-message" />
		<a>
			<xsl:attribute name="href">
                    <xsl:apply-templates select="." mode="url">
                    <xsl:with-param name="productId" select="$productId" />
                    <xsl:with-param name="componentId" select="$componentId" />
                    </xsl:apply-templates>
                </xsl:attribute>
			<xsl:choose>
				<xsl:when test="$link-message">
					<xsl:value-of select="$link-message" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates select="." mode="url">
						<xsl:with-param name="productId" select="$productId" />
						<xsl:with-param name="componentId" select="$componentId" />
					</xsl:apply-templates>
				</xsl:otherwise>
			</xsl:choose>
		</a>
	</xsl:template>

	<xsl:template match="conf:reference" mode="picture">
		<xsl:param name="productId" />
		<xsl:param name="componentId" />
		<xsl:param name="picture-path" />
		<a>
			<xsl:attribute name="href">
                    <xsl:apply-templates select="." mode="url">
                    <xsl:with-param name="productId" select="$productId" />
                    <xsl:with-param name="componentId" select="$componentId" />
                    </xsl:apply-templates>
                </xsl:attribute>
			<xsl:text disable-output-escaping="yes">&lt;img src="</xsl:text>
			<xsl:value-of select="$picture-path" />
			<xsl:text disable-output-escaping="yes">" width="20" height="20" /&gt;</xsl:text>
		</a>
	</xsl:template>

</xsl:stylesheet>
