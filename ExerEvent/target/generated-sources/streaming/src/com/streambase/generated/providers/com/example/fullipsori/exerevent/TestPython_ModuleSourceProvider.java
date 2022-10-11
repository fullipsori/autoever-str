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

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n<modify version=\"11.0.0_");
        sb.append("248f263d973a774f84731121e9d4527c932d77fd\">\r\n    <add>\r\n        <annotations>\r\n  ");
        sb.append("          <annotation name=\"hygienic\"/>\r\n        </annotations>\r\n        <type-m");
        sb.append("etadata>\r\n            <param name=\"type\" value=\"module\"/>\r\n            <param na");
        sb.append("me=\"fully-qualified-name\" value=\"com.example.fullipsori.exerevent.TestPython\"/>\r");
        sb.append("\n        </type-metadata>\r\n        <memory-model-settings/>\r\n        <dynamic-va");
        sb.append("riables/>\r\n        <stream name=\"InputStream\">\r\n            <schema>\r\n          ");
        sb.append("      <field name=\"filePath\" type=\"string\"/>\r\n                <field name=\"time\"");
        sb.append(" type=\"timestamp\"/>\r\n            </schema>\r\n        </stream>\r\n        <box name");
        sb.append("=\"LocalInstance\" type=\"java\">\r\n            <param name=\"start:state\" value=\"true");
        sb.append("\"/>\r\n            <param name=\"javaclass\" value=\"com.streambase.sb.adapter.python");
        sb.append(".PythonInstanceAdapter\"/>\r\n            <param name=\"CaptureOutput\" value=\"false\"");
        sb.append("/>\r\n            <param name=\"EnableControlPort\" value=\"false\"/>\r\n            <pa");
        sb.append("ram name=\"Executable\" value=\"c:/ProgramData/Anaconda3/python.exe\"/>\r\n           ");
        sb.append(" <param name=\"GlobalInstanceId\" value=\"\"/>\r\n            <param name=\"InstanceTyp");
        sb.append("e\" value=\"Local\"/>\r\n            <param name=\"LogLevel\" value=\"INFO\"/>\r\n         ");
        sb.append("   <param name=\"UseTempFile\" value=\"false\"/>\r\n            <param name=\"WorkingDi");
        sb.append("r\" value=\".\"/>\r\n        </box>\r\n        <box name=\"Map\" type=\"map\">\r\n           ");
        sb.append(" <input port=\"1\" stream=\"InputStream\"/>\r\n            <output port=\"1\" stream=\"ou");
        sb.append("t:Map_1\"/>\r\n            <target-list>\r\n                <item name=\"input\" select");
        sb.append("ion=\"none\"/>\r\n                <expressions>\r\n                    <include field=");
        sb.append("\"inputVars.*\">*</include>\r\n                </expressions>\r\n            </target-");
        sb.append("list>\r\n        </box>\r\n        <box name=\"ppp\" type=\"java\">\r\n            <input ");
        sb.append("port=\"1\" stream=\"out:Map_1\"/>\r\n            <output port=\"1\" stream=\"out:ppp_1\"/>");
        sb.append("\r\n            <param name=\"start:state\" value=\"true\"/>\r\n            <param name=");
        sb.append("\"javaclass\" value=\"com.streambase.sb.adapter.python.PythonAdapter\"/>\r\n          ");
        sb.append("  <param name=\"Asynchronous\" value=\"false\"/>\r\n            <param name=\"GlobalIns");
        sb.append("tanceId\" value=\"\"/>\r\n            <param name=\"InstanceType\" value=\"Local\"/>\r\n   ");
        sb.append("         <param name=\"LocalInstanceId\" value=\"LocalInstance\"/>\r\n            <par");
        sb.append("am name=\"LogLevel\" value=\"INFO\"/>\r\n            <param name=\"OutputVarsSchema\" va");
        sb.append("lue=\"&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;&lt;schema&");
        sb.append("gt;&#13;&#10;    &lt;field name=&quot;result_data&quot; type=&quot;string&quot;/");
        sb.append("&gt;&#13;&#10;    &lt;field name=&quot;timestamp&quot; type=&quot;timestamp&quot");
        sb.append(";/&gt;&#13;&#10;&lt;/schema&gt;&#13;&#10;\"/>\r\n            <param name=\"Script\" v");
        sb.append("alue=\"\"/>\r\n            <param name=\"ScriptFile\" value=\"test.py\"/>\r\n            <");
        sb.append("param name=\"ScriptSource\" value=\"File\"/>\r\n            <param name=\"amsRequiredOn");
        sb.append("Startup\" value=\"false\"/>\r\n            <param name=\"artifactName\" value=\"\"/>\r\n   ");
        sb.append("         <param name=\"artifactVersion\" value=\"\"/>\r\n            <param name=\"enab");
        sb.append("leControlPort\" value=\"false\"/>\r\n        </box>\r\n        <box name=\"Map2\" type=\"m");
        sb.append("ap\">\r\n            <input port=\"1\" stream=\"out:ppp_1\"/>\r\n            <output port");
        sb.append("=\"1\" stream=\"out:Map2_1\"/>\r\n            <target-list>\r\n                <item nam");
        sb.append("e=\"input\" selection=\"none\"/>\r\n                <expressions>\r\n                   ");
        sb.append(" <include field=\"*\">outputVars.*</include>\r\n                    <include field=\"");
        sb.append("elapsed\">to_milliseconds(now())-to_milliseconds(outputVars.timestamp)</include>\r");
        sb.append("\n                </expressions>\r\n            </target-list>\r\n        </box>\r\n   ");
        sb.append("     <box name=\"pythonoutput\" type=\"outputadapter\">\r\n            <input port=\"1\"");
        sb.append(" stream=\"out:Map2_1\"/>\r\n            <output port=\"1\" stream=\"OutputStream\"/>\r\n  ");
        sb.append("          <param name=\"start:state\" value=\"true\"/>\r\n            <param name=\"jav");
        sb.append("aclass\" value=\"com.streambase.sb.adapter.logger.Log\"/>\r\n            <param name=");
        sb.append("\"CaptureStrategy\" value=\"FLATTEN\"/>\r\n            <param name=\"asyncEnabled\" valu");
        sb.append("e=\"true\"/>\r\n            <param name=\"compressFrequentMessages\" value=\"false\"/>\r\n");
        sb.append("            <param name=\"compressMaxWindowSize\" value=\"5.0\"/>\r\n            <para");
        sb.append("m name=\"customFormat\" value=\"\"/>\r\n            <param name=\"customFormatExpressio");
        sb.append("n\" value=\"\"/>\r\n            <param name=\"customFormatType\" value=\"Basic\"/>\r\n     ");
        sb.append("       <param name=\"emitLoggedTuples\" value=\"true\"/>\r\n            <param name=\"e");
        sb.append("ncodeSubType\" value=\"Map\"/>\r\n            <param name=\"hexEncodeBlobs\" value=\"fal");
        sb.append("se\"/>\r\n            <param name=\"includeNullFields\" value=\"false\"/>\r\n            ");
        sb.append("<param name=\"logLevel\" value=\"Info\"/>\r\n            <param name=\"logLevelOverride");
        sb.append("FieldName\" value=\"\"/>\r\n            <param name=\"loggerFormatType\" value=\"CSV\"/>\r");
        sb.append("\n            <param name=\"messageIdentifier\" value=\"\"/>\r\n            <param name");
        sb.append("=\"messageIdentifierExpression\" value=\"\"/>\r\n            <param name=\"messageIdent");
        sb.append("ifierType\" value=\"Use EventFlow Name\"/>\r\n            <param name=\"messagePrefix\"");
        sb.append(" value=\"\"/>\r\n            <param name=\"moduleQualifiedEventFlowName\" value=\"false");
        sb.append("\"/>\r\n            <param name=\"quoteNullStrings\" value=\"true\"/>\r\n            <par");
        sb.append("am name=\"timestampAsLong\" value=\"false\"/>\r\n            <param name=\"timestampFor");
        sb.append("mat\" value=\"yyyy-MM-dd HH:mm:ss.SSSZ\"/>\r\n            <param name=\"verbose\" value");
        sb.append("=\"false\"/>\r\n        </box>\r\n        <output-stream name=\"OutputStream\"/>\r\n    </");
        sb.append("add>\r\n</modify>\r\n");
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
