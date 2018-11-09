package org.jspare.tools.avro4idea.actions.generation;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.Protocol;
import org.apache.avro.compiler.idl.Idl;
import org.apache.avro.compiler.idl.ParseException;
import org.apache.avro.compiler.specific.SpecificCompiler;
import org.apache.avro.generic.GenericData;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class AvroGenerateSourcesFromAvdl extends AbstractAvroGenerateSource {

    private static final Logger log = Logger.getInstance(AvroGenerateSourcesFromAvdl.class);
    public static final String DEFAULT_TEMPLATE_DIR = "/org/apache/avro/compiler/specific/templates/java/classic/";

    @Override
    protected void doIt(VirtualFile vInput, VirtualFile vOutput) throws IOException, ParseException {

        log.info("Converting [" + vInput.getUrl() + "] to avsc.");
        if (!"avdl".equals(vInput.getExtension())) {
            log.info("Conversion not supported for " + vInput.getExtension());
            return;
        }

        URL url = new URL(vInput.getUrl());
        File file = new File(vInput.getCanonicalPath());

        URLClassLoader projectPathLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
        Idl parser = new Idl(file, projectPathLoader);

        Protocol p = parser.CompilationUnit();
        String json = p.toString(true);
        Protocol protocol = Protocol.parse(json);
        SpecificCompiler compiler = new SpecificCompiler(protocol);
        compiler.setStringType(GenericData.StringType.String);
        compiler.setTemplateDir(DEFAULT_TEMPLATE_DIR);
        compiler.setFieldVisibility(SpecificCompiler.FieldVisibility.PUBLIC);
        compiler.setCreateSetters(true);
        compiler.setEnableDecimalLogicalType(false);
        compiler.setOutputCharacterEncoding(vInput.getCharset().name());
        compiler.compileToDestination(null, new File(vOutput.getCanonicalPath()));
    }
}
