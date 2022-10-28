/*

	Class

*/
package com.streambase.generated.providers.com.autoever.poc.maineventflow;

public class HttpClientSchema_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public HttpClientSchema_ModuleSourceProvider() {
        super("MainEventFlow", "62AA00D73FB516046E5E347897F63BF5");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"interface\"/>\n            <param name=\"f");
        sb.append("ully-qualified-name\" value=\"com.autoever.poc.maineventflow.HttpClientSchema\"/>\n ");
        sb.append("       </type-metadata>\n        <memory-model-settings/>\n        <named-schemas>");
        sb.append("\n            <schema name=\"HttpClientControlSchema\">\n                <field name");
        sb.append("=\"command\" type=\"string\"/>\n                <field name=\"url\" type=\"string\"/>\n   ");
        sb.append("             <field name=\"data\" type=\"list\">\n                    <element-type t");
        sb.append("ype=\"tuple\">\n                        <schema>\n                            <field");
        sb.append(" name=\"text\" type=\"string\"/>\n                            <field name=\"binary\" ty");
        sb.append("pe=\"blob\"/>\n                            <field name=\"filePath\" type=\"string\"/>\n ");
        sb.append("                           <field name=\"mediaType\" type=\"string\"/>\n             ");
        sb.append("               <field name=\"name\" type=\"string\"/>\n                            <f");
        sb.append("ield name=\"urlEncode\" type=\"bool\"/>\n                            <field name=\"url");
        sb.append("EncodeType\" type=\"string\"/>\n                        </schema>\n                  ");
        sb.append("  </element-type>\n                </field>\n                <field name=\"headers\"");
        sb.append(" type=\"list\">\n                    <element-type type=\"tuple\">\n                  ");
        sb.append("      <schema>\n                            <field name=\"name\" type=\"string\"/>\n  ");
        sb.append("                          <field name=\"values\" type=\"list\">\n                    ");
        sb.append("            <element-type type=\"string\"/>\n                            </field>\n ");
        sb.append("                       </schema>\n                    </element-type>\n           ");
        sb.append("     </field>\n                <field name=\"queryParameters\" type=\"list\">\n       ");
        sb.append("             <element-type type=\"tuple\">\n                        <schema>\n      ");
        sb.append("                      <field name=\"name\" type=\"string\"/>\n                       ");
        sb.append("     <field name=\"value\" type=\"string\"/>\n                            <field name");
        sb.append("=\"urlEncode\" type=\"bool\"/>\n                            <field name=\"urlEncodeTyp");
        sb.append("e\" type=\"string\"/>\n                        </schema>\n                    </eleme");
        sb.append("nt-type>\n                </field>\n                <field name=\"cookies\" type=\"li");
        sb.append("st\">\n                    <element-type type=\"tuple\">\n                        <sc");
        sb.append("hema>\n                            <field name=\"name\" type=\"string\"/>\n           ");
        sb.append("                 <field name=\"value\" type=\"string\"/>\n                        </s");
        sb.append("chema>\n                    </element-type>\n                </field>\n            ");
        sb.append("    <field name=\"settings\" type=\"tuple\">\n                    <schema>\n          ");
        sb.append("              <field name=\"requestMethod\" type=\"string\"/>\n                      ");
        sb.append("  <field name=\"requestType\" type=\"string\"/>\n                        <field name=");
        sb.append("\"downloadPath\" type=\"string\"/>\n                        <field name=\"asynchronous");
        sb.append("\" type=\"bool\"/>\n                        <field name=\"proxy\" type=\"tuple\">\n      ");
        sb.append("                      <schema>\n                                <field name=\"useP");
        sb.append("roxy\" type=\"bool\"/>\n                                <field name=\"host\" type=\"str");
        sb.append("ing\"/>\n                                <field name=\"port\" type=\"int\"/>\n         ");
        sb.append("                       <field name=\"user\" type=\"string\"/>\n                      ");
        sb.append("          <field name=\"pass\" type=\"string\"/>\n                            </schem");
        sb.append("a>\n                        </field>\n                        <field name=\"advance");
        sb.append("d\" type=\"tuple\">\n                            <schema>\n                          ");
        sb.append("      <field name=\"connectTimeout\" type=\"int\"/>\n                                ");
        sb.append("<field name=\"readTimeout\" type=\"int\"/>\n                                <field na");
        sb.append("me=\"reconnectTimeout\" type=\"int\"/>\n                                <field name=\"");
        sb.append("pollFrequency\" type=\"int\"/>\n                                <field name=\"ignoreC");
        sb.append("ertificateErrors\" type=\"bool\"/>\n                            </schema>\n          ");
        sb.append("              </field>\n                        <field name=\"auth\" type=\"tuple\">\n");
        sb.append("                            <schema>\n                                <field name");
        sb.append("=\"authType\" type=\"string\"/>\n                                <field name=\"user\" t");
        sb.append("ype=\"string\"/>\n                                <field name=\"pass\" type=\"string\"/");
        sb.append(">\n                            </schema>\n                        </field>\n       ");
        sb.append("                 <field name=\"output\" type=\"tuple\">\n                            ");
        sb.append("<schema>\n                                <field name=\"outputTuplePerLine\" type=\"");
        sb.append("bool\"/>\n                                <field name=\"outputBlankLines\" type=\"boo");
        sb.append("l\"/>\n                                <field name=\"outputNullTupleOnCompletion\" t");
        sb.append("ype=\"bool\"/>\n                                <field name=\"outputConnectingStatus");
        sb.append("\" type=\"bool\"/>\n                                <field name=\"useDefaultCharset\" ");
        sb.append("type=\"bool\"/>\n                                <field name=\"charset\" type=\"string");
        sb.append("\"/>\n                                <field name=\"urlDecode\" type=\"bool\"/>\n      ");
        sb.append("                          <field name=\"urlDecodeType\" type=\"string\"/>\n          ");
        sb.append("                  </schema>\n                        </field>\n                   ");
        sb.append(" </schema>\n                </field>\n            </schema>\n            <schema na");
        sb.append("me=\"HttpClientDataSchema\">\n                <field name=\"text\" type=\"string\"/>\n  ");
        sb.append("              <field name=\"binary\" type=\"blob\"/>\n                <field name=\"fi");
        sb.append("lePath\" type=\"string\"/>\n                <field name=\"mediaType\" type=\"string\"/>\n");
        sb.append("                <field name=\"name\" type=\"string\"/>\n                <field name=\"");
        sb.append("urlEncode\" type=\"bool\"/>\n                <field name=\"urlEncodeType\" type=\"strin");
        sb.append("g\"/>\n            </schema>\n        </named-schemas>\n        <dynamic-variables/>");
        sb.append("\n    </add>\n</modify>\n");
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

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.autoever.poc.maineventflow.HttpClientSchema_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.autoever.poc.maineventflow.HttpClientSchema_ModuleSourceProvider.makeExports();
}
