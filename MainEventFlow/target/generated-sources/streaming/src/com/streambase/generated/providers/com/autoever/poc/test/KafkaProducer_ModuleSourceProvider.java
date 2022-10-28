/*

	Class

*/
package com.streambase.generated.providers.com.autoever.poc.test;

public class KafkaProducer_ModuleSourceProvider extends com.streambase.sb.build.impl.gen.AbstractBinaryModuleSourceProvider {

    public KafkaProducer_ModuleSourceProvider() {
        super("MainEventFlow", "E358EFA489F58062F10DD7316B65649E");
    }

    public static java.lang.String makeXMLAppIr() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n<modify version=\"11.0.0_2");
        sb.append("48f263d973a774f84731121e9d4527c932d77fd\">\n    <add>\n        <annotations>\n      ");
        sb.append("      <annotation name=\"hygienic\"/>\n        </annotations>\n        <type-metadat");
        sb.append("a>\n            <param name=\"type\" value=\"module\"/>\n            <param name=\"full");
        sb.append("y-qualified-name\" value=\"com.autoever.poc.test.KafkaProducer\"/>\n        </type-m");
        sb.append("etadata>\n        <memory-model-settings/>\n        <dynamic-variables/>\n    </add");
        sb.append(">\n</modify>\n");
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

    public static final java.lang.String XML_APP_IR = com.streambase.generated.providers.com.autoever.poc.test.KafkaProducer_ModuleSourceProvider.makeXMLAppIr();

    public static final com.streambase.sb.build.ModuleCGExports MODULE_CG_EXPORTS = com.streambase.generated.providers.com.autoever.poc.test.KafkaProducer_ModuleSourceProvider.makeExports();
}
