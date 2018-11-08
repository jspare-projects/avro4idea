package org.jspare.tools.avro4idea.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.compiler.idl.Idl;
import org.apache.avro.compiler.specific.SpecificCompiler;
import org.apache.avro.generic.GenericData;

import java.io.File;

public class AvroGenerateSourcesFromAvsc extends AnAction {

    private static final Logger log = Logger.getInstance(AvroGenerateSourcesFromAvsc.class);
    public static final String DEFAULT_TEMPLATE_DIR = "/org/apache/avro/compiler/specific/templates/java/classic/";

    @Override
    public void actionPerformed(AnActionEvent event) {

        VirtualFile vFile = FileEditorManager.getInstance(event.getProject()).getSelectedEditor().getFile();
        Schema parser;

        log.info("Generating sources from [" + vFile.getUrl() + "].");
        if (!"avdl".equals(vFile.getExtension())) {
            log.info("Conversion not supported for " + vFile.getExtension());
            return;
        }

        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        VirtualFile vOutput = FileChooser.chooseFile(descriptor, event.getProject(), null);

        ClassLoader oldContextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(Idl.class.getClassLoader());

            parser = new Schema.Parser().parse(new String(vFile.contentsToByteArray()));

            String json = parser.toString();
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
            log.error("Fail while generating sources from " + vFile.getUrl() + ".");
            log.error(e1.getMessage(), e1);
        } finally {
            Thread.currentThread().setContextClassLoader(oldContextClassLoader);
        }
    }

}
