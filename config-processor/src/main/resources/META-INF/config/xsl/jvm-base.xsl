<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:j="http://baxter-it.com/config/jvm" xmlns:c="http://baxter-it.com/config/component"
    xmlns:conf="http://baxter-it.com/config" exclude-result-prefixes="xs c j conf" version="2.0">

    <xsl:import href="baxterxsl:text-fmt.xsl" />
    <xsl:import href="baxterxsl:conf-reference.xsl" />

    <xsl:output encoding="UTF-8" method="text" />

    <xsl:param name="configurationComponentId" />

    <xsl:template match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='windows']"
        mode="append-opt">
        <xsl:param name="opt" />
        <xsl:text>set JAVA_OPTS=%JAVA_OPTS% </xsl:text>
        <xsl:text>"</xsl:text>
        <xsl:value-of select="$opt" />
        <xsl:text>"</xsl:text>
        <xsl:call-template name="CRLF" />
    </xsl:template>

    <xsl:template match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='unix']" mode="append-opt">
        <xsl:param name="opt" />
        <xsl:text>JAVA_OPTS="$JAVA_OPTS </xsl:text>
        <xsl:value-of select="$opt" />
        <xsl:text>"</xsl:text>
        <xsl:call-template name="LF" />
    </xsl:template>

    <xsl:template name="build-jvm-options">
        <xsl:apply-templates select="j:debug/c:component[@id=$configurationComponentId]/j:port" />
        <xsl:apply-templates select="j:logging[c:component[@id=$configurationComponentId]]" />
        <xsl:apply-templates select="j:option[c:component[@id=$configurationComponentId]]" />
        <xsl:apply-templates select="j:heap[c:component[@id=$configurationComponentId]]" />
        <xsl:apply-templates select="j:gc[c:component[@id=$configurationComponentId]]" />
        <xsl:apply-templates select="j:gclog[c:component[@id=$configurationComponentId]]" />
        <xsl:apply-templates select="j:jmx/c:component[@id=$configurationComponentId]/j:port" />
        <xsl:apply-templates select="j:system[c:component[@id=$configurationComponentId]]" />
        <xsl:call-template name="conf-opts" />
    </xsl:template>

    <xsl:template name="conf-opts">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dcom.baxter.config.restUrl=</xsl:text>
                <xsl:value-of select="/conf:request/@base" />
            </xsl:with-param>
        </xsl:apply-templates>
        <xsl:if test="/conf:request/conf:variant">
            <xsl:apply-templates select="/j:configuration" mode="append-opt">
                <xsl:with-param name="opt">
                    <xsl:text>-Dcom.baxter.config.variants=</xsl:text>
                    <xsl:value-of select="/conf:request/conf:variant/@id" separator="," />
                </xsl:with-param>
            </xsl:apply-templates>
        </xsl:if>
    </xsl:template>

    <xsl:template match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='windows']"
        mode="script-end"> </xsl:template>

    <xsl:template match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='windows']"
        mode="script-start">
        <xsl:text>rem JVM Parameters for Windows</xsl:text>
        <xsl:call-template name="CRLF" />
        <xsl:call-template name="build-jvm-options" />
    </xsl:template>

    <xsl:template match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='unix']"
        mode="script-start">
        <xsl:text>#!/bin/bash</xsl:text>
        <xsl:call-template name="LF" />
        <xsl:text># JVM Parameters for Unix</xsl:text>
        <xsl:call-template name="LF" />
        <xsl:call-template name="build-jvm-options" />
    </xsl:template>

    <xsl:template match="j:configuration[/conf:request/conf:parameter[@id='osfamily']/text()='unix']" mode="script-end">
        <xsl:text>export JAVA_OPTS</xsl:text>
        <xsl:call-template name="LF" />
        <xsl:call-template name="LF" />
    </xsl:template>

    <xsl:template match="j:debug/c:component/j:port">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-agentlib:jdwp=</xsl:text>
                <xsl:text>transport=dt_socket</xsl:text>
                <!-- Address host:port -->
                <xsl:text>,address=</xsl:text>
                <xsl:if test="../../@listenHost">
                    <xsl:value-of select="../../@listenHost" />
                    <xsl:text>:</xsl:text>
                </xsl:if>
                <xsl:value-of select="." />
                <!-- Allow debugger to attach -->
                <xsl:text>,server=y</xsl:text>
                <!-- Do suspend ? by default yes -->
                <xsl:choose>
                    <xsl:when test="../../@suspend = 'true'">
                        <xsl:text>,suspend=y</xsl:text>
                    </xsl:when>
                    <xsl:when test="../../@suspend = 'false'">
                        <xsl:text>,suspend=n</xsl:text>
                    </xsl:when>
                </xsl:choose>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:logging[@framework='log4j']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dlog4j.configuration=</xsl:text>
                <xsl:apply-templates select="conf:reference" mode="url" />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:logging[@framework='logback']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dlogback.configurationFile=</xsl:text>
                <xsl:apply-templates select="conf:reference" mode="url" />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system[@value]">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name" />
                <xsl:text>=</xsl:text>
                <xsl:value-of select="@value" />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system[@param][/conf:request/conf:parameter[@id='osfamily']/text()='unix']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name" />
                <xsl:text>=$</xsl:text>
                <xsl:value-of select="@param" />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system[@var][/conf:request/conf:parameter[@id='osfamily']/text()='unix']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name" />
                <xsl:text>=$</xsl:text>
                <xsl:value-of select="@var" />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system[@param][/conf:request/conf:parameter[@id='osfamily']/text()='windows']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name" />
                <xsl:text>=%</xsl:text>
                <xsl:value-of select="@param" />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system[@var][/conf:request/conf:parameter[@id='osfamily']/text()='windows']">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name" />
                <xsl:text>=%</xsl:text>
                <xsl:value-of select="@var" />
                <xsl:text>%</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system[conf:reference]">
        <xsl:variable name="refUrl">
            <xsl:apply-templates select="conf:reference" mode="url" />
        </xsl:variable>
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name" />
                <xsl:text>=</xsl:text>
                <xsl:value-of select="$refUrl" />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:system">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-D</xsl:text>
                <xsl:value-of select="@name" />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:option">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt" select="@value" />
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:heap/@initial">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Xms</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:heap/@maximum">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Xmx</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:heap/@new">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Xmn</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:heap/@perm">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-XX:PermSize=</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:heap/@maxperm">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-XX:MaxPermSize=</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <!-- Heap sample: <heap initial="16M" maximum="64M" new="120K" perm="128M" 
		maxperm="128M" /> -->
    <xsl:template match="j:heap">
        <xsl:apply-templates select="@initial" />
        <xsl:apply-templates select="@maximum" />
        <xsl:apply-templates select="@new" />
        <xsl:apply-templates select="@perm" />
        <xsl:apply-templates select="@maxperm" />
    </xsl:template>

    <xsl:template match="j:gc[@disableExplicit='true']/@disableExplicit">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-XX:-DisableExplicitGC</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:gc[@type='cms']/@type">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-XX:+UseConcMarkSweepGC</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-XX:+UseParNewGC</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:gclog/@file">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Xloggc:</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:gclog[@rfcDate='false']/@rfcDate">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-XX:-PrintGCTimeStamps</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:gclog[@rfcDate='true']/@rfcDate">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-XX:-PrintGCDateStamps</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <!-- GC sample: <gc disableExplicit="true" type="cms" /> -->
    <xsl:template match="j:gc">
        <xsl:apply-templates select="@disableExplicit" />
        <xsl:apply-templates select="@type" />
    </xsl:template>

    <!-- GC Log sample: <gclog file="/var/log/appgc.log" rfcDate="true" /> -->
    <xsl:template match="j:gclog">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-XX:+PrintGCDetails</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
        <xsl:apply-templates select="@file" />
        <xsl:apply-templates select="@rfcDate" />
    </xsl:template>

    <xsl:template match="j:jmx/@auth">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dcom.sun.management.jmxremote.authenticate=</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="j:jmx/@ssl">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dcom.sun.management.jmxremote.ssl=</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>

    <!-- JMX sample: <jmx auth="false" ssl="false"><c:component id="cd-me"><port>10001</port></c:component></jmx> -->
    <xsl:template match="j:jmx/c:component/j:port">
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dcom.sun.management.jmxremote</xsl:text>
            </xsl:with-param>
        </xsl:apply-templates>
        <xsl:apply-templates select="/j:configuration" mode="append-opt">
            <xsl:with-param name="opt">
                <xsl:text>-Dcom.sun.management.jmxremote.port=</xsl:text>
                <xsl:value-of select="." />
            </xsl:with-param>
        </xsl:apply-templates>
        <xsl:apply-templates select="../../@auth" />
        <xsl:apply-templates select="../../@ssl" />
    </xsl:template>

</xsl:stylesheet>
