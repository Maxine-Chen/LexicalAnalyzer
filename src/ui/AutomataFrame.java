package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * NFA/DFA/MFA 转换实验界面
 */
public class AutomataFrame extends JFrame {
    private JTextField regexField;
    private JTable nfaTable, dfaTable, mfaTable;

    public AutomataFrame() {
        setTitle("NFA_DFA_MFA 转换工具");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. 顶部表达式输入区
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("表达式"));
        regexField = new JTextField(30);
        JButton verifyBtn = new JButton("验证正规式");
        topPanel.add(new JLabel("请输入一个正规式："));
        topPanel.add(regexField);
        topPanel.add(verifyBtn);
        add(topPanel, BorderLayout.NORTH);

        // 2. 中间三个表格区
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        centerPanel.add(createTablePanel("正规式 -> NFA", nfaTable = new JTable()));
        centerPanel.add(createTablePanel("NFA -> DFA", dfaTable = new JTable()));
        centerPanel.add(createTablePanel("DFA -> MFA", mfaTable = new JTable()));
        add(centerPanel, BorderLayout.CENTER);

        // 3. 底部状态与操作区（这里简化，你可以参考截图继续添加按钮）
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JButton("生成NFA"));
        bottomPanel.add(new JButton("生成DFA"));
        bottomPanel.add(new JButton("生成MFA"));
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createTablePanel(String title, JTable table) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));

        // 设置表头
        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"起始状态", "接受符号", "到达状态"}
        ));

        p.add(new JScrollPane(table), BorderLayout.CENTER);

        // 底部状态显示
        JPanel statusP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusP.add(new JLabel("开始状态集: "));
        statusP.add(new JLabel("终结状态集: "));
        p.add(statusP, BorderLayout.SOUTH);

        return p;
    }
}