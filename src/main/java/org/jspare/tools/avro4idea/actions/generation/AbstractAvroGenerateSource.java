package org.jspare.tools.avro4idea.actions.generation;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.compiler.idl.Idl;
import org.apache.avro.compiler.idl.ParseException;

import java.io.IOException;

public abstract class AbstractAvroGenerateSource extends AnAction {

    private static final Logger log = Logger.getInstance(AbstractAvroGenerateSource.class);

    @Override
    public void actionPerformed(AnActionEvent event) {

        VirtualFile vFile = FileEditorManager.getInstance(event.getProject()).getSelectedEditor().getFile();

        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile vOutput = FileChooser.chooseFile(descriptor, event.getProject(), null);

        ClassLoader oldContextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(Idl.class.getClassLoader());

            doIt(vFile, vOutput);

        } catch (Exception e1) {
            log.error("Fail while converting file " + vFile.getUrl() + " to a avsc.");
            log.error(e1.getMessage(), e1);
        } finally {
            Thread.currentThread().setContextClassLoader(oldContextClassLoader);
            LocalFileSystem.getInstance().refreshAndFindFileByPath(vFile.getCanonicalFile().getParent().getCanonicalPath());
        }
    }

    protected abstract void doIt(VirtualFile vInput, VirtualFile vOutput) throws IOException, ParseException;
}
