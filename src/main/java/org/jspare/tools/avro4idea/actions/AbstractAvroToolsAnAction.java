package org.jspare.tools.avro4idea.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.tool.Tool;

import java.util.Arrays;

public abstract class AbstractAvroToolsAnAction extends AnAction {

    private static final Logger log = Logger.getInstance(AbstractAvroToolsAnAction.class);

    @Override
    public void actionPerformed(AnActionEvent event) {
        String command = getCommand();
        VirtualFile vFile = FileEditorManager.getInstance(event.getProject()).getSelectedEditor().getFile();

        String in = vFile.getCanonicalFile().getCanonicalPath();
        String out = vFile.getCanonicalFile().getParent().getCanonicalPath();

        try {
            log.info("Performing " + command + " " + in + " > " + out);
            getTool().run(System.in, System.out, System.err, Arrays.asList(in, out));
        } catch (Exception e) {
            log.error("Fail to " + command + " [" + vFile.getCanonicalPath() + "] to schemata");
            log.error(e.getMessage(), e);
        }
    }

    public abstract String getCommand();

    public abstract Tool getTool();
}
