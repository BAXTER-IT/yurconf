<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:j="http://yurconf.org/jvm" xmlns:c="http://yurconf.org/component" xmlns:conf="http://yurconf.org"
    xmlns="http://yurconf.org/om/jvm" exclude-result-prefixes="xs j c conf" version="2.0">

    <xsl:import href="repo-base.xsl" />

    <xsl:output method="xml" encoding="UTF-8" indent="yes" omit-xml-declaration="no" />

    <xsl:param name="configurationComponentId" />

    <xsl:template match="/">
        <xsl:variable name="root">
            <xsl:copy-of select="conf:configuration-source/conf:request" />
            <xsl:apply-templates select="conf:configuration-source/conf:request" mode="load-document-with-variants">
                <xsl:with-param name="prefix" select="'jvm'" />
            </xsl:apply-templates>
        </xsl:variable>
        <xsl:apply-templates select="$root/j:configuration" />
    </xsl:template>

    <xsl:template match="j:configuration">
        <classpath>
            <xsl:apply-templates select="j:classpath[c:component[@id=$configurationComponentId]]" />
        </classpath>
    </xsl:template>

    <xsl:template match="j:classpath">
        <path>
            <xsl:value-of select="@url" />
        </path>
    </xsl:template>

</xsl:stylesheet>

