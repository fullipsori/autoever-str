/*

	Class

*/
package com.streambase.generated.providers.com.autoever.poc.maineventflow;

public class DataParser_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public DataParser_ModuleSourceProvider() {
        super("MainEventFlow", "156CE3005102C1890130E49B19136F4E");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"interface\"/>\n            <param name=\"f");
        sb.append("ully-qualified-name\" value=\"com.autoever.poc.maineventflow.DataParser\"/>\n       ");
        sb.append(" </type-metadata>\n        <memory-model-settings/>\n        <named-schemas>\n     ");
        sb.append("       <schema name=\"GPSParserDataSchema\">\n                <field name=\"realTime");
        sb.append("\" type=\"long\"/>\n                <field name=\"RealTime\" type=\"string\"/>\n         ");
        sb.append("       <field name=\"DataFlag\" type=\"int\"/>\n                <field name=\"DataChan");
        sb.append("nel\" type=\"int\"/>\n                <field name=\"DataID\" type=\"int\"/>\n            ");
        sb.append("    <field name=\"Latitude\" type=\"double\"/>\n                <field name=\"Longitud");
        sb.append("e\" type=\"double\"/>\n                <field name=\"Heading\" type=\"int\"/>\n          ");
        sb.append("      <field name=\"Velocity\" type=\"double\"/>\n                <field name=\"Altitu");
        sb.append("de\" type=\"double\"/>\n                <field name=\"NS\" type=\"string\"/>\n           ");
        sb.append("     <field name=\"EW\" type=\"string\"/>\n            </schema>\n            <schema ");
        sb.append("name=\"GPSParserSchema\">\n                <field name=\"messageID\" type=\"string\"/>\n");
        sb.append("                <field name=\"dataList\" type=\"list\">\n                    <element");
        sb.append("-type type=\"tuple\" value=\"GPSParserDataSchema\"/>\n                </field>\n      ");
        sb.append("      </schema>\n        </named-schemas>\n        <dynamic-variables/>\n    </add>");
        sb.append("\n</modify>\n");
        return sb.toString();
    }

    public java.lang.String getXMLAppIr() {
        return XML_APP_IR;
    }

    public static com.streambase.sb.build.ModuleCGExports makeExports() {
        com.streambase.sb.build.ModuleCGExports exports = new com.streambase.sb.build.ModuleCGExports();

        return exports;
    }

    public com.streambase.sb.build.ModuleCGExports getModuleCGExports() {
        return MODULE_CG_EXPORTS;
    }

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.autoever.poc.maineventflow.DataParser_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.autoever.poc.maineventflow.DataParser_ModuleSourceProvider.makeExports();
}
