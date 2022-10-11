/*

	Class

*/
package com.streambase.generated.providers.com.example.fullipsori.exerevent;

public class TestJavaOp_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public TestJavaOp_ModuleSourceProvider() {
        super("ExerEvent", "DA297181281F3BA28755D2850C331135");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<modify version=\"11.0.0_");
        sb.append("248f263d973a774f84731121e9d4527c932d77fd\">\r\n    <add>\r\n        <annotations>\r\n  ");
        sb.append("          <annotation name=\"hygienic\"/>\r\n        </annotations>\r\n        <type-m");
        sb.append("etadata>\r\n            <param name=\"type\" value=\"module\"/>\r\n            <param na");
        sb.append("me=\"fully-qualified-name\" value=\"com.example.fullipsori.exerevent.TestJavaOp\"/>\r");
        sb.append("\n        </type-metadata>\r\n        <memory-model-settings/>\r\n        <dynamic-va");
        sb.append("riables/>\r\n        <stream name=\"InputStreamCopy\">\r\n            <schema>\r\n      ");
        sb.append("          <field name=\"filePath\" type=\"string\"/>\r\n                <field name=\"t");
        sb.append("ime\" type=\"timestamp\"/>\r\n            </schema>\r\n        </stream>\r\n        <box ");
        sb.append("name=\"Java\" type=\"java\">\r\n            <input port=\"1\" stream=\"InputStreamCopy\"/>");
        sb.append("\r\n            <output port=\"1\" stream=\"out:Java_1\"/>\r\n            <param name=\"s");
        sb.append("tart:state\" value=\"true\"/>\r\n            <param name=\"javaclass\" value=\"com.examp");
        sb.append("le.fullipsori.exerevent.TestFile\"/>\r\n        </box>\r\n        <box name=\"Map2\" ty");
        sb.append("pe=\"map\">\r\n            <input port=\"1\" stream=\"out:Java_1\"/>\r\n            <outpu");
        sb.append("t port=\"1\" stream=\"OutputStream\"/>\r\n            <target-list>\r\n                <");
        sb.append("item name=\"input\" selection=\"none\"/>\r\n                <expressions>\r\n           ");
        sb.append("         <include field=\"elapsed\">to_milliseconds(now())-to_milliseconds(time)</");
        sb.append("include>\r\n                    <include field=\"result_data\">string(input1.result_");
        sb.append("data)</include>\r\n                    <include field=\"timestamp\">input1.time</inc");
        sb.append("lude>\r\n                </expressions>\r\n            </target-list>\r\n        </box");
        sb.append(">\r\n        <output-stream name=\"OutputStream\"/>\r\n    </add>\r\n</modify>\r\n");
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

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.example.fullipsori.exerevent.TestJavaOp_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.example.fullipsori.exerevent.TestJavaOp_ModuleSourceProvider.makeExports();
}
