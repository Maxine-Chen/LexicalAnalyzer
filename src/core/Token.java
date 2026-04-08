package core;


//Token 类：用于存储词法分析出来的每一个单词单元的信息
public class Token {
    private int line;       // 行号
    private String lexeme;  // 单词文本（源码中的原始字符串）
    private int typeCode;   // 类别码（来自 TokenConstants）

    public Token(int line, String lexeme, int typeCode) {
        this.line = line;
        this.lexeme = lexeme;
        this.typeCode = typeCode;
    }

    public int getLine() { return line; }
    public String getLexeme() { return lexeme; }
    public int getTypeCode() { return typeCode; }


    @Override
    public String toString() {
        return String.format("%-4d:\t%-15s\t%d", line, lexeme, typeCode);
    }
}