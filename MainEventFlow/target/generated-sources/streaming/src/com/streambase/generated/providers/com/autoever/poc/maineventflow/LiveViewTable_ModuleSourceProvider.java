/*

	Class

*/
package com.streambase.generated.providers.com.autoever.poc.maineventflow;

public class LiveViewTable_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public LiveViewTable_ModuleSourceProvider() {
        super("MainEventFlow", "EC6A8ACB0781116FE23A80EB37C6D3E5");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"interface\"/>\n            <param name=\"f");
        sb.append("ully-qualified-name\" value=\"com.autoever.poc.maineventflow.LiveViewTable\"/>\n    ");
        sb.append("    </type-metadata>\n        <memory-model-settings/>\n        <named-schemas>\n  ");
        sb.append("          <schema name=\"CanDataTableSchema\">\n                <field name=\"messag");
        sb.append("eID\" type=\"string\"/>\n                <field name=\"terminalID\" type=\"string\"/>\n  ");
        sb.append("              <field name=\"vehicleKeyID\" type=\"long\"/>\n                <field na");
        sb.append("me=\"mgmtNO\" type=\"string\"/>\n                <field name=\"rootCount\" type=\"int\"/>");
        sb.append("\n                <field name=\"baseTime\" type=\"long\"/>\n                <field nam");
        sb.append("e=\"realTime\" type=\"long\"/>\n                <field name=\"preTime\" type=\"double\"/>");
        sb.append("\n                <field name=\"postTime\" type=\"double\"/>\n                <field n");
        sb.append("ame=\"deltaTime\" type=\"double\"/>\n                <field name=\"eventName\" type=\"st");
        sb.append("ring\"/>\n                <field name=\"value\" type=\"int\"/>\n                <field ");
        sb.append("name=\"category\" type=\"string\"/>\n                <field name=\"status\" type=\"strin");
        sb.append("g\"/>\n            </schema>\n            <schema name=\"DiagDTCOutputSchema\">\n     ");
        sb.append("           <field name=\"messageID\" type=\"string\"/>\n                <field name=\"");
        sb.append("terminalID\" type=\"string\"/>\n                <field name=\"vehicleKeyID\" type=\"lon");
        sb.append("g\"/>\n                <field name=\"mgmtNO\" type=\"string\"/>\n                <field");
        sb.append(" name=\"rootCount\" type=\"int\"/>\n                <field name=\"baseTime\" type=\"long");
        sb.append("\"/>\n                <field name=\"realTime\" type=\"long\"/>\n                <field ");
        sb.append("name=\"timestamp\" type=\"double\"/>\n                <field name=\"can_id\" type=\"stri");
        sb.append("ng\"/>\n                <field name=\"ecu_name\" type=\"string\"/>\n                <fi");
        sb.append("eld name=\"dtc_code\" type=\"string\"/>\n                <field name=\"dtc_time\" type=");
        sb.append("\"string\"/>\n                <field name=\"description\" type=\"string\"/>\n           ");
        sb.append(" </schema>\n            <schema name=\"DiagDataOutputSchema\">\n                <fie");
        sb.append("ld name=\"messageID\" type=\"string\"/>\n                <field name=\"terminalID\" typ");
        sb.append("e=\"string\"/>\n                <field name=\"vehicleKeyID\" type=\"long\"/>\n          ");
        sb.append("      <field name=\"mgmtNO\" type=\"string\"/>\n                <field name=\"rootCoun");
        sb.append("t\" type=\"int\"/>\n                <field name=\"baseTime\" type=\"long\"/>\n           ");
        sb.append("     <field name=\"realTime\" type=\"long\"/>\n                <field name=\"timestamp");
        sb.append("\" type=\"double\"/>\n                <field name=\"channel\" type=\"int\"/>\n           ");
        sb.append("     <field name=\"can_id\" type=\"string\"/>\n                <field name=\"ecu_name\"");
        sb.append(" type=\"string\"/>\n                <field name=\"Tx_Rx\" type=\"string\"/>\n           ");
        sb.append("     <field name=\"data0\" type=\"string\"/>\n                <field name=\"data1\" typ");
        sb.append("e=\"string\"/>\n                <field name=\"data2\" type=\"string\"/>\n               ");
        sb.append(" <field name=\"data3\" type=\"string\"/>\n                <field name=\"data4\" type=\"s");
        sb.append("tring\"/>\n                <field name=\"data5\" type=\"string\"/>\n                <fi");
        sb.append("eld name=\"data6\" type=\"string\"/>\n                <field name=\"data7\" type=\"strin");
        sb.append("g\"/>\n                <field name=\"raw\" type=\"string\"/>\n                <field na");
        sb.append("me=\"dtc_type\" type=\"string\"/>\n            </schema>\n            <schema name=\"GP");
        sb.append("SDataTableSchema\">\n                <field name=\"messageID\" type=\"string\"/>\n     ");
        sb.append("           <field name=\"terminalID\" type=\"string\"/>\n                <field name=");
        sb.append("\"vehicleKeyID\" type=\"long\"/>\n                <field name=\"mgmtNO\" type=\"string\"/");
        sb.append(">\n                <field name=\"rootCount\" type=\"int\"/>\n                <field na");
        sb.append("me=\"baseTime\" type=\"long\"/>\n                <field name=\"realTime\" type=\"long\"/>");
        sb.append("\n                <field name=\"Latitude\" type=\"double\"/>\n                <field n");
        sb.append("ame=\"Longitude\" type=\"double\"/>\n                <field name=\"Altitude\" type=\"dou");
        sb.append("ble\"/>\n                <field name=\"Velocity\" type=\"double\"/>\n                <f");
        sb.append("ield name=\"NS\" type=\"string\"/>\n                <field name=\"EW\" type=\"string\"/>\n");
        sb.append("            </schema>\n            <schema name=\"PerfDataSchema\">\n               ");
        sb.append(" <field name=\"kafka_msg_id\" type=\"string\"/>\n                <field name=\"start_t");
        sb.append("ime\" type=\"long\"/>\n                <field name=\"t1_time\" type=\"long\"/>\n         ");
        sb.append("       <field name=\"t2_time\" type=\"long\"/>\n                <field name=\"t3_time\"");
        sb.append(" type=\"long\"/>\n                <field name=\"t4_time\" type=\"long\"/>\n             ");
        sb.append("   <field name=\"t5_time\" type=\"long\"/>\n                <field name=\"end_time\" ty");
        sb.append("pe=\"long\"/>\n                <field name=\"elapsed\" type=\"long\"/>\n                ");
        sb.append("<field name=\"data_count\" type=\"int\"/>\n                <field name=\"msg_count\" ty");
        sb.append("pe=\"long\"/>\n            </schema>\n        </named-schemas>\n        <dynamic-vari");
        sb.append("ables/>\n    </add>\n</modify>\n");
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

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.autoever.poc.maineventflow.LiveViewTable_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.autoever.poc.maineventflow.LiveViewTable_ModuleSourceProvider.makeExports();
}
