package org.jspare.tools.avro4idea;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jspare.tools.avro4idea.view.AvroWindowManager;

public class AvroEditorComponent extends AbstractProjectComponent {

    private static final String COMPONENT_NAME = "Avro";

    public AvroEditorComponent(Project project) {
        super(project);
    }

    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }


    public void projectOpened() {
        AvroWindowManager.getInstance(myProject);
    }

    public void projectClosed() {
        AvroWindowManager.getInstance(myProject).unregisterMyself();
    }
}
