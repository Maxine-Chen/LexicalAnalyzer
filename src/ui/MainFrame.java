package ui;

import javax.swing.*;
import java.awt.*;

import core.Lexer;
import core.Token;
import service.FileService;



public class MainFrame extends JFrame {

    private JTextArea sourceEditor;    // 左侧源码编辑区
    private JTextArea tokenTableArea;  // 右上Token序列区
    private JTextArea errorLogArea;    // 右下错误日志区

    private JMenuItem openFileMenu;    // 菜单：打开
    private JMenuItem saveFileMenu;    // 菜单：保存
    private JButton runBtn;            // 工具栏：分析按钮
    private JButton clearBtn;          // 工具栏：清空按钮
    private JButton automataBtn;       // 工具栏：NFA_DFA_MFA 转换按钮

    public MainFrame() {
        // 设置窗口基本属性
        setTitle("词法分析器教学辅助系统");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 窗口居中

        // 初始化
        initMenuBar();
        initToolBar();
        initMainLayout();
        initListeners();
    }


    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        openFileMenu = new JMenuItem("打开文件");
        saveFileMenu = new JMenuItem("保存文件");
        JMenuItem exitMenu = new JMenuItem("退出");
        fileMenu.add(openFileMenu);
        fileMenu.add(saveFileMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitMenu);

        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }


    private void initToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false); // 固定工具栏

        runBtn = new JButton("词法分析");
        automataBtn = new JButton("NFA_DFA_MFA转换");
        clearBtn = new JButton("清空内容");


        toolBar.add(runBtn);
        toolBar.add(automataBtn); // 并排添加
        toolBar.addSeparator();
        toolBar.add(clearBtn);

        this.add(toolBar, BorderLayout.NORTH);
    }

    private void initMainLayout() {
        // 源码编辑区
        sourceEditor = new JTextArea();
        sourceEditor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        sourceEditor.setTabSize(4);
        JScrollPane leftScroll = new JScrollPane(sourceEditor);
        leftScroll.setBorder(BorderFactory.createTitledBorder("源程序输入区"));

        // Token表和错误区
        tokenTableArea = new JTextArea();
        tokenTableArea.setEditable(false);
        tokenTableArea.setBackground(new Color(245, 245, 245));
        tokenTableArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane tokenScroll = new JScrollPane(tokenTableArea);
        tokenScroll.setBorder(BorderFactory.createTitledBorder("Token表信息"));

        errorLogArea = new JTextArea();
        errorLogArea.setEditable(false);
        errorLogArea.setForeground(Color.RED);
        JScrollPane errorScroll = new JScrollPane(errorLogArea);
        errorScroll.setBorder(BorderFactory.createTitledBorder("词法分析错误信息"));

        // 右侧上下分割
        JSplitPane rightSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tokenScroll, errorScroll);
        rightSplit.setDividerLocation(400); // 初始高度

        // 总体左右分割
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightSplit);
        mainSplit.setDividerLocation(450); // 初始宽度

        this.add(mainSplit, BorderLayout.CENTER);
    }



    // 1. 在 MainFrame 类中添加成员变量
    private FileService fileService = new FileService();

    // 2. 创建一个方法来绑定所有事件监听器
    private void initListeners() {
        // --- “打开文件”监听 ---
        openFileMenu.addActionListener(e -> {
            String content = fileService.openFile(this);
            if (content != null) {
                sourceEditor.setText(content); // 将读取的内容放入编辑器
            }
        });

        // --- “保存文件”监听 ---
        saveFileMenu.addActionListener(e -> {
            String content = sourceEditor.getText();
            if (content.isEmpty()) {
                JOptionPane.showMessageDialog(this, "内容为空，无需保存");
                return;
            }
            fileService.saveFile(this, content);
        });

        // --- “清空内容”监听 ---
        clearBtn.addActionListener(e -> {
            sourceEditor.setText("");
            tokenTableArea.setText("");
            errorLogArea.setText("");
        });

        // --- “开始分析”监听 ---
        runBtn.addActionListener(e -> {
            String code = sourceEditor.getText();
            if (code.trim().isEmpty()) {
                errorLogArea.setText("错误：请输入源代码再进行分析！");
                return;
            }
            // 1. 从界面获取源码
            String inputCode = sourceEditor.getText();
            // 2. 实例化并运行 Lexer
            Lexer lexer = new Lexer(inputCode);
            lexer.analyze();
            // 3. 将 Token 序列显示到右上角窗口
            StringBuilder tokenOutput = new StringBuilder();
            tokenOutput.append("行号:\t单词文本\t\t类别码\n");
            tokenOutput.append("------------------------------------------\n");
            for (Token t : lexer.getTokens()) {
                tokenOutput.append(t.toString()).append("\n");
            }
            tokenTableArea.setText(tokenOutput.toString());
            // 4. 将错误信息显示到右下角窗口
            errorLogArea.setText(lexer.getErrorLog());
        });

        automataBtn.addActionListener(e -> {
            AutomataFrame automataFrame = new AutomataFrame();
            automataFrame.setVisible(true);
        });
    }


}

