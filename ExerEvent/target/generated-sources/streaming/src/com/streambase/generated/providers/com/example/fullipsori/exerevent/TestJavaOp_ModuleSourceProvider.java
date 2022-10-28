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

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"module\"/>\n            <param name=\"full");
        sb.append("y-qualified-name\" value=\"com.example.fullipsori.exerevent.TestJavaOp\"/>\n        ");
        sb.append("</type-metadata>\n        <memory-model-settings/>\n        <dynamic-variables/>\n ");
        sb.append("       <stream name=\"InputStreamCopy\">\n            <schema>\n                <fie");
        sb.append("ld name=\"filePath\" type=\"string\"/>\n                <field name=\"time\" type=\"time");
        sb.append("stamp\"/>\n            </schema>\n        </stream>\n        <box name=\"Java\" type=\"");
        sb.append("java\">\n            <input port=\"1\" stream=\"InputStreamCopy\"/>\n            <outpu");
        sb.append("t port=\"1\" stream=\"out:Java_1\"/>\n            <param name=\"start:state\" value=\"tr");
        sb.append("ue\"/>\n            <param name=\"javaclass\" value=\"com.example.fullipsori.exereven");
        sb.append("t.TestFile\"/>\n        </box>\n        <box name=\"Map2\" type=\"map\">\n            <i");
        sb.append("nput port=\"1\" stream=\"out:Java_1\"/>\n            <output port=\"1\" stream=\"OutputS");
        sb.append("tream\"/>\n            <target-list>\n                <item name=\"input\" selection=");
        sb.append("\"none\"/>\n                <expressions>\n                    <include field=\"elaps");
        sb.append("ed\">to_milliseconds(now())-to_milliseconds(time)</include>\n                    <");
        sb.append("include field=\"result_data\">string(input1.result_data)</include>\n               ");
        sb.append("     <include field=\"timestamp\">input1.time</include>\n                </expressi");
        sb.append("ons>\n            </target-list>\n        </box>\n        <output-stream name=\"Outp");
        sb.append("utStream\"/>\n    </add>\n</modify>\n");
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
