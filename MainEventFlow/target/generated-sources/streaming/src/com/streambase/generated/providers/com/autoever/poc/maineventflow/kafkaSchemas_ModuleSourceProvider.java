/*

	Class

*/
package com.streambase.generated.providers.com.autoever.poc.maineventflow;

public class kafkaSchemas_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public kafkaSchemas_ModuleSourceProvider() {
        super("MainEventFlow", "B3F36F9C1E6E13942E428DD60FA1137D");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"interface\"/>\n            <param name=\"f");
        sb.append("ully-qualified-name\" value=\"com.autoever.poc.maineventflow.kafkaSchemas\"/>\n     ");
        sb.append("   </type-metadata>\n        <memory-model-settings/>\n        <named-schemas>\n   ");
        sb.append("         <schema name=\"KafkaAdminCommandSchema\">\n                <field descript");
        sb.append("ion=\"Commands: createTopic, deleteTopic, brokers, writeValue, readValue, or topi");
        sb.append("cs\" name=\"command\" type=\"string\"/>\n                <field name=\"topic\" type=\"str");
        sb.append("ing\"/>\n                <field name=\"partition\" type=\"int\"/>\n                <fie");
        sb.append("ld name=\"replication\" type=\"int\"/>\n                <field name=\"properties\" type");
        sb.append("=\"list\">\n                    <element-type type=\"tuple\">\n                       ");
        sb.append(" <schema>\n                            <field name=\"key\" type=\"string\"/>\n        ");
        sb.append("                    <field name=\"value\" type=\"string\"/>\n                        ");
        sb.append("</schema>\n                    </element-type>\n                </field>\n         ");
        sb.append("   </schema>\n            <schema name=\"KafkaAdminFullCommandSchema\">\n           ");
        sb.append("     <field description=\"Commands: createTopic, deleteTopic, brokers, writeValue");
        sb.append(", readValue, or topics\" name=\"command\" type=\"string\"/>\n                <field na");
        sb.append("me=\"topic\" type=\"string\"/>\n                <field name=\"partition\" type=\"int\"/>\n");
        sb.append("                <field name=\"replication\" type=\"int\"/>\n                <field na");
        sb.append("me=\"properties\" type=\"list\">\n                    <element-type type=\"tuple\">\n   ");
        sb.append("                     <schema>\n                            <field name=\"key\" type");
        sb.append("=\"string\"/>\n                            <field name=\"value\" type=\"string\"/>\n    ");
        sb.append("                    </schema>\n                    </element-type>\n              ");
        sb.append("  </field>\n            </schema>\n            <schema name=\"KafkaConsumerControlS");
        sb.append("chema\">\n                <field description=\"Commands: subscribe, unsubscribe, up");
        sb.append("dateBrokers, pause, or resume\" name=\"command\" type=\"string\"/>\n                <f");
        sb.append("ield name=\"topic\" type=\"string\"/>\n                <field name=\"partition\" type=\"");
        sb.append("int\"/>\n                <field name=\"time\" type=\"long\"/>\n            </schema>\n  ");
        sb.append("          <schema name=\"KafkaConsumerFullControlSchema\">\n                <field ");
        sb.append("description=\"Commands: subscribe, unsubscribe, updateBrokers, pause, or resume\" ");
        sb.append("name=\"command\" type=\"string\"/>\n                <field name=\"topic\" type=\"string\"");
        sb.append("/>\n                <field name=\"partition\" type=\"int\"/>\n                <field n");
        sb.append("ame=\"brokers\" type=\"list\">\n                    <element-type type=\"tuple\">\n     ");
        sb.append("                   <schema>\n                            <field name=\"host\" type=");
        sb.append("\"string\"/>\n                            <field name=\"port\" type=\"int\"/>\n         ");
        sb.append("               </schema>\n                    </element-type>\n                </f");
        sb.append("ield>\n                <field name=\"time\" type=\"long\"/>\n                <field na");
        sb.append("me=\"offset\" type=\"long\"/>\n                <field name=\"timeout\" type=\"int\"/>\n   ");
        sb.append("             <field name=\"clientId\" type=\"string\"/>\n                <field name=");
        sb.append("\"bufferSize\" type=\"int\"/>\n                <field name=\"fetchSize\" type=\"int\"/>\n ");
        sb.append("           </schema>\n            <schema name=\"KafkaProducerCommandSchema\">\n    ");
        sb.append("            <field description=\"Commands: connect, disconnect, updateBrokers, or");
        sb.append(" metrics\" name=\"command\" type=\"string\"/>\n                <field name=\"brokers\" t");
        sb.append("ype=\"list\">\n                    <element-type type=\"tuple\">\n                    ");
        sb.append("    <schema>\n                            <field name=\"host\" type=\"string\"/>\n    ");
        sb.append("                        <field name=\"port\" type=\"int\"/>\n                        ");
        sb.append("</schema>\n                    </element-type>\n                </field>\n         ");
        sb.append("   </schema>\n            <schema name=\"KafkaProducerDataSchema\">\n               ");
        sb.append(" <field name=\"topic\" type=\"string\"/>\n                <field name=\"message\" type=");
        sb.append("\"string\"/>\n                <field name=\"myKey\" type=\"string\"/>\n                <");
        sb.append("field name=\"partition\" type=\"int\"/>\n            </schema>\n            <schema na");
        sb.append("me=\"KafkaVDMSDataSchema\">\n                <field description=\"단말기 시리얼 번호\" name=\"");
        sb.append("TerminalID\" type=\"string\"/>\n                <field name=\"SequenceNo\" type=\"long\"");
        sb.append("/>\n                <field description=\"Body 크기\" name=\"BodyLength\" type=\"long\"/>\n");
        sb.append("                <field description=\"차종\" name=\"CIN\" type=\"string\"/>\n             ");
        sb.append("   <field description=\"차량식별번호\" name=\"VIN\" type=\"string\"/>\n                <field");
        sb.append(" description=\"차량 키 아이디\" name=\"VehicleKeyID\" type=\"long\"/>\n                <field");
        sb.append(" description=\"데이터 수집 정책 버전\" name=\"PolicyVersion\" type=\"int\"/>\n                <f");
        sb.append("ield description=\"데이터 건수\" name=\"RecordCount\" type=\"long\"/>\n                <fiel");
        sb.append("d description=\"트립 번호\" name=\"RootCount\" type=\"int\"/>\n                <field descr");
        sb.append("iption=\"트립 안에서의 데이터 순서\" name=\"SubmitSequenceNo\" type=\"long\"/>\n                <f");
        sb.append("ield description=\"단말기 시리얼 번호\" name=\"SerialNo\" type=\"string\"/>\n                <f");
        sb.append("ield description=\"데이터 수집 시작 시간\" name=\"BaseTime\" type=\"long\"/>\n                <f");
        sb.append("ield description=\"수집 데이터의 종류\" name=\"MessageType\" type=\"int\"/>\n                <f");
        sb.append("ield description=\"CCP 메세지인 경우\" name=\"FirstPID\" type=\"string\"/>\n                <");
        sb.append("field description=\"메시지파일ID\" name=\"MsgSrcKeyID\" type=\"string\"/>\n                <");
        sb.append("field description=\"동기서버 생성 ID\" name=\"SyncSerID\" type=\"string\"/>\n                ");
        sb.append("<field description=\"Message Collect Time\" name=\"LoadDTM\" type=\"string\"/>\n       ");
        sb.append("         <field description=\"예외 Redis 입력일시\" name=\"XctRedisInpDTM\" type=\"long\"/>\n");
        sb.append("            </schema>\n        </named-schemas>\n        <dynamic-variables/>\n    ");
        sb.append("</add>\n</modify>\n");
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

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.autoever.poc.maineventflow.kafkaSchemas_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.autoever.poc.maineventflow.kafkaSchemas_ModuleSourceProvider.makeExports();
}
