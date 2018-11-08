package org.jspare.tools.avro4idea.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.Protocol;
import org.apache.avro.compiler.idl.Idl;
import org.apache.avro.compiler.specific.SpecificCompiler;
import org.apache.avro.generic.GenericData;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class AvroGenerateSourcesFromAvdl extends AnAction {

    private static final Logger log = Logger.getInstance(AvroGenerateSourcesFromAvdl.class);
    public static final String DEFAULT_TEMPLATE_DIR = "/org/apache/avro/compiler/specific/templates/java/classic/";
    public static final String DEFAULT_STRING_TYPE = "string";

    @Override
    public void actionPerformed(AnActionEvent event) {

        VirtualFile vFile = FileEditorManager.getInstance(event.getProject()).getSelectedEditor().getFile();
        Idl parser;

        log.info("Converting [" + vFile.getUrl() + "] to avsc.");
        if (!"avdl".equals(vFile.getExtension())) {
            log.info("Conversion not supported for " + vFile.getExtension());
            return;
        }

        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile vOutput = FileChooser.chooseFile(descriptor, event.getProject(), null);

        ClassLoader oldContextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(Idl.class.getClassLoader());

            URL url = new URL(vFile.getUrl());
            File file = new File(vFile.getCanonicalPath());

            URLClassLoader projectPathLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
            parser = new Idl(file, projectPathLoader);

            Protocol p = parser.CompilationUnit();
            String json = p.toString(true);
            Protocol protocol = Protocol.parse(json);
            SpecificCompiler compiler = new SpecificCompiler(protocol);
            compiler.setStringType(GenericData.StringType.String);
            compiler.setTemplateDir(DEFAULT_TEMPLATE_DIR);
            compiler.setFieldVisibility(SpecificCompiler.FieldVisibility.PUBLIC);
            compiler.setCreateSetters(true);
            compiler.setEnableDecimalLogicalType(false);
            compiler.setOutputCharacterEncoding(vFile.getCharset().name());
            compiler.compileToDestination(null, new File(vOutput.getCanonicalPath()));

        } catch (Exception e1) {
            log.error("Fail while converting file " + vFile.getUrl() + " to a avsc.");
            log.error(e1.getMessage(), e1);
        } finally {
            Thread.currentThread().setContextClassLoader(oldContextClassLoader);
        }
    }

}
