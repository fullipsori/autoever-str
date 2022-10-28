/*

	Class

*/
package com.streambase.generated.providers.com.example.fullipsori.exerevent;

public class ExerEvent_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public ExerEvent_ModuleSourceProvider() {
        super("ExerEvent", "6BAC14E328B44197987A5FA6B7AB6EC0");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"module\"/>\n            <param name=\"full");
        sb.append("y-qualified-name\" value=\"com.example.fullipsori.exerevent.ExerEvent\"/>\n        <");
        sb.append("/type-metadata>\n        <memory-model-settings/>\n        <implements module-refe");
        sb.append("rence=\"com.example.fullipsori.exerevent.TestTable\"/>\n        <import from=\"com.e");
        sb.append("xample.fullipsori.exerevent.TestTable\"/>\n        <dynamic-variables/>\n        <d");
        sb.append("ata name=\"QueryTable\" shared=\"false\" source=\"concrete\" type=\"querytable\">\n      ");
        sb.append("      <param name=\"storage-method\" value=\"heap\"/>\n            <param name=\"repli");
        sb.append("cated\" value=\"false\"/>\n            <param name=\"table-schema\" value=\"testTable\"/");
        sb.append(">\n            <param name=\"truncate\" value=\"false\"/>\n            <preload format");
        sb.append("=\"csv\" mode=\"empty\"/>\n        </data>\n        <box name=\"InputAdapter\" type=\"inp");
        sb.append("utadapter\">\n            <output port=\"1\" stream=\"out:InputAdapter_1\"/>\n         ");
        sb.append("   <param name=\"start:state\" value=\"true\"/>\n            <param name=\"javaclass\" ");
        sb.append("value=\"com.streambase.sb.adapter.once.Once\"/>\n            <param name=\"OutputFie");
        sb.append("ld\" value=\"time\"/>\n        </box>\n        <box name=\"Map\" type=\"map\">\n          ");
        sb.append("  <input port=\"1\" stream=\"out:InputAdapter_1\"/>\n            <output port=\"1\" str");
        sb.append("eam=\"out:Map_1\"/>\n            <target-list>\n                <item name=\"input\" s");
        sb.append("election=\"none\"/>\n                <expressions>\n                    <include fie");
        sb.append("ld=\"filePath\">\"e:/projects/str/testdata.dat\"</include>\n                    <incl");
        sb.append("ude field=\"time\">input1.time</include>\n                </expressions>\n          ");
        sb.append("  </target-list>\n        </box>\n        <module-reference name=\"TestPythonRef2\">");
        sb.append("\n            <input name=\"InputStream\" port=\"1\" stream=\"out:Map_1\"/>\n           ");
        sb.append(" <output name=\"OutputStream\" port=\"1\" stream=\"out:TestPythonRef2_1\"/>\n          ");
        sb.append("  <param name=\"file\" value=\"com.example.fullipsori.exerevent.TestPython\"/>\n     ");
        sb.append("   </module-reference>\n        <box name=\"Map3\" type=\"map\">\n            <input p");
        sb.append("ort=\"1\" stream=\"out:TestPythonRef2_1\"/>\n            <output port=\"1\" stream=\"out");
        sb.append(":Map3_1\"/>\n            <target-list>\n                <item name=\"input\" selectio");
        sb.append("n=\"all\"/>\n                <expressions>\n                    <include field=\"quer");
        sb.append("yId\">range(1, 1000)</include>\n                </expressions>\n            </targe");
        sb.append("t-list>\n        </box>\n        <box name=\"Iterate2\" type=\"iterate\">\n            ");
        sb.append("<input port=\"1\" stream=\"out:Map3_1\"/>\n            <output port=\"1\" stream=\"out:I");
        sb.append("terate2_1\"/>\n            <param name=\"iterate-expr\" value=\"queryId\"/>\n          ");
        sb.append("  <target-list>\n                <item name=\"input\" selection=\"all\"/>\n           ");
        sb.append("     <expressions>\n                    <include field=\"keyId\">each.element</incl");
        sb.append("ude>\n                </expressions>\n            </target-list>\n        </box>\n  ");
        sb.append("      <box name=\"Map4\" type=\"map\">\n            <input port=\"1\" stream=\"out:Itera");
        sb.append("te2_1\"/>\n            <output port=\"1\" stream=\"out:Map4_1\"/>\n            <target-");
        sb.append("list>\n                <item name=\"input\" selection=\"all\"/>\n                <expr");
        sb.append("essions>\n                    <include field=\"devId\">string(keyId)</include>\n    ");
        sb.append("                <include field=\"value\">string(now())</include>\n                <");
        sb.append("/expressions>\n            </target-list>\n        </box>\n        <box name=\"Query");
        sb.append("\" type=\"query\">\n            <input port=\"1\" stream=\"out:Map4_1\"/>\n            <o");
        sb.append("utput port=\"1\" stream=\"out:Query_1\"/>\n            <dataref id=\"querytable\" name=");
        sb.append("\"QueryTable\"/>\n            <param name=\"operation\" value=\"write\"/>\n            <");
        sb.append("param name=\"where\" value=\"primary-key\"/>\n            <param name=\"key-field.0\" v");
        sb.append("alue=\"keyId\"/>\n            <param name=\"key-value.0\" value=\"input1.keyId\"/>\n    ");
        sb.append("        <param name=\"write-type\" value=\"insert\"/>\n            <param name=\"if-wr");
        sb.append("ite-fails\" value=\"ignore\"/>\n            <param name=\"no-match-mode\" value=\"outpu");
        sb.append("t-no-match-null\"/>\n            <param name=\"order-by-direction\" value=\"none\"/>\n ");
        sb.append("           <target-list>\n                <item name=\"input\" selection=\"all\"/>\n  ");
        sb.append("              <item name=\"old\" selection=\"none\"/>\n                <item name=\"ne");
        sb.append("w\" selection=\"none\"/>\n                <item name=\"current\" selection=\"none\"/>\n  ");
        sb.append("          </target-list>\n            <target-list name=\"insert\">\n               ");
        sb.append(" <item name=\"input\" selection=\"all\"/>\n                <expressions>\n            ");
        sb.append("        <set field=\"devId\">devId</set>\n                    <set field=\"value\">va");
        sb.append("lue</set>\n                </expressions>\n            </target-list>\n            ");
        sb.append("<target-list name=\"update\">\n                <item name=\"input\" selection=\"all\"/>");
        sb.append("\n            </target-list>\n            <target-list name=\"no-match\">\n          ");
        sb.append("      <item name=\"input\" selection=\"all\"/>\n            </target-list>\n        </");
        sb.append("box>\n        <box name=\"Map2\" type=\"map\">\n            <input port=\"1\" stream=\"ou");
        sb.append("t:Query_1\"/>\n            <output port=\"1\" stream=\"out:Map2_1\"/>\n            <tar");
        sb.append("get-list>\n                <item name=\"input\" selection=\"all\"/>\n                <");
        sb.append("expressions>\n                    <include field=\"list_item\">range(0, 48)</includ");
        sb.append("e>\n                </expressions>\n            </target-list>\n        </box>\n    ");
        sb.append("    <box name=\"Iterate\" type=\"iterate\">\n            <input port=\"1\" stream=\"out:");
        sb.append("Map2_1\"/>\n            <output port=\"1\" stream=\"out:Iterate_1\"/>\n            <par");
        sb.append("am name=\"iterate-expr\" value=\"list_item\"/>\n            <target-list>\n           ");
        sb.append("     <item name=\"input\" selection=\"all\"/>\n                <expressions>\n        ");
        sb.append("            <include field=\"element\">each.element</include>\n                </ex");
        sb.append("pressions>\n            </target-list>\n        </box>\n        <box name=\"OutputAd");
        sb.append("apter\" type=\"outputadapter\">\n            <input port=\"1\" stream=\"out:Iterate_1\"/");
        sb.append(">\n            <output port=\"1\" stream=\"out:OutputAdapter_1\"/>\n            <param");
        sb.append(" name=\"start:state\" value=\"true\"/>\n            <param name=\"javaclass\" value=\"co");
        sb.append("m.streambase.sb.adapter.logger.Log\"/>\n            <param name=\"CaptureStrategy\" ");
        sb.append("value=\"FLATTEN\"/>\n            <param name=\"asyncEnabled\" value=\"true\"/>\n        ");
        sb.append("    <param name=\"compressFrequentMessages\" value=\"false\"/>\n            <param na");
        sb.append("me=\"compressMaxWindowSize\" value=\"5.0\"/>\n            <param name=\"customFormat\" ");
        sb.append("value=\"\"/>\n            <param name=\"customFormatExpression\" value=\"\"/>\n         ");
        sb.append("   <param name=\"customFormatType\" value=\"Basic\"/>\n            <param name=\"emitL");
        sb.append("oggedTuples\" value=\"true\"/>\n            <param name=\"encodeSubType\" value=\"Map\"/");
        sb.append(">\n            <param name=\"hexEncodeBlobs\" value=\"false\"/>\n            <param na");
        sb.append("me=\"includeNullFields\" value=\"false\"/>\n            <param name=\"logLevel\" value=");
        sb.append("\"Info\"/>\n            <param name=\"logLevelOverrideFieldName\" value=\"\"/>\n        ");
        sb.append("    <param name=\"loggerFormatType\" value=\"CSV\"/>\n            <param name=\"messag");
        sb.append("eIdentifier\" value=\"\"/>\n            <param name=\"messageIdentifierExpression\" va");
        sb.append("lue=\"\"/>\n            <param name=\"messageIdentifierType\" value=\"Use EventFlow Na");
        sb.append("me\"/>\n            <param name=\"messagePrefix\" value=\"\"/>\n            <param name");
        sb.append("=\"moduleQualifiedEventFlowName\" value=\"false\"/>\n            <param name=\"quoteNu");
        sb.append("llStrings\" value=\"true\"/>\n            <param name=\"timestampAsLong\" value=\"false");
        sb.append("\"/>\n            <param name=\"timestampFormat\" value=\"yyyy-MM-dd HH:mm:ss.SSSZ\"/>");
        sb.append("\n            <param name=\"verbose\" value=\"false\"/>\n        </box>\n        <box n");
        sb.append("ame=\"MapCopy\" type=\"map\">\n            <input port=\"1\" stream=\"out:OutputAdapter_");
        sb.append("1\"/>\n            <output port=\"1\" stream=\"out:MapCopy_1\"/>\n            <target-l");
        sb.append("ist>\n                <item name=\"input\" selection=\"none\"/>\n                <expr");
        sb.append("essions>\n                    <include field=\"filePath\">\"e:/projects/str/testdata");
        sb.append(".dat\"</include>\n                    <include field=\"time\">timestamp</include>\n  ");
        sb.append("              </expressions>\n            </target-list>\n        </box>\n        <");
        sb.append("module-reference name=\"TestPythonRef1\">\n            <input dispatch=\"round_robin");
        sb.append("\" name=\"InputStream\" port=\"1\" stream=\"out:MapCopy_1\"/>\n            <output name=");
        sb.append("\"OutputStream\" port=\"1\" stream=\"OutputStream\"/>\n            <param name=\"paralle");
        sb.append("l\" value=\"true\"/>\n            <param name=\"parallel-region-queue-max-outstanding");
        sb.append("-tuples\" value=\"2\"/>\n            <param name=\"parallel-region-queue-wait-strateg");
        sb.append("y\" value=\"BLOCKING\"/>\n            <multiplicity number=\"8\" type=\"concrete\"/>\n   ");
        sb.append("         <param name=\"file\" value=\"com.example.fullipsori.exerevent.TestPython\"/");
        sb.append(">\n        </module-reference>\n        <output-stream name=\"OutputStream\"/>\n    <");
        sb.append("/add>\n</modify>\n");
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

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.example.fullipsori.exerevent.ExerEvent_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.example.fullipsori.exerevent.ExerEvent_ModuleSourceProvider.makeExports();
}
