<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:bcl="http://baxter-it.com/config/log"
    xmlns:c="http://baxter-it.com/config/component"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs bcl c" version="2.0">

    <xsl:output encoding="UTF-8" method="xml" />
    <xsl:param name="configurationComponentId"/>
    
    
    <xsl:template match="bcl:configuration">
        <configuration>
            <xsl:apply-templates select="bcl:console-appender[c:component[@id=$configurationComponentId]]"/>
            <xsl:apply-templates select="bcl:file-appender[c:component[@id=$configurationComponentId]]"/>
            <xsl:apply-templates select="bcl:rolling-file-appender[c:component[@id=$configurationComponentId]]"/>
            <xsl:apply-templates select="bcl:logger[c:component[@id=$configurationComponentId]]"/>
        </configuration>
    </xsl:template>

    <xsl:template match="bcl:logger[@name='ROOT']">
        <root>
            <xsl:apply-templates select="@level"/>
            <xsl:apply-templates select="bcl:appender-ref"/>
        </root>
    </xsl:template>

    <xsl:template match="bcl:logger/@level">
        <xsl:attribute name="level">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="bcl:appender-ref">
        <appender-ref>
            <xsl:attribute name="ref">
                <xsl:value-of select="@ref"/>
            </xsl:attribute>
        </appender-ref>
    </xsl:template>

    <xsl:template match="bcl:logger">
        <logger>
            <xsl:apply-templates select="@name"/>
            <xsl:apply-templates select="@additivity"/>
            <xsl:apply-templates select="@level"/>
            <xsl:apply-templates select="bcl:appender-ref"/>
        </logger>
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

    <xsl:template match="bcl:console-appender">
        <appender class="ch.qos.logback.core.ConsoleAppender">
            <xsl:attribute name="name">
                <xsl:value-of select="@name"/>
            </xsl:attribute>

            <xsl:apply-templates select="bcl:layout"/>
        </appender>
    </xsl:template>

    <xsl:template match="bcl:layout">
        <encoder>
            <pattern>
                <xsl:value-of select="."/>
            </pattern>
        </encoder>
    </xsl:template>

    <xsl:template match="bcl:file-appender">
        <appender class="ch.qos.logback.core.FileAppender">
            <xsl:apply-templates select="@name"/>
            <xsl:apply-templates select="@file"/>
            <xsl:apply-templates />
        </appender>
    </xsl:template>

    <xsl:template match="bcl:append">
        <append>
            <xsl:value-of select="."/>
        </append>
    </xsl:template>

    <xsl:template match="bcl:rolling-file-appender">
        <appender class="ch.qos.logback.core.rolling.RollingFileAppender">
            <xsl:apply-templates select="@name"/>
            <xsl:apply-templates select="@file"/>
            <xsl:apply-templates select="@backupIndex"/>
            <xsl:apply-templates select="@maxSize"/>
            <xsl:apply-templates />
        </appender>
    </xsl:template>

    <xsl:template match="bcl:file-appender/@name | bcl:rolling-file-appender/@name">
        <xsl:attribute name="name">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="bcl:rolling-file-appender/@maxSize">
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <maxFileSize>
                <xsl:value-of select="."/>
            </maxFileSize>
        </triggeringPolicy>
    </xsl:template>

    <xsl:template match="bcl:rolling-file-appender/@backupIndex">
        <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
            <fileNamePattern>
                <xsl:call-template name="build-log-directory-name"/>
                <xsl:value-of select="../@file"/>
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
    
    <xsl:template match="bcl:rolling-file-appender/@file | bcl:file-appender/@file">
        <file>
            <xsl:call-template name="build-log-directory-name"/>
            <xsl:value-of select="."/>
        </file>
    </xsl:template>


</xsl:stylesheet>
