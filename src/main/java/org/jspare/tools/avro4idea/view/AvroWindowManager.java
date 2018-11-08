package org.jspare.tools.avro4idea.view;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;

public class AvroWindowManager {

    private static final String AVRO_EXPLORER = "Avro Explorer";

    private final Project project;

    public static AvroWindowManager getInstance(Project project) {
        return ServiceManager.getService(project, AvroWindowManager.class);
    }

    private AvroWindowManager(Project project) {
        this.project = project;

        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolAvroExplorerWindow = toolWindowManager.registerToolWindow(AVRO_EXPLORER, false, ToolWindowAnchor.RIGHT, project, true);
    }

    public void unregisterMyself() {
        ToolWindowManager.getInstance(project).unregisterToolWindow(AVRO_EXPLORER);
    }
}
