package com.program;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ToDoListToolWindow implements Disposable {
    private final JPanel contentPanel;
    private final JTextField taskInputField;
    private final JPanel taskListPanel;
    private final List<JCheckBox> taskCheckBoxes = new ArrayList<>();
    private int currentFontSize;

    public ToDoListToolWindow(int initialFontSize) {
        this.currentFontSize = initialFontSize;
        this.contentPanel = new JPanel(new BorderLayout());
        this.taskInputField = new JTextField();
        this.taskListPanel = new JPanel();

        initializeUI();
    }

    private void initializeUI() {
        contentPanel.add(createTopPanel(), BorderLayout.NORTH);
        contentPanel.add(createTaskListScrollPane(), BorderLayout.CENTER);
        contentPanel.add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        taskInputField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JButton increaseFontButton = new JButton("A+");
        JButton decreaseFontButton = new JButton("A-");

        increaseFontButton.addActionListener(e -> adjustFontSize(2));
        decreaseFontButton.addActionListener(e -> adjustFontSize(-2));

        JPanel fontPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        fontPanel.add(increaseFontButton);
        fontPanel.add(decreaseFontButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(5, 5, 2, 5));
        panel.add(taskInputField);
        panel.add(fontPanel);

        return panel;
    }

    private JBScrollPane createTaskListScrollPane() {
        // Layout modificado para espaçamento compacto
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS));
        taskListPanel.setBorder(JBUI.Borders.empty(2, 5));

        // Forçar alinhamento ao topo
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(taskListPanel, BorderLayout.NORTH);

        JBScrollPane scrollPane = new JBScrollPane(wrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Mesmo scroll rate do editor
        return scrollPane;
    }

    private JPanel createBottomPanel() {
        JButton addButton = new JButton("Adicionar", AllIcons.General.Add);
        JButton deleteButton = new JButton("Deletar", AllIcons.General.Remove);

        // Tamanho compacto
        addButton.setMargin(new Insets(2, 8, 2, 8));
        deleteButton.setMargin(new Insets(2, 8, 2, 8));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
        panel.add(addButton);
        panel.add(deleteButton);
        panel.setBorder(new EmptyBorder(2, 5, 5, 5));

        addButton.addActionListener(this::addTask);
        deleteButton.addActionListener(this::deleteSelectedTasks);

        taskInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) addTask(null);
            }
        });

        return panel;
    }

    private void addTask(ActionEvent e) {
        String taskText = taskInputField.getText().trim();
        if (!taskText.isEmpty()) {
            JCheckBox checkBox = createTaskCheckBox(taskText);
            taskCheckBoxes.add(checkBox);

            // Adicionar diretamente sem painel extra
            checkBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            taskListPanel.add(checkBox);

            // Espaçamento entre itens igual ao do editor (4px)
            taskListPanel.add(Box.createRigidArea(new Dimension(0, 4)));

            taskInputField.setText("");
            taskListPanel.revalidate();
            taskListPanel.repaint();
        }
    }

    private JCheckBox createTaskCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setFont(checkBox.getFont().deriveFont(Font.PLAIN, currentFontSize));
        checkBox.setBorder(new EmptyBorder(2, 0, 2, 0)); // Reduzir espaçamento interno

        checkBox.addActionListener(e -> {
            if (checkBox.isSelected()) {
                checkBox.setText("<html><strike>" + text + "</strike></html>");
            } else {
                checkBox.setText(text);
            }
        });

        return checkBox;
    }

    private void deleteSelectedTasks(ActionEvent e) {
        taskCheckBoxes.removeIf(checkBox -> {
            if (checkBox.isSelected()) {
                Container parent = checkBox.getParent();
                taskListPanel.remove(parent);
                return true;
            }
            return false;
        });

        taskListPanel.revalidate();
        taskListPanel.repaint();
    }

    private void adjustFontSize(int delta) {
        currentFontSize = Math.max(8, currentFontSize + delta);
        for (JCheckBox checkBox : taskCheckBoxes) {
            checkBox.setFont(checkBox.getFont().deriveFont((float) currentFontSize));
        }
    }

    public JComponent getContent() {
        return contentPanel;
    }

    @Override
    public void dispose() {
        // Cleanup se necessário
    }
}