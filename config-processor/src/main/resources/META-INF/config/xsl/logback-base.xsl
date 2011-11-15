<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs" version="2.0">

    <xsl:output encoding="UTF-8" method="xml" doctype-system="logback.dtd"/>

    <xsl:template match="configuration">
        <configuration>
            <xsl:apply-templates select="*"/>
        </configuration>
    </xsl:template>

    <xsl:template match="logger[@name='ROOT']">
        <root>
            <xsl:apply-templates select="@level"/>
            <xsl:apply-templates select="appender-ref"/>
        </root>
    </xsl:template>

    <xsl:template match="logger/@level">
        <xsl:attribute name="level">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="appender-ref">
        <appender-ref>
            <xsl:attribute name="ref">
                <xsl:value-of select="@ref"/>
            </xsl:attribute>
        </appender-ref>
    </xsl:template>

    <xsl:template match="logger">
        <logger>
            <xsl:apply-templates select="@name"/>
            <xsl:apply-templates select="@additivity"/>
            <xsl:apply-templates select="@level"/>
            <xsl:apply-templates select="appender-ref"/>
        </logger>
    </xsl:template>

    <xsl:template match="logger/@name">
        <xsl:attribute name="name">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="logger/@additivity">
        <xsl:attribute name="additivity">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="console-appender">
        <appender class="ch.qos.logback.core.ConsoleAppender">
            <xsl:attribute name="name">
                <xsl:value-of select="@name"/>
            </xsl:attribute>

            <xsl:apply-templates select="layout"/>
        </appender>
    </xsl:template>

    <xsl:template match="layout">
        <encoder>
            <pattern>
                <xsl:value-of select="."/>
            </pattern>
        </encoder>
    </xsl:template>

    <xsl:template match="file-appender">
        <appender class="ch.qos.logback.core.FileAppender">
            <xsl:apply-templates select="@name"/>
            <xsl:apply-templates select="*"/>
        </appender>
    </xsl:template>

    <xsl:template match="append">
        <append>
            <xsl:value-of select="."/>
        </append>
    </xsl:template>

    <xsl:template match="rolling-file-appender">
        <appender class="ch.qos.logback.core.rolling.RollingFileAppender">
            <xsl:apply-templates select="@name"/>
            <xsl:apply-templates select="*"/>
        </appender>
    </xsl:template>

    <xsl:template match="file-appender/@name | rolling-file-appender/@name">
        <xsl:attribute name="name">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="maxSize">
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <maxFileSize>
                <xsl:value-of select="."/>
            </maxFileSize>
        </triggeringPolicy>
    </xsl:template>

    <xsl:template match="backupIndex">
        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <fileNamePattern>
                <xsl:value-of select="../fileName"/>
                <xsl:text>-%i</xsl:text>
            </fileNamePattern>
            <minIndex>
                <xsl:text>1</xsl:text>
            </minIndex>
            <maxIndex>
                <xsl:value-of select="."/>
            </maxIndex>
        </rollingPolicy>
    </xsl:template>
    
    <xsl:template name="build-log-directory-name">
        <xsl:text>--NOT IMPLEMENTED--</xsl:text>
    </xsl:template>
    
    <xsl:template match="fileName">
        <file>
            <xsl:call-template name="build-log-directory-name"/>
            <xsl:value-of select="."/>
        </file>
    </xsl:template>


</xsl:stylesheet>
