package org.jspare.tools.avro4idea.actions.tools;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.tool.CreateRandomFileTool;
import org.apache.avro.tool.Tool;

import java.util.Arrays;
import java.util.List;

public class CreateRandomFileToolAnAction extends AbstractAvroToolsAnAction {
    @Override
    protected List<String> arguments(AnActionEvent event, VirtualFile vFile, String in) {
        return Arrays.asList(in);
    }

    @Override
    protected String getCommand() {
        return "random";
    }

    @Override
    protected Tool getTool() {
        return new CreateRandomFileTool();
    }
}
