import ui.MainFrame;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 设置界面外观为系统原生样式
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainFrame frame = new MainFrame();
        frame.setVisible(true);

    }
}