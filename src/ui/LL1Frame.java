package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * LL(1) 预测分析实验界面
 */
public class LL1Frame extends JFrame {

    // --- UI 组件定义 ---
    private JTextArea grammarArea;         // 原始文法输入
    private JTable firstTable;             // First集表格
    private JTable followTable;            // Follow集表格
    private JTable predictTable;           // 预测分析表
    private JTable processTable;           // 分析过程表格
    private JTextField sentenceField;      // 待分析句子输入框

    // --- 按钮定义 ---
    private JButton btnOpen, btnConfirm, btnSave, btnBuildTable, btnExit;
    private JButton btnSolveFirst, btnSolveFollow;
    private JButton btnRunAll, btnRunStep;

    public LL1Frame() {
        setTitle("LL(1) 预测分析");
        setSize(1250, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initUI();
        initListeners();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // 1. 顶部工具栏 (Top Buttons)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnOpen = new JButton("打开文件");
        btnConfirm = new JButton("确认文法");
        btnSave = new JButton("保存文件");
        btnBuildTable = new JButton("构造预测分析表");
        btnExit = new JButton("退出");

        topPanel.add(btnOpen);
        topPanel.add(btnConfirm);
        topPanel.add(btnSave);
        topPanel.add(Box.createHorizontalStrut(100)); // 占位间距
        topPanel.add(btnBuildTable);
        topPanel.add(btnExit);
        add(topPanel, BorderLayout.NORTH);

        // 2. 主体面板：左右并排
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // ======================== 左侧面板 ========================
        JPanel leftPanel = new JPanel(new BorderLayout());

        // (1) 原始文法输入区
        grammarArea = new JTextArea();
        grammarArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane grammarScroll = new JScrollPane(grammarArea);
        grammarScroll.setBorder(BorderFactory.createTitledBorder("原始文法：请输入形如E->TB的LL(1)文法，其中空字符用$代替"));
        grammarScroll.setPreferredSize(new Dimension(0, 250));
        leftPanel.add(grammarScroll, BorderLayout.NORTH);

        // (2) 中间：First集 和 Follow集 展示表格
        JPanel setTablesPanel = new JPanel(new GridLayout(2, 1, 0, 5));

        // First集表格
        firstTable = new JTable(new DefaultTableModel(null, new String[]{"First集", "+", "$", "*", "(", ")", "i"}));
        JScrollPane firstScroll = new JScrollPane(firstTable);
        firstScroll.setBorder(BorderFactory.createTitledBorder("First集展示"));

        // Follow集表格
        followTable = new JTable(new DefaultTableModel(null, new String[]{"Follow集", "+", "*", "(", ")", "i", "#"}));
        JScrollPane followScroll = new JScrollPane(followTable);
        followScroll.setBorder(BorderFactory.createTitledBorder("Follow集展示"));

        setTablesPanel.add(firstScroll);
        setTablesPanel.add(followScroll);
        leftPanel.add(setTablesPanel, BorderLayout.CENTER);

        // (3) 左侧底部：求集按钮
        JPanel leftBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnSolveFirst = new JButton("求First集");
        btnSolveFollow = new JButton("求Follow集");
        leftBottomPanel.add(btnSolveFirst);
        leftBottomPanel.add(btnSolveFollow);
        leftPanel.add(leftBottomPanel, BorderLayout.SOUTH);

        // ======================== 右侧面板 ========================
        JPanel rightPanel = new JPanel(new BorderLayout());

        // (1) 预测分析表区
        predictTable = new JTable(new DefaultTableModel(null, new String[]{"预测表", "+", "*", "(", ")", "i", "#"}));
        JScrollPane predictScroll = new JScrollPane(predictTable);
        predictScroll.setBorder(BorderFactory.createTitledBorder("预测分析表展示"));
        predictScroll.setPreferredSize(new Dimension(0, 250));
        rightPanel.add(predictScroll, BorderLayout.NORTH);

        // (2) 分析过程区
        JPanel processPanel = new JPanel(new BorderLayout());

        // 分析输入控制行
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sentenceField = new JTextField(20);
        btnRunAll = new JButton("一键显示");
        btnRunStep = new JButton("单步显示");
        inputPanel.add(new JLabel("分析句子："));
        inputPanel.add(sentenceField);
        inputPanel.add(btnRunAll);
        inputPanel.add(btnRunStep);
        processPanel.add(inputPanel, BorderLayout.NORTH);

        // 过程展示表
        processTable = new JTable(new DefaultTableModel(null, new String[]{"步骤", "符号栈", "输入串", "所用产生式"}));
        JScrollPane processScroll = new JScrollPane(processTable);
        processScroll.setBorder(BorderFactory.createTitledBorder("LL(1) 预测分析过程记录"));
        processPanel.add(processScroll, BorderLayout.CENTER);

        rightPanel.add(processPanel, BorderLayout.CENTER);

        // 组合并添加
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void initListeners() {
        // 退出
        btnExit.addActionListener(e -> this.dispose());

        // 求First集按钮逻辑占位
        btnSolveFirst.addActionListener(e -> {
            String text = grammarArea.getText().trim();
            if(text.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请先输入文法！");
                return;
            }
            System.out.println("执行求 First 集逻辑...");
        });

        // 求Follow集按钮逻辑占位
        btnSolveFollow.addActionListener(e -> {
            System.out.println("执行求 Follow 集逻辑...");
        });

        // 构造预测表按钮逻辑占位
        btnBuildTable.addActionListener(e -> {
            System.out.println("执行构建预测分析表逻辑...");
        });

        // 一键显示逻辑占位
        btnRunAll.addActionListener(e -> {
            String sentence = sentenceField.getText().trim();
            if(sentence.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入待分析的句子！");
                return;
            }
            System.out.println("开始一键分析句子: " + sentence);
        });
    }
}