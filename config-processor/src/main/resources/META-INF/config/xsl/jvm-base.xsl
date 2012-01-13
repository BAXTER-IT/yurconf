<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:j="http://baxter-it.com/config/jvm"
    xmlns:c="http://baxter-it.com/config/component" xmlns:conf="http://baxter-it.com/config"
    exclude-result-prefixes="xs c j conf" version="2.0">

    <xsl:import href="baxterxsl:text-fmt.xsl"/>
    <xsl:import href="baxterxsl:conf-reference.xsl"/>

    <xsl:output encoding="UTF-8" method="text"/>

    <xsl:param name="configurationComponentId"/>

    <xsl:template
        match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='windows']"
        mode="append-opt">
        <xsl:param name="opt"/>
        <xsl:text>set JAVA_OPTS=%JAVA_OPTS% </xsl:text>
        <xsl:text>"</xsl:text>
        <xsl:value-of select="$opt"/>
        <xsl:text>"</xsl:text>
        <xsl:call-template name="CRLF"/>
    </xsl:template>

    <xsl:template
        match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='unix']"
        mode="append-opt">
        <xsl:param name="opt"/>
        <xsl:text>JAVA_OPTS="$JAVA_OPTS </xsl:text>
        <xsl:value-of select="$opt"/>
        <xsl:text>"</xsl:text>
        <xsl:call-template name="LF"/>
    </xsl:template>

    <xsl:template name="build-jvm-options">
        <xsl:apply-templates select="j:debug/c:component[@id=$configurationComponentId]/j:port"/>
        <xsl:apply-templates select="j:logging[c:component[@id=$configurationComponentId]]"/>
        <xsl:apply-templates select="j:option[c:component[@id=$configurationComponentId]]"/>
        <xsl:apply-templates select="j:heap[c:component[@id=$configurationComponentId]]"/>
        <xsl:apply-templates select="j:system[c:component[@id=$configurationComponentId]]"/>
        <xsl:call-template name="conf-opts"/>
    </xsl:template>

    <xsl:template name="conf-opts">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dcom.baxter.config.restUrl=</xsl:text>
                <xsl:value-of select="/conf:request/@base"/>
            </xsl:with-param>
        </xsl:apply-templates>
        <xsl:if test="/conf:request/conf:variant">
            <xsl:apply-templates select="/j:configuration" mode="append-opt">
                <xsl:with-param name="opt">
                    <xsl:text>-Dcom.baxter.config.variants=</xsl:text>
                    <xsl:value-of select="/conf:request/conf:variant/@id" separator=","/>
                </xsl:with-param>
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <xsl:template
        match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='windows']"
        mode="script-end"> </xsl:template>

    <xsl:template
        match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='windows']"
        mode="script-start">
        <xsl:text>rem JVM Parameters for Windows</xsl:text>
        <xsl:call-template name="CRLF"/>
        <xsl:call-template name="build-jvm-options"/>
    </xsl:template>

    <xsl:template
        match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='unix']"
        mode="script-start">
        <xsl:text>#!/bin/bash</xsl:text>
        <xsl:call-template name="LF"/>
        <xsl:text># JVM Parameters for Unix</xsl:text>
        <xsl:call-template name="LF"/>
        <xsl:call-template name="build-jvm-options"/>
    </xsl:template>

    <xsl:template
        match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='unix']"
        mode="script-end">
        <xsl:text>export JAVA_OPTS</xsl:text>
        <xsl:call-template name="LF"/>
        <xsl:call-template name="LF"/>
    </xsl:template>

    <xsl:template match="j:debug/@suspend[.='true']">
        <xsl:text>suspend=y</xsl:text>
    </xsl:template>

    <xsl:template match="j:debug/@suspend[.='false']">
        <xsl:text>suspend=n</xsl:text>
    </xsl:template>

    <xsl:template match="j:debug/c:component/j:port">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-agentlib:jdwp=</xsl:text>
                <xsl:text>transport=</xsl:text>
                <xsl:value-of select="../../@transport"/>
                <xsl:text>,</xsl:text>
                <xsl:text>address=</xsl:text>
                <xsl:if test="../../@listenHost">
                    <xsl:value-of select="../../@listenHost"/>
                    <xsl:text>:</xsl:text>
                </xsl:if>
                <xsl:value-of select="."/>
                <xsl:text>,</xsl:text>
                <xsl:text>server=y,</xsl:text>
                <xsl:apply-templates select="../../@suspend"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:logging[@framework='log4j']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dlog4j.configuration=</xsl:text>
                <xsl:apply-templates select="conf:reference" mode="url"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:logging[@framework='logback']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dlogback.configurationFile=</xsl:text>
                <xsl:apply-templates select="conf:reference" mode="url"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system[@value]">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name"/>
                <xsl:text>=</xsl:text>
                <xsl:value-of select="@value"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template
        match="j:system[@param][/conf:request/conf:parameter[@id='osfamily']/text()='unix']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name"/>
                <xsl:text>=$</xsl:text>
                <xsl:value-of select="@param"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system[@var][/conf:request/conf:parameter[@id='osfamily']/text()='unix']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name"/>
                <xsl:text>=$</xsl:text>
                <xsl:value-of select="@var"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template
        match="j:system[@param][/conf:request/conf:parameter[@id='osfamily']/text()='windows']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name"/>
                <xsl:text>=%</xsl:text>
                <xsl:value-of select="@param"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template
        match="j:system[@var][/conf:request/conf:parameter[@id='osfamily']/text()='windows']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name"/>
                <xsl:text>=%</xsl:text>
                <xsl:value-of select="@var"/>
                <xsl:text>%</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:option">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt" select="@value"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:heap/@initial">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Xms</xsl:text>
                <xsl:value-of select="."/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:heap/@maximum">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Xmx</xsl:text>
                <xsl:value-of select="."/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:heap">
        <xsl:apply-templates select="@initial"/>
        <xsl:apply-templates select="@maximum"/>
    </xsl:template>

</xsl:stylesheet>
