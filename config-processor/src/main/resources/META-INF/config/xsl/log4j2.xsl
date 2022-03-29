<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:bcl="http://baxter-it.com/config/log" xmlns:c="http://baxter-it.com/config/component"
	xmlns:conf="http://baxter-it.com/config" xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="xs bcl c conf" version="2.0">

	<xsl:import href="repo-base.xsl" />
	<xsl:import href="conf-reference.xsl" />

	<xsl:output encoding="UTF-8" method="xml" />

	<xsl:param name="configurationComponentId" />

	<xsl:template match="/">
		<xsl:variable name="root">
			<xsl:copy-of select="conf:configuration-source/conf:request" />
			<xsl:apply-templates select="conf:configuration-source/conf:request"
				mode="load-document-with-variants">
				<xsl:with-param name="prefix" select="'log'" />
			</xsl:apply-templates>
		</xsl:variable>
		<xsl:apply-templates select="$root/bcl:configuration" />
	</xsl:template>
	<xsl:template match="bcl:configuration">
		<Configuration status="info">
			<xsl:variable name="refs"
				select="bcl:logger[c:component[@id=$configurationComponentId]]/bcl:appender-ref/@ref" />
			<Appenders>
				<xsl:apply-templates
					select="(bcl:console-appender | bcl:file-appender | bcl:rolling-file-appender | bcl:generic-appender)[@name=$refs or c:component/@id=$configurationComponentId]" />
			</Appenders>
			<Loggers>
				<xsl:apply-templates
					select="bcl:logger[c:component[@id=$configurationComponentId]]" />
			</Loggers>
		</Configuration>
	</xsl:template>

	<xsl:template match="c:component" mode="deep-copy-generic">
	</xsl:template>
	<xsl:template match="*" mode="deep-copy-generic">
		<xsl:param name="name" select="name()" />
		<xsl:element name="{$name}">
			<xsl:copy-of select="@*[name() != 'appenderType']" />
			<xsl:apply-templates select="*" mode="deep-copy-generic" />
			<xsl:if test="text()">
				<xsl:value-of select="text()" />
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<xsl:template match="bcl:generic-appender">
		<xsl:comment>
			<xsl:text>Using of generic appenders in log configuration source. </xsl:text>
			<xsl:text>This is not portable across the different logging frameworks. </xsl:text>
			<xsl:text>Use it on your own risk. </xsl:text>
		</xsl:comment>
		<xsl:apply-templates select="." mode="deep-copy-generic">
			<xsl:with-param name="name" select="@appenderType" />
		</xsl:apply-templates>
	</xsl:template>
	<xsl:template match="bcl:logger[@name='ROOT']">
		<Root>
			<xsl:apply-templates select="@level" />
			<xsl:apply-templates select="bcl:appender-ref" />
		</Root>
	</xsl:template>
	<xsl:template match="bcl:logger/@level">
		<xsl:attribute name="level">
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="bcl:appender-ref">
		<AppenderRef>
			<xsl:attribute name="ref">
				<xsl:value-of select="@ref" />
			</xsl:attribute>
		</AppenderRef>
	</xsl:template>
	<xsl:template match="bcl:logger">
		<Logger>
			<xsl:apply-templates select="@name" />
			<xsl:apply-templates select="@additivity" />
			<xsl:apply-templates select="@level" />
			<xsl:apply-templates select="bcl:appender-ref" />
		</Logger>
	</xsl:template>
	<xsl:template match="bcl:logger/@name">
		<xsl:attribute name="name">
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="bcl:logger/@additivity">
		<xsl:attribute name="additivity">
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="bcl:console-appender">
		<Console>
			<xsl:attribute name="name">
				<xsl:value-of select="@name" />
			</xsl:attribute>
			<xsl:apply-templates select="bcl:layout" />
		</Console>
	</xsl:template>
	<xsl:template match="bcl:layout">
		<PatternLayout>
			<xsl:attribute name="pattern">
				<xsl:value-of select="." />
			</xsl:attribute>
		</PatternLayout>
	</xsl:template>

	<xsl:template match="bcl:file-appender">
		<File>
			<xsl:apply-templates select="@name" />
			<xsl:call-template name="log-file" />
			<xsl:apply-templates select="@append" />
			<xsl:apply-templates />
		</File>
	</xsl:template>

	<xsl:template match="bcl:file-appender/@append">
		<xsl:attribute name="append">
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="bcl:rolling-file-appender">
		<RollingFile>
			<xsl:apply-templates select="@name" />
			<xsl:call-template name="log-file" />
			<xsl:apply-templates select="@backupIndex" />
			<xsl:call-template name="policies" />
			<xsl:apply-templates />
		</RollingFile>
	</xsl:template>

	<xsl:template match="bcl:file-appender/@name | bcl:rolling-file-appender/@name">
		<xsl:attribute name="name">
			<xsl:value-of select="." />
		</xsl:attribute>
	</xsl:template>

	<xsl:template name="policies">
		<Policies>
			<xsl:apply-templates select="@maxSize" />
			<xsl:choose>
				<xsl:when test="not(empty(@interval))">
					<xsl:apply-templates select="@minSize" />
				</xsl:when>
			</xsl:choose>
			<xsl:choose>
				<xsl:when test="not(empty(@interval))">
					<xsl:call-template name="timebased">
						<xsl:with-param name="interval" select="@interval" />
						<xsl:with-param name="modulate" select="@modulate" />
					</xsl:call-template>
				</xsl:when>
			</xsl:choose>
		</Policies>
	</xsl:template>

	<xsl:template name="timebased">
		<xsl:param name="interval" />
		<xsl:param name="modulate" />
		<xsl:choose>
			<xsl:when test="not(empty($modulate))">
				<TimeBasedTriggeringPolicy>
					<xsl:attribute name="interval">
						<xsl:value-of select="$interval" />
					</xsl:attribute>
					<xsl:attribute name="modulate">
						<xsl:value-of select="$modulate" />
					</xsl:attribute>
				</TimeBasedTriggeringPolicy>
			</xsl:when>
			<xsl:otherwise>
				<TimeBasedTriggeringPolicy>
					<xsl:attribute name="interval">
						<xsl:value-of select="$interval" />
					</xsl:attribute>
				</TimeBasedTriggeringPolicy>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="bcl:rolling-file-appender/@maxSize">
		<SizeBasedTriggeringPolicy>
			<xsl:attribute name="size">
				<xsl:value-of select="." />
			</xsl:attribute>
		</SizeBasedTriggeringPolicy>
	</xsl:template>

	<xsl:template match="bcl:rolling-file-appender/@minSize">
		<OnStartupTriggeringPolicy>
			<xsl:attribute name="minSize">
				<xsl:value-of select="." />
			</xsl:attribute>
		</OnStartupTriggeringPolicy>
	</xsl:template>

	<xsl:template match="bcl:rolling-file-appender/@backupIndex">
		<xsl:attribute name="filePattern">
			<xsl:call-template name="build-log-file-path">
				<xsl:with-param name="file" select="../@file" />
			</xsl:call-template>
			<xsl:text>-%i</xsl:text>
		</xsl:attribute>
		<DefaultRolloverStrategy>
			<xsl:attribute name="max">
				<xsl:value-of select="." />
			</xsl:attribute>
		</DefaultRolloverStrategy>
	</xsl:template>

	<xsl:template name="log-file">
		<xsl:attribute name="fileName">
			<xsl:call-template name="build-log-file-path">
				<xsl:with-param name="file" select="@file" />
			</xsl:call-template>
		</xsl:attribute>
	</xsl:template>

	<xsl:template name="build-log-file-path">
		<xsl:param name="file" select="'DEFAULT'" />
		<xsl:choose>
			<xsl:when test="empty($file)">
				<xsl:value-of select="$configurationComponentId" />
				<xsl:text>.log</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$file" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
