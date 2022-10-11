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

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<modify version=\"11.0.0_");
        sb.append("248f263d973a774f84731121e9d4527c932d77fd\">\r\n    <add>\r\n        <annotations>\r\n  ");
        sb.append("          <annotation name=\"hygienic\"/>\r\n        </annotations>\r\n        <type-m");
        sb.append("etadata>\r\n            <param name=\"type\" value=\"interface\"/>\r\n            <param");
        sb.append(" name=\"fully-qualified-name\" value=\"com.example.fullipsori.exerevent.TestTable\"/");
        sb.append(">\r\n        </type-metadata>\r\n        <memory-model-settings/>\r\n        <dynamic-");
        sb.append("variables/>\r\n        <table-schemas>\r\n            <table-schema name=\"testTable\"");
        sb.append(">\r\n                <schema>\r\n                    <field name=\"keyId\" type=\"int\"/");
        sb.append(">\r\n                    <field name=\"devId\" type=\"string\"/>\r\n                    ");
        sb.append("<field name=\"value\" type=\"string\"/>\r\n                </schema>\r\n                ");
        sb.append("<primary-index type=\"btree\">\r\n                    <field name=\"keyId\"/>\r\n       ");
        sb.append("         </primary-index>\r\n                <index type=\"btree\">\r\n               ");
        sb.append("     <field name=\"devId\"/>\r\n                </index>\r\n            </table-schema");
        sb.append(">\r\n        </table-schemas>\r\n    </add>\r\n</modify>\r\n");
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
