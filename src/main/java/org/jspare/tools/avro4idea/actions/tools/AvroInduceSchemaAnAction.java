package org.jspare.tools.avro4idea.actions.tools;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.tool.IdlToSchemataTool;
import org.apache.avro.tool.Tool;

import java.util.Arrays;
import java.util.List;

public class AvroInduceSchemaAnAction extends AbstractAvroToolsAnAction {

    @Override
    protected List<String> arguments(AnActionEvent event, VirtualFile vFile, String in) {
        String out = vFile.getCanonicalFile().getParent().getCanonicalPath();
        return Arrays.asList(in, out);
    }

    @Override
    public String getCommand() {
        return "idl2schemata";
    }

    @Override
    public Tool getTool() {
        return new IdlToSchemataTool();
    }

}
