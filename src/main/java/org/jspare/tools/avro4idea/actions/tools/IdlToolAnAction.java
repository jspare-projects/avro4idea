package org.jspare.tools.avro4idea.actions.tools;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.tool.IdlTool;
import org.apache.avro.tool.Tool;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class IdlToolAnAction extends AbstractAvroToolsAnAction {

    @Override
    protected List<String> arguments(AnActionEvent event, VirtualFile vFile, String in) {
        String filename = vFile.getNameWithoutExtension();
        String out = vFile.getCanonicalFile().getParent().getCanonicalPath() + File.separator + filename + ".avpr";
        return Arrays.asList(in, out);
    }

    @Override
    public String getCommand() {
        return "idl";
    }

    @Override
    public Tool getTool() {
        return new IdlTool();
    }
}
