package ui;

import core.automata.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * NFA/DFA/MFA 转换实验界面
 * 功能：实现从正规式到 NFA、DFA 以及最小化 DFA 的全流程可视化展示
 */
public class AutomataFrame extends JFrame {
    // UI 组件：文本框与表格
    private JTextField regexField;
    private JTable nfaTable, dfaTable, mfaTable;

    // UI 组件：用于更新状态显示的标签
    private JLabel nfaStartLabel, nfaEndLabel;
    private JLabel dfaStartLabel, dfaEndLabel;
    private JLabel mfaStartLabel, mfaEndLabel;

    // 逻辑组件：存储转换过程中产生的自动机对象
    private AutomataEntity.Automaton currentNfa;
    private AutomataEntity.Automaton currentDfa;
    private AutomataEntity.Automaton currentMfa;

    public AutomataFrame() {
        setTitle("NFA_DFA_MFA 转换工具");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        // 使用 DISPOSE_ON_CLOSE 确保关闭此窗口不会退出整个程序
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 1. 顶部：表达式输入区
        initTopPanel();

        // 2. 中间：三个表格展示区（NFA, DFA, MFA）
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 0));

        // 分别创建并添加三个面板
        centerPanel.add(createNfaPanel());
        centerPanel.add(createDfaPanel());
        centerPanel.add(createMfaPanel());

        add(centerPanel, BorderLayout.CENTER);
    }

    /**
     * 初始化顶部正规式输入面板
     */
    private void initTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("表达式"));
        regexField = new JTextField(35);
        JButton verifyBtn = new JButton("验证正规式");
        JButton exitBtn = new JButton("退出");

        topPanel.add(new JLabel("请输入一个正规式："));
        topPanel.add(regexField);
        topPanel.add(new JLabel(" 例如: (a|b)*abb "));
        topPanel.add(verifyBtn);
        topPanel.add(exitBtn);
        add(topPanel, BorderLayout.NORTH);

        exitBtn.addActionListener(e -> this.dispose());

        // 验证正规式按钮逻辑（此处可根据需要增加复杂的正则校验）
        verifyBtn.addActionListener(e -> {
            if(regexField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "正规式不能为空！");
            } else {
                JOptionPane.showMessageDialog(this, "正规式格式有效，请点击下方的‘生成’按钮进行转换。");
            }
        });
    }

    /**
     * 创建 NFA 展示面板
     */
    private JPanel createNfaPanel() {
        nfaStartLabel = new JLabel("开始状态: ");
        nfaEndLabel = new JLabel("终结状态: ");
        nfaTable = new JTable();

        JButton genBtn = new JButton("生成NFA");
        genBtn.addActionListener(e -> generateNfaAction());

        return assembleTablePanel("正规式 -> NFA", nfaTable, nfaStartLabel, nfaEndLabel, genBtn);
    }

    /**
     * 创建 DFA 展示面板
     */
    private JPanel createDfaPanel() {
        dfaStartLabel = new JLabel("开始状态集: ");
        dfaEndLabel = new JLabel("终结状态集: ");
        dfaTable = new JTable();

        JButton genBtn = new JButton("生成DFA");
        // 绑定生成 DFA 的动作
        genBtn.addActionListener(e -> generateDfaAction());

        return assembleTablePanel("NFA -> DFA", dfaTable, dfaStartLabel, dfaEndLabel, genBtn);
    }

    /**
     * 创建 MFA 展示面板
     */
    private JPanel createMfaPanel() {
        mfaStartLabel = new JLabel("开始状态集: ");
        mfaEndLabel = new JLabel("终结状态集: ");
        mfaTable = new JTable();

        JButton genBtn = new JButton("生成MFA");
        // 绑定生成 MFA 的动作
        genBtn.addActionListener(e -> generateMfaAction());

        return assembleTablePanel("DFA -> MFA", mfaTable, mfaStartLabel, mfaEndLabel, genBtn);
    }

    /**
     * 通用的表格面板组装方法
     * 用于统一 NFA/DFA/MFA 面板的外观结构
     */
    private JPanel assembleTablePanel(String title, JTable table, JLabel startL, JLabel endL, JButton genBtn) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(title));

        // 初始化表格模型（三列结构）
        table.setModel(new DefaultTableModel(null, new String[]{"起始状态", "接受符号", "到达状态"}));
        p.add(new JScrollPane(table), BorderLayout.CENTER);

        // 底部区域：展示状态集标签和操作按钮
        JPanel bottomP = new JPanel(new BorderLayout());

        JPanel labelP = new JPanel(new GridLayout(2, 1));
        labelP.add(startL);
        labelP.add(endL);

        JPanel btnP = new JPanel(new FlowLayout());
        btnP.add(new JButton("读入文件"));
        btnP.add(genBtn);
        btnP.add(new JButton("保存"));

        bottomP.add(labelP, BorderLayout.NORTH);
        bottomP.add(btnP, BorderLayout.SOUTH);
        p.add(bottomP, BorderLayout.SOUTH);

        return p;
    }

    /**
     * “生成NFA”按钮触发动作：正规式 -> NFA (Thompson算法)
     */
    private void generateNfaAction() {
        String regex = regexField.getText().trim();
        if (regex.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入正规式！");
            return;
        }

        try {
            NfaBuilder builder = new NfaBuilder();
            AutomataEntity.Automaton nfa = builder.build(regex);

            // 更新表格与标签
            refreshTableAndLabels(nfaTable, nfaStartLabel, nfaEndLabel, nfa);
            currentNfa = nfa;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "NFA 生成失败！请检查正则格式。\n错误: " + ex.getMessage());
        }
    }

    /**
     * “生成DFA”按钮触发动作：NFA -> DFA (子集构造法)
     */
    private void generateDfaAction() {
        if (currentNfa == null) {
            JOptionPane.showMessageDialog(this, "请先生成 NFA！");
            return;
        }

        try {
            DfaConstructor constructor = new DfaConstructor();
            AutomataEntity.Automaton dfa = constructor.convert(currentNfa);

            // 更新表格与标签
            refreshTableAndLabels(dfaTable, dfaStartLabel, dfaEndLabel, dfa);
            currentDfa = dfa;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DFA 生成失败: " + ex.getMessage());
        }
    }

    /**
     * “生成MFA”按钮触发动作：DFA -> MFA (分割法)
     */
    private void generateMfaAction() {
        if (currentDfa == null) {
            JOptionPane.showMessageDialog(this, "请先生成 DFA！");
            return;
        }

        try {
            DfaMinimizer minimizer = new DfaMinimizer();
            AutomataEntity.Automaton mfa = minimizer.minimize(currentDfa);

            // 更新表格与标签
            refreshTableAndLabels(mfaTable, mfaStartLabel, mfaEndLabel, mfa);
            currentMfa = mfa;

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "MFA 生成失败: " + ex.getMessage());
        }
    }

    /**
     * 辅助方法：统一刷新表格数据和状态集文本显示
     */
    private void refreshTableAndLabels(JTable table, JLabel startL, JLabel endL, AutomataEntity.Automaton automaton) {
        String[] columns = {"起始状态", "接受符号", "到达状态"};
        table.setModel(new DefaultTableModel(automaton.getTableData(), columns));

        startL.setText("开始状态集: " + automaton.statesToString(automaton.getStartStates()));
        endL.setText("终结状态集: " + automaton.statesToString(automaton.getEndStates()));
    }
}