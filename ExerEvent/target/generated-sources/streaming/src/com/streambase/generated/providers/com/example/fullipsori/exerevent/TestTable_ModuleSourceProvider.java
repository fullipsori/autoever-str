/*

	Class

*/
package com.streambase.generated.providers.com.example.fullipsori.exerevent;

public class TestTable_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public TestTable_ModuleSourceProvider() {
        super("ExerEvent", "3A230C9CB41FB142B004291908B456C6");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"interface\"/>\n            <param name=\"f");
        sb.append("ully-qualified-name\" value=\"com.example.fullipsori.exerevent.TestTable\"/>\n      ");
        sb.append("  </type-metadata>\n        <memory-model-settings/>\n        <dynamic-variables/>");
        sb.append("\n        <table-schemas>\n            <table-schema name=\"testTable\">\n           ");
        sb.append("     <schema>\n                    <field name=\"keyId\" type=\"int\"/>\n             ");
        sb.append("       <field name=\"devId\" type=\"string\"/>\n                    <field name=\"valu");
        sb.append("e\" type=\"string\"/>\n                </schema>\n                <primary-index type");
        sb.append("=\"btree\">\n                    <field name=\"keyId\"/>\n                </primary-in");
        sb.append("dex>\n                <index type=\"btree\">\n                    <field name=\"devId");
        sb.append("\"/>\n                </index>\n            </table-schema>\n        </table-schemas");
        sb.append(">\n    </add>\n</modify>\n");
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

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.example.fullipsori.exerevent.TestTable_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.example.fullipsori.exerevent.TestTable_ModuleSourceProvider.makeExports();
}
