package org.jspare.tools.avro4idea.actions;

import org.apache.avro.tool.IdlToSchemataTool;
import org.apache.avro.tool.Tool;

public class AvroIdlToSchemata extends AbstractAvroToolsAnAction {

    @Override
    public String getCommand() {
        return "idl2schemata";
    }

    @Override
    public Tool getTool() {
        return new IdlToSchemataTool();
    }

}
