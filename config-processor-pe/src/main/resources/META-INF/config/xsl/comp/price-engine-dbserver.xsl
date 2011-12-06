<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:t="urn:templates"
    xmlns:pedbs="http://baxter-it.com/config/pe/dbserver"
    xmlns:pedb="http://baxter-it.com/config/pe/db" xmlns="http://baxter-it.com/config/pe/properties"
    exclude-result-prefixes="xs t pedb pedbs" version="2.0">

    <xsl:import href="../imp/jdbc.xsl"/>

    <xsl:template match="t:price-engine-dbserver" mode="component-specific-sources">
        <xsl:call-template name="load-merged-repo-document">
            <xsl:with-param name="prefix" select="'comp/dbserver'"/>
        </xsl:call-template>
        <xsl:call-template name="add-jdbc-config-to-root" />
    </xsl:template>

    <xsl:template match="t:price-engine-dbserver" mode="component-specific">
        <xsl:param name="root"/>
        <xsl:apply-templates select="$root/pedbs:configuration/*"/>
        <group key="dbConnection">
            <xsl:apply-templates
                select="$root/pedb:configuration/pedb:connection[@id='dbConnection']" mode="entries-only"/>
            <xsl:apply-templates
                select="$root/pedb:configuration/pedb:connection[@id='dbConnection2']" mode="entries-only">
                <xsl:with-param name="suffix">2</xsl:with-param>
            </xsl:apply-templates>
        </group>
    </xsl:template>

    <xsl:template match="pedbs:toffile">
        <group key="toffile">
            <xsl:apply-templates select="@*"/>
        </group>
    </xsl:template>

    <xsl:template match="pedbs:deleteLogins | pedbs:dbUDPPort">
        <entry>
            <xsl:attribute name="key">
                <xsl:value-of select="local-name()"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </entry>
    </xsl:template>

    <xsl:template match="pedbs:*/@*[name()!='id']">
        <entry>
            <xsl:attribute name="key">
                <xsl:value-of select="name()"/>
            </xsl:attribute>
            <xsl:value-of select="."/>
        </entry>
    </xsl:template>

    <xsl:template match="pedbs:ldap/pedbs:connection">
        <group>
            <xsl:attribute name="key">
                <xsl:value-of select="@id"/>
            </xsl:attribute>
            <xsl:apply-templates select="@*"/>
        </group>
    </xsl:template>

    <xsl:template match="pedbs:ldap">
        <group key="ldapConnection">
            <entry key="loginModule">
                <xsl:value-of select="@loginModule"/>
            </entry>
            <entry key="controlFlag">
                <xsl:value-of select="@controlFlag"/>
            </entry>
            <xsl:apply-templates/>
        </group>
    </xsl:template>

</xsl:stylesheet>
