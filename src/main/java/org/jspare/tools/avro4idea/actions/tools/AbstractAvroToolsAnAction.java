package org.jspare.tools.avro4idea.actions.tools;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.tool.Tool;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractAvroToolsAnAction extends AnAction {

    private static final Logger log = Logger.getInstance(AbstractAvroToolsAnAction.class);

    @Override
    public void actionPerformed(AnActionEvent event) {
        String command = getCommand();
        VirtualFile vFile = FileEditorManager.getInstance(event.getProject()).getSelectedEditor().getFile();

        String in = vFile.getCanonicalFile().getCanonicalPath();
        List<String> arguments = arguments(event, vFile, in);

        try {
            log.info("Performing " + arguments.stream().collect(Collectors.joining(" ")));
            getTool().run(System.in, System.out, System.err, arguments);
        } catch (Exception e) {
            log.error("Fail to " + command + " [" + arguments + "]");
            log.error(e.getMessage(), e);
        } finally {

            LocalFileSystem.getInstance().refreshAndFindFileByPath(vFile.getCanonicalFile().getParent().getCanonicalPath());
        }
    }

    protected abstract List<String> arguments(AnActionEvent event, VirtualFile vFile, String in);

    protected abstract String getCommand();

    protected abstract Tool getTool();

}
