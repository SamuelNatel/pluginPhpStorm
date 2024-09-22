package com.example.todolist;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import javax.swing.*;

public class ToDoListToolWindow
{
    private JPanel contentPanel;
    private JButton addButton;
    private JTextField taskInputField;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;

    public ToDoListToolWindow(ToolWindow toolWindow, Project project)
    {
        listModel = new DefaultListModel<>();
        taskList.setModel(listModel);

        addButton.addActionListener(e -> {
           String task = taskInputField.getText();
           if (!task.isEmpty())
           {
               listModel.addElement(task);
               taskInputField.setText("");
           }
        });
    }

    public JPanel getContent()
    {
        return contentPanel;
    }

    private void createUIComponents() {
        taskInputField = new JTextField();
        addButton = new JButton("Add Task");
        taskList = new JList<>();
    }
}