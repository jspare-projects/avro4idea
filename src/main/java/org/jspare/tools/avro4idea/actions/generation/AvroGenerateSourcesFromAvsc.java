package org.jspare.tools.avro4idea.actions.generation;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.avro.Protocol;
import org.apache.avro.Schema;
import org.apache.avro.compiler.idl.ParseException;
import org.apache.avro.compiler.specific.SpecificCompiler;
import org.apache.avro.generic.GenericData;

import java.io.File;
import java.io.IOException;

public class AvroGenerateSourcesFromAvsc extends AbstractAvroGenerateSource {

    private static final Logger log = Logger.getInstance(AvroGenerateSourcesFromAvsc.class);
    public static final String DEFAULT_TEMPLATE_DIR = "/org/apache/avro/compiler/specific/templates/java/classic/";

    @Override
    protected void doIt(VirtualFile vInput, VirtualFile vOutput) throws IOException, ParseException {
        log.info("Generating sources from [" + vInput.getUrl() + "].");
        if (!"avdl".equals(vInput.getExtension())) {
            log.info("Conversion not supported for " + vInput.getExtension());
            return;
        }

        Schema parser = new Schema.Parser().parse(new String(vInput.contentsToByteArray()));

        String json = parser.toString();
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
