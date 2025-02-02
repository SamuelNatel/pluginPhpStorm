package com.program;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class ToDoListToolWindowFactory implements ToolWindowFactory {
    private int currentFontSize = 12;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ToDoListToolWindow toolWindowContent = new ToDoListToolWindow(currentFontSize);
        Content content = ContentFactory.getInstance().createContent(
                toolWindowContent.getContent(),
                "",
                false
        );
        toolWindow.getContentManager().addContent(content);
    }
}