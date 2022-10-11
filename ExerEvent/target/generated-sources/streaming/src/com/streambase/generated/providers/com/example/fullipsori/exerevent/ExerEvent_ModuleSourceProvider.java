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

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<modify version=\"11.0.0_");
        sb.append("248f263d973a774f84731121e9d4527c932d77fd\">\r\n    <add>\r\n        <annotations>\r\n  ");
        sb.append("          <annotation name=\"hygienic\"/>\r\n        </annotations>\r\n        <type-m");
        sb.append("etadata>\r\n            <param name=\"type\" value=\"module\"/>\r\n            <param na");
        sb.append("me=\"fully-qualified-name\" value=\"com.example.fullipsori.exerevent.ExerEvent\"/>\r\n");
        sb.append("        </type-metadata>\r\n        <memory-model-settings/>\r\n        <implements ");
        sb.append("module-reference=\"com.example.fullipsori.exerevent.TestTable\"/>\r\n        <import");
        sb.append(" from=\"com.example.fullipsori.exerevent.TestTable\"/>\r\n        <dynamic-variables");
        sb.append("/>\r\n        <data name=\"QueryTable\" shared=\"false\" source=\"concrete\" type=\"query");
        sb.append("table\">\r\n            <param name=\"storage-method\" value=\"heap\"/>\r\n            <p");
        sb.append("aram name=\"replicated\" value=\"false\"/>\r\n            <param name=\"table-schema\" v");
        sb.append("alue=\"testTable\"/>\r\n            <param name=\"truncate\" value=\"false\"/>\r\n        ");
        sb.append("    <preload format=\"csv\" mode=\"empty\"/>\r\n        </data>\r\n        <box name=\"In");
        sb.append("putAdapter\" type=\"inputadapter\">\r\n            <output port=\"1\" stream=\"out:Input");
        sb.append("Adapter_1\"/>\r\n            <param name=\"start:state\" value=\"true\"/>\r\n            ");
        sb.append("<param name=\"javaclass\" value=\"com.streambase.sb.adapter.once.Once\"/>\r\n         ");
        sb.append("   <param name=\"OutputField\" value=\"time\"/>\r\n        </box>\r\n        <box name=\"");
        sb.append("Map\" type=\"map\">\r\n            <input port=\"1\" stream=\"out:InputAdapter_1\"/>\r\n   ");
        sb.append("         <output port=\"1\" stream=\"out:Map_1\"/>\r\n            <target-list>\r\n     ");
        sb.append("           <item name=\"input\" selection=\"none\"/>\r\n                <expressions>\r");
        sb.append("\n                    <include field=\"filePath\">\"e:/projects/str/testdata.dat\"</i");
        sb.append("nclude>\r\n                    <include field=\"time\">input1.time</include>\r\n      ");
        sb.append("          </expressions>\r\n            </target-list>\r\n        </box>\r\n        <m");
        sb.append("odule-reference name=\"TestPythonRef2\">\r\n            <input name=\"InputStream\" po");
        sb.append("rt=\"1\" stream=\"out:Map_1\"/>\r\n            <output name=\"OutputStream\" port=\"1\" st");
        sb.append("ream=\"out:TestPythonRef2_1\"/>\r\n            <param name=\"file\" value=\"com.example");
        sb.append(".fullipsori.exerevent.TestPython\"/>\r\n        </module-reference>\r\n        <box n");
        sb.append("ame=\"Map3\" type=\"map\">\r\n            <input port=\"1\" stream=\"out:TestPythonRef2_1");
        sb.append("\"/>\r\n            <output port=\"1\" stream=\"out:Map3_1\"/>\r\n            <target-lis");
        sb.append("t>\r\n                <item name=\"input\" selection=\"all\"/>\r\n                <expre");
        sb.append("ssions>\r\n                    <include field=\"queryId\">range(1, 1000)</include>\r\n");
        sb.append("                </expressions>\r\n            </target-list>\r\n        </box>\r\n    ");
        sb.append("    <box name=\"Iterate2\" type=\"iterate\">\r\n            <input port=\"1\" stream=\"ou");
        sb.append("t:Map3_1\"/>\r\n            <output port=\"1\" stream=\"out:Iterate2_1\"/>\r\n           ");
        sb.append(" <param name=\"iterate-expr\" value=\"queryId\"/>\r\n            <target-list>\r\n      ");
        sb.append("          <item name=\"input\" selection=\"all\"/>\r\n                <expressions>\r\n ");
        sb.append("                   <include field=\"keyId\">each.element</include>\r\n              ");
        sb.append("  </expressions>\r\n            </target-list>\r\n        </box>\r\n        <box name=");
        sb.append("\"Map4\" type=\"map\">\r\n            <input port=\"1\" stream=\"out:Iterate2_1\"/>\r\n     ");
        sb.append("       <output port=\"1\" stream=\"out:Map4_1\"/>\r\n            <target-list>\r\n      ");
        sb.append("          <item name=\"input\" selection=\"all\"/>\r\n                <expressions>\r\n ");
        sb.append("                   <include field=\"devId\">string(keyId)</include>\r\n             ");
        sb.append("       <include field=\"value\">string(now())</include>\r\n                </express");
        sb.append("ions>\r\n            </target-list>\r\n        </box>\r\n        <box name=\"Query\" typ");
        sb.append("e=\"query\">\r\n            <input port=\"1\" stream=\"out:Map4_1\"/>\r\n            <outp");
        sb.append("ut port=\"1\" stream=\"out:Query_1\"/>\r\n            <dataref id=\"querytable\" name=\"Q");
        sb.append("ueryTable\"/>\r\n            <param name=\"operation\" value=\"write\"/>\r\n            <");
        sb.append("param name=\"where\" value=\"primary-key\"/>\r\n            <param name=\"key-field.0\" ");
        sb.append("value=\"keyId\"/>\r\n            <param name=\"key-value.0\" value=\"input1.keyId\"/>\r\n ");
        sb.append("           <param name=\"write-type\" value=\"insert\"/>\r\n            <param name=\"i");
        sb.append("f-write-fails\" value=\"ignore\"/>\r\n            <param name=\"no-match-mode\" value=\"");
        sb.append("output-no-match-null\"/>\r\n            <param name=\"order-by-direction\" value=\"non");
        sb.append("e\"/>\r\n            <target-list>\r\n                <item name=\"input\" selection=\"a");
        sb.append("ll\"/>\r\n                <item name=\"old\" selection=\"none\"/>\r\n                <ite");
        sb.append("m name=\"new\" selection=\"none\"/>\r\n                <item name=\"current\" selection=");
        sb.append("\"none\"/>\r\n            </target-list>\r\n            <target-list name=\"insert\">\r\n ");
        sb.append("               <item name=\"input\" selection=\"all\"/>\r\n                <expression");
        sb.append("s>\r\n                    <set field=\"devId\">devId</set>\r\n                    <set");
        sb.append(" field=\"value\">value</set>\r\n                </expressions>\r\n            </target");
        sb.append("-list>\r\n            <target-list name=\"update\">\r\n                <item name=\"inp");
        sb.append("ut\" selection=\"all\"/>\r\n            </target-list>\r\n            <target-list name");
        sb.append("=\"no-match\">\r\n                <item name=\"input\" selection=\"all\"/>\r\n            ");
        sb.append("</target-list>\r\n        </box>\r\n        <box name=\"Map2\" type=\"map\">\r\n          ");
        sb.append("  <input port=\"1\" stream=\"out:Query_1\"/>\r\n            <output port=\"1\" stream=\"o");
        sb.append("ut:Map2_1\"/>\r\n            <target-list>\r\n                <item name=\"input\" sele");
        sb.append("ction=\"all\"/>\r\n                <expressions>\r\n                    <include field");
        sb.append("=\"list_item\">range(0, 48)</include>\r\n                </expressions>\r\n           ");
        sb.append(" </target-list>\r\n        </box>\r\n        <box name=\"Iterate\" type=\"iterate\">\r\n  ");
        sb.append("          <input port=\"1\" stream=\"out:Map2_1\"/>\r\n            <output port=\"1\" st");
        sb.append("ream=\"out:Iterate_1\"/>\r\n            <param name=\"iterate-expr\" value=\"list_item\"");
        sb.append("/>\r\n            <target-list>\r\n                <item name=\"input\" selection=\"all");
        sb.append("\"/>\r\n                <expressions>\r\n                    <include field=\"element\"");
        sb.append(">each.element</include>\r\n                </expressions>\r\n            </target-li");
        sb.append("st>\r\n        </box>\r\n        <box name=\"OutputAdapter\" type=\"outputadapter\">\r\n  ");
        sb.append("          <input port=\"1\" stream=\"out:Iterate_1\"/>\r\n            <output port=\"1\"");
        sb.append(" stream=\"out:OutputAdapter_1\"/>\r\n            <param name=\"start:state\" value=\"tr");
        sb.append("ue\"/>\r\n            <param name=\"javaclass\" value=\"com.streambase.sb.adapter.logg");
        sb.append("er.Log\"/>\r\n            <param name=\"CaptureStrategy\" value=\"FLATTEN\"/>\r\n        ");
        sb.append("    <param name=\"asyncEnabled\" value=\"true\"/>\r\n            <param name=\"compress");
        sb.append("FrequentMessages\" value=\"false\"/>\r\n            <param name=\"compressMaxWindowSiz");
        sb.append("e\" value=\"5.0\"/>\r\n            <param name=\"customFormat\" value=\"\"/>\r\n           ");
        sb.append(" <param name=\"customFormatExpression\" value=\"\"/>\r\n            <param name=\"custo");
        sb.append("mFormatType\" value=\"Basic\"/>\r\n            <param name=\"emitLoggedTuples\" value=\"");
        sb.append("true\"/>\r\n            <param name=\"encodeSubType\" value=\"Map\"/>\r\n            <par");
        sb.append("am name=\"hexEncodeBlobs\" value=\"false\"/>\r\n            <param name=\"includeNullFi");
        sb.append("elds\" value=\"false\"/>\r\n            <param name=\"logLevel\" value=\"Info\"/>\r\n      ");
        sb.append("      <param name=\"logLevelOverrideFieldName\" value=\"\"/>\r\n            <param nam");
        sb.append("e=\"loggerFormatType\" value=\"CSV\"/>\r\n            <param name=\"messageIdentifier\" ");
        sb.append("value=\"\"/>\r\n            <param name=\"messageIdentifierExpression\" value=\"\"/>\r\n  ");
        sb.append("          <param name=\"messageIdentifierType\" value=\"Use EventFlow Name\"/>\r\n    ");
        sb.append("        <param name=\"messagePrefix\" value=\"\"/>\r\n            <param name=\"moduleQ");
        sb.append("ualifiedEventFlowName\" value=\"false\"/>\r\n            <param name=\"quoteNullString");
        sb.append("s\" value=\"true\"/>\r\n            <param name=\"timestampAsLong\" value=\"false\"/>\r\n  ");
        sb.append("          <param name=\"timestampFormat\" value=\"yyyy-MM-dd HH:mm:ss.SSSZ\"/>\r\n    ");
        sb.append("        <param name=\"verbose\" value=\"false\"/>\r\n        </box>\r\n        <box name");
        sb.append("=\"MapCopy\" type=\"map\">\r\n            <input port=\"1\" stream=\"out:OutputAdapter_1\"");
        sb.append("/>\r\n            <output port=\"1\" stream=\"out:MapCopy_1\"/>\r\n            <target-l");
        sb.append("ist>\r\n                <item name=\"input\" selection=\"none\"/>\r\n                <ex");
        sb.append("pressions>\r\n                    <include field=\"filePath\">\"e:/projects/str/testd");
        sb.append("ata.dat\"</include>\r\n                    <include field=\"time\">timestamp</include");
        sb.append(">\r\n                </expressions>\r\n            </target-list>\r\n        </box>\r\n ");
        sb.append("       <module-reference name=\"TestPythonRef1\">\r\n            <input dispatch=\"ro");
        sb.append("und_robin\" name=\"InputStream\" port=\"1\" stream=\"out:MapCopy_1\"/>\r\n            <ou");
        sb.append("tput name=\"OutputStream\" port=\"1\" stream=\"OutputStream\"/>\r\n            <param na");
        sb.append("me=\"parallel\" value=\"true\"/>\r\n            <param name=\"parallel-region-queue-max");
        sb.append("-outstanding-tuples\" value=\"2\"/>\r\n            <param name=\"parallel-region-queue");
        sb.append("-wait-strategy\" value=\"BLOCKING\"/>\r\n            <multiplicity number=\"8\" type=\"c");
        sb.append("oncrete\"/>\r\n            <param name=\"file\" value=\"com.example.fullipsori.exereve");
        sb.append("nt.TestPython\"/>\r\n        </module-reference>\r\n        <output-stream name=\"Outp");
        sb.append("utStream\"/>\r\n    </add>\r\n</modify>\r\n");
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
