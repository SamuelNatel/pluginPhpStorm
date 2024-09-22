package com.example.todolist;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ToDoListToolWindowFactory implements ToolWindowFactory
{
    private int currentFontSize = 12; // Tamanho inicial da fonte

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull com.intellij.openapi.wm.ToolWindow toolWindow) {
        JPanel todoPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("ToDo List", SwingConstants.CENTER);
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

        // Painel onde as tarefas aparecerão
        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new GridLayout(0, 1)); // Usar GridLayout para melhor espaçamento
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

        // Ação do botão Add Task
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTask(taskField, taskListPanel);
            }
        });

        // Ação do botão Delete Selected
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component component : taskListPanel.getComponents()) {
                    if (component instanceof JPanel) {
                        JPanel taskPanel = (JPanel) component;
                        JCheckBox checkBox = (JCheckBox) taskPanel.getComponent(0);
                        if (checkBox.isSelected()) {
                            taskListPanel.remove(taskPanel);
                        }
                    }
                }
                taskListPanel.revalidate();
                taskListPanel.repaint();
            }
        });

        // Adiciona a task ao pressionar Enter
        taskField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    addTask(taskField, taskListPanel);
                }
            }
        });

        // Ações para os botões de ajuste de fonte
        increaseFontButton.addActionListener(e -> adjustFontSize(taskListPanel, 1));
        decreaseFontButton.addActionListener(e -> adjustFontSize(taskListPanel, -1));

        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content = contentFactory.createContent(todoPanel, "", false);
        toolWindow.getContentManager().addContent(content);
    }

    private void addTask(JTextField taskField, JPanel taskListPanel) {
        String taskText = taskField.getText();
        if (!taskText.isEmpty()) {
            JPanel taskPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Centraliza a task
            JCheckBox checkBox = new JCheckBox(taskText);
            checkBox.setFont(new Font(checkBox.getFont().getName(), checkBox.getFont().getStyle(), currentFontSize));

            // Adiciona ação para riscar a tarefa ao ser marcada
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    checkBox.setText("<html><strike>" + taskText + "</strike></html>");
                } else {
                    checkBox.setText(taskText);
                }
            });

            taskPanel.add(checkBox);
            taskListPanel.add(taskPanel);
            taskListPanel.revalidate();
            taskListPanel.repaint();
            taskField.setText("");
        }
    }

    private void adjustFontSize(JPanel taskListPanel, int increment) {
        currentFontSize += increment;
        for (Component component : taskListPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel taskPanel = (JPanel) component;
                JCheckBox checkBox = (JCheckBox) taskPanel.getComponent(0);
                checkBox.setFont(new Font(checkBox.getFont().getName(), checkBox.getFont().getStyle(), currentFontSize));
            }
        }
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }
}