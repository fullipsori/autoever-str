/*

	Class

*/
package com.streambase.generated.providers.com.example.fullipsori.exerevent;

public class TestPython_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public TestPython_ModuleSourceProvider() {
        super("ExerEvent", "91AED00B334F79F2CE4B5F5187525F4B");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"module\"/>\n            <param name=\"full");
        sb.append("y-qualified-name\" value=\"com.example.fullipsori.exerevent.TestPython\"/>\n        ");
        sb.append("</type-metadata>\n        <memory-model-settings/>\n        <dynamic-variables/>\n ");
        sb.append("       <stream name=\"InputStream\">\n            <schema>\n                <field n");
        sb.append("ame=\"filePath\" type=\"string\"/>\n                <field name=\"time\" type=\"timestam");
        sb.append("p\"/>\n            </schema>\n        </stream>\n        <box name=\"LocalInstance\" t");
        sb.append("ype=\"java\">\n            <param name=\"start:state\" value=\"true\"/>\n            <pa");
        sb.append("ram name=\"javaclass\" value=\"com.streambase.sb.adapter.python.PythonInstanceAdapt");
        sb.append("er\"/>\n            <param name=\"CaptureOutput\" value=\"false\"/>\n            <param");
        sb.append(" name=\"EnableControlPort\" value=\"false\"/>\n            <param name=\"Executable\" v");
        sb.append("alue=\"c:/ProgramData/Anaconda3/python.exe\"/>\n            <param name=\"GlobalInst");
        sb.append("anceId\" value=\"\"/>\n            <param name=\"InstanceType\" value=\"Local\"/>\n      ");
        sb.append("      <param name=\"LogLevel\" value=\"INFO\"/>\n            <param name=\"UseTempFile");
        sb.append("\" value=\"false\"/>\n            <param name=\"WorkingDir\" value=\".\"/>\n        </box");
        sb.append(">\n        <box name=\"Map\" type=\"map\">\n            <input port=\"1\" stream=\"InputS");
        sb.append("tream\"/>\n            <output port=\"1\" stream=\"out:Map_1\"/>\n            <target-l");
        sb.append("ist>\n                <item name=\"input\" selection=\"none\"/>\n                <expr");
        sb.append("essions>\n                    <include field=\"inputVars.*\">*</include>\n          ");
        sb.append("      </expressions>\n            </target-list>\n        </box>\n        <box name");
        sb.append("=\"ppp\" type=\"java\">\n            <input port=\"1\" stream=\"out:Map_1\"/>\n           ");
        sb.append(" <output port=\"1\" stream=\"out:ppp_1\"/>\n            <param name=\"start:state\" val");
        sb.append("ue=\"true\"/>\n            <param name=\"javaclass\" value=\"com.streambase.sb.adapter");
        sb.append(".python.PythonAdapter\"/>\n            <param name=\"Asynchronous\" value=\"false\"/>\n");
        sb.append("            <param name=\"GlobalInstanceId\" value=\"\"/>\n            <param name=\"I");
        sb.append("nstanceType\" value=\"Local\"/>\n            <param name=\"LocalInstanceId\" value=\"Lo");
        sb.append("calInstance\"/>\n            <param name=\"LogLevel\" value=\"INFO\"/>\n            <pa");
        sb.append("ram name=\"OutputVarsSchema\" value=\"&lt;?xml version=&quot;1.0&quot; encoding=&qu");
        sb.append("ot;UTF-8&quot;?&gt;&lt;schema&gt;&#13;&#10;    &lt;field name=&quot;result_data&");
        sb.append("quot; type=&quot;string&quot;/&gt;&#13;&#10;    &lt;field name=&quot;timestamp&q");
        sb.append("uot; type=&quot;timestamp&quot;/&gt;&#13;&#10;&lt;/schema&gt;&#13;&#10;\"/>\n     ");
        sb.append("       <param name=\"Script\" value=\"\"/>\n            <param name=\"ScriptFile\" valu");
        sb.append("e=\"test.py\"/>\n            <param name=\"ScriptSource\" value=\"File\"/>\n            ");
        sb.append("<param name=\"amsRequiredOnStartup\" value=\"false\"/>\n            <param name=\"arti");
        sb.append("factName\" value=\"\"/>\n            <param name=\"artifactVersion\" value=\"\"/>\n      ");
        sb.append("      <param name=\"enableControlPort\" value=\"false\"/>\n        </box>\n        <bo");
        sb.append("x name=\"Map2\" type=\"map\">\n            <input port=\"1\" stream=\"out:ppp_1\"/>\n     ");
        sb.append("       <output port=\"1\" stream=\"out:Map2_1\"/>\n            <target-list>\n        ");
        sb.append("        <item name=\"input\" selection=\"none\"/>\n                <expressions>\n    ");
        sb.append("                <include field=\"*\">outputVars.*</include>\n                    <i");
        sb.append("nclude field=\"elapsed\">to_milliseconds(now())-to_milliseconds(outputVars.timesta");
        sb.append("mp)</include>\n                </expressions>\n            </target-list>\n        ");
        sb.append("</box>\n        <box name=\"pythonoutput\" type=\"outputadapter\">\n            <input");
        sb.append(" port=\"1\" stream=\"out:Map2_1\"/>\n            <output port=\"1\" stream=\"OutputStrea");
        sb.append("m\"/>\n            <param name=\"start:state\" value=\"true\"/>\n            <param nam");
        sb.append("e=\"javaclass\" value=\"com.streambase.sb.adapter.logger.Log\"/>\n            <param ");
        sb.append("name=\"CaptureStrategy\" value=\"FLATTEN\"/>\n            <param name=\"asyncEnabled\" ");
        sb.append("value=\"true\"/>\n            <param name=\"compressFrequentMessages\" value=\"false\"/");
        sb.append(">\n            <param name=\"compressMaxWindowSize\" value=\"5.0\"/>\n            <par");
        sb.append("am name=\"customFormat\" value=\"\"/>\n            <param name=\"customFormatExpressio");
        sb.append("n\" value=\"\"/>\n            <param name=\"customFormatType\" value=\"Basic\"/>\n       ");
        sb.append("     <param name=\"emitLoggedTuples\" value=\"true\"/>\n            <param name=\"enco");
        sb.append("deSubType\" value=\"Map\"/>\n            <param name=\"hexEncodeBlobs\" value=\"false\"/");
        sb.append(">\n            <param name=\"includeNullFields\" value=\"false\"/>\n            <param");
        sb.append(" name=\"logLevel\" value=\"Info\"/>\n            <param name=\"logLevelOverrideFieldNa");
        sb.append("me\" value=\"\"/>\n            <param name=\"loggerFormatType\" value=\"CSV\"/>\n        ");
        sb.append("    <param name=\"messageIdentifier\" value=\"\"/>\n            <param name=\"messageI");
        sb.append("dentifierExpression\" value=\"\"/>\n            <param name=\"messageIdentifierType\" ");
        sb.append("value=\"Use EventFlow Name\"/>\n            <param name=\"messagePrefix\" value=\"\"/>\n");
        sb.append("            <param name=\"moduleQualifiedEventFlowName\" value=\"false\"/>\n         ");
        sb.append("   <param name=\"quoteNullStrings\" value=\"true\"/>\n            <param name=\"timest");
        sb.append("ampAsLong\" value=\"false\"/>\n            <param name=\"timestampFormat\" value=\"yyyy");
        sb.append("-MM-dd HH:mm:ss.SSSZ\"/>\n            <param name=\"verbose\" value=\"false\"/>\n      ");
        sb.append("  </box>\n        <output-stream name=\"OutputStream\"/>\n    </add>\n</modify>\n");
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

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.example.fullipsori.exerevent.TestPython_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.example.fullipsori.exerevent.TestPython_ModuleSourceProvider.makeExports();
}
