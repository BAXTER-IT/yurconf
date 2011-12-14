<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:cidt="http://baxter-it.com/config/component/id-template"
    xmlns:jaas="http://baxter-it.com/price-engine/conf/jaas"
    xmlns:db="http://baxter-it.com/price-engine/conf/db" xmlns="http://baxter-it.com/price-engine/conf/properties"
    exclude-result-prefixes="xs cidt db jaas" version="2.0">

    <xsl:import href="../db.xsl" />

    <xsl:template match="cidt:price-engine-dbserver">
        <group key="dbConnection">
            <xsl:apply-templates select="/db:configuration/db:connection[@id='dbConnection']" mode="entries-only" />
            <xsl:apply-templates select="/db:configuration/db:connection[@id='dbConnection2']" mode="entries-only">
                <xsl:with-param name="suffix">2</xsl:with-param>
            </xsl:apply-templates>
        </group>
        <xsl:apply-templates select="/jaas:configuration/jaas:loginModule[@id='peLDAP']"/>
    </xsl:template>

    <xsl:template match="jaas:connection">
        <group>
            <xsl:attribute name="key">
                <xsl:value-of select="@id" />
            </xsl:attribute>
            <xsl:apply-templates select="@*" />
        </group>
    </xsl:template>

    <xsl:template match="jaas:loginModule">
        <group key="ldapConnection">
            <entry key="loginModule">
                <xsl:value-of select="@class" />
            </entry>
            <entry key="controlFlag">
                <xsl:value-of select="@controlFlag" />
            </entry>
            <xsl:apply-templates select="*"/>
        </group>
    </xsl:template>

</xsl:stylesheet>
