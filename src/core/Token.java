package core;

public class Token {
    private int line;       // 行号
    private String word;    // 单词文本内容
    private int typeCode;   // 类别码（对应TokenType里的数字）

    public Token(int line, String word, int typeCode) {
        this.line = line;
        this.word = word;
        this.typeCode = typeCode;
    }

    // 为了方便在 UI 的 JTextArea 中显示，重写 toString
    @Override
    public String toString() {
        // 格式参考截图：行号:  单词  类别码
        return String.format("%d:\t%-15s\t%d", line, word, typeCode);
    }
}
