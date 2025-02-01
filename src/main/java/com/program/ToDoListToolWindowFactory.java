package com.program;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ToDoListToolWindowFactory implements ToolWindowFactory {

    private int currentFontSize = 12;
    private final List<JCheckBox> taskCheckBoxes = new ArrayList<>();

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        JPanel todoPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("To-Do List", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        todoPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        JTextField taskField = new JTextField(20);
        taskField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        inputPanel.add(taskField);

        JPanel fontPanel = new JPanel(new FlowLayout());
        JButton increaseFontButton = new JButton("A+");
        JButton decreaseFontButton = new JButton("A-");

        fontPanel.add(increaseFontButton);
        fontPanel.add(decreaseFontButton);
        inputPanel.add(fontPanel);

        todoPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        todoPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Add Task");
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Selected");
        buttonPanel.add(deleteButton);

        todoPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addTask(taskField, taskListPanel));
        deleteButton.addActionListener(e -> deleteSelectedTasks(taskListPanel));

        taskField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addTask(taskField, taskListPanel);
                }
            }
        });

        increaseFontButton.addActionListener(e -> adjustFontSize(taskListPanel, 1));
        decreaseFontButton.addActionListener(e -> adjustFontSize(taskListPanel, -1));

        ContentFactory contentFactory = ApplicationManager.getApplication().getService(ContentFactory.class);
        Content content = contentFactory.createContent(todoPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private void addTask(JTextField taskField, JPanel taskListPanel) {
        String taskText = taskField.getText();
        if (!taskText.isEmpty()) {
            JPanel taskPanel = new JPanel();
            taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.X_AXIS));
            taskPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JCheckBox checkBox = new JCheckBox(taskText);
            checkBox.setFont(new Font(checkBox.getFont().getName(), checkBox.getFont().getStyle(), currentFontSize));

            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    checkBox.setText("<html><strike>" + taskText + "</strike></html>");
                } else {
                    checkBox.setText(taskText);
                }
            });

            taskPanel.add(checkBox);
            taskListPanel.add(taskPanel);
            taskCheckBoxes.add(checkBox);

            taskListPanel.revalidate();
            taskListPanel.repaint();
            taskField.setText("");
        }
    }

    private void deleteSelectedTasks(JPanel taskListPanel) {
        taskCheckBoxes.removeIf(checkBox -> {
            if (checkBox.isSelected()) {
                Component parent = checkBox.getParent();
                taskListPanel.remove(parent);
                return true;
            }
            return false;
        });

        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    private void adjustFontSize(JPanel taskListPanel, int increment) {
        currentFontSize += increment;
        for (JCheckBox checkBox : taskCheckBoxes) {
            checkBox.setFont(new Font(checkBox.getFont().getName(), checkBox.getFont().getStyle(), currentFontSize));
        }
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }
}