package service;

import javax.swing.*;
import javax.swing.JFileChooser;
import java.io.*;
import java.nio.file.Files;
import java.awt.*;

/**
 * 文件服务类：处理文件的打开、读取和保存逻辑
 */
public class FileService {

    private JFileChooser fileChooser;

    public FileService() {
        // 初始化文件选择器，默认指向当前项目目录
        fileChooser = new JFileChooser(new File("."));
    }

    /**
     * 打开文件对话框并读取内容
     * @param parent 父窗口组件（用于居中显示对话框）
     * @return 读取到的文件内容，如果取消或失败则返回 null
     */
    public String openFile(Component parent) {
        int result = fileChooser.showOpenDialog(parent);
        if (result == 0) {  //不知道为什么JFileChooser.APPROVE_VALUE无法使用，暂时用0代替
            File selectedFile = fileChooser.getSelectedFile();
            try {
                byte[] bytes = Files.readAllBytes(selectedFile.toPath());
                return new String(bytes, "UTF-8");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "文件读取失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

    /**
     * 保存内容到文件（弹出另存为对话框）
     * @param parent 父窗口组件
     * @param content 要保存的文本内容
     */
    public void saveFile(Component parent, String content) {
        int result = fileChooser.showSaveDialog(parent);
        if (result == 0) {  //不知道为什么JFileChooser.APPROVE_VALUE无法使用，暂时用0代替
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(file, "UTF-8")) {
                writer.print(content);
                JOptionPane.showMessageDialog(parent, "保存成功！");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "文件保存失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
