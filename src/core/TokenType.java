package core;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TokenType: 将正则表达式与 TokenConstants 中的类别码进行映射
 */
public class TokenType {

    // 存储 正则表达式 -> 类别码 的映射
    // 使用 LinkedHashMap 保证匹配顺序（优先级）
    public static final Map<String, Integer> RULES = new LinkedHashMap<>();

    static {
        // 1. 关键字 (Keywords) - 使用 \b 单词边界防止部分匹配
        RULES.put("\\bint\\b", TokenConstants.KEYWORD_INT);
        RULES.put("\\bfloat\\b", TokenConstants.KEYWORD_FLOAT);
        RULES.put("\\bchar\\b", TokenConstants.KEYWORD_CHAR);
        RULES.put("\\bvoid\\b", TokenConstants.KEYWORD_VOID);
        RULES.put("\\bif\\b", TokenConstants.KEYWORD_IF);
        RULES.put("\\belse\\b", TokenConstants.KEYWORD_ELSE);
        RULES.put("\\bwhile\\b", TokenConstants.KEYWORD_WHILE);
        RULES.put("\\bfor\\b", TokenConstants.KEYWORD_FOR);
        RULES.put("\\breturn\\b", TokenConstants.KEYWORD_RETURN);
        RULES.put("\\bbreak\\b", TokenConstants.KEYWORD_BREAK);
        RULES.put("\\bcontinue\\b", TokenConstants.KEYWORD_CONTINUE);
        RULES.put("\\bstruct\\b", TokenConstants.KEYWORD_STRUCT);
        RULES.put("\\btypedef\\b", TokenConstants.KEYWORD_TYPEDEF);
        RULES.put("\\bstatic\\b", TokenConstants.KEYWORD_STATIC);
        RULES.put("\\bconst\\b", TokenConstants.KEYWORD_CONST);

        // 2. 复合运算符 (Compound Operators) - 必须放在单符号运算符之前
        RULES.put("\\+\\+", TokenConstants.OP_INC);         // ++
        RULES.put("--", TokenConstants.OP_DEC);           // --
        RULES.put("==", TokenConstants.OP_EQ);            // ==
        RULES.put("!=", TokenConstants.OP_NE);            // !=
        RULES.put("<=", TokenConstants.OP_LE);            // <=
        RULES.put(">=", TokenConstants.OP_GE);            // >=
        RULES.put("&&", TokenConstants.OP_AND);           // &&
        RULES.put("\\|\\|", TokenConstants.OP_OR);        // ||
        RULES.put("<<", TokenConstants.OP_LEFT_SHIFT);    // <<
        RULES.put(">>", TokenConstants.OP_RIGHT_SHIFT);   // >>

        // 3. 单符号运算符 (Simple Operators)
        RULES.put("=", TokenConstants.OP_ASSIGN);
        RULES.put("\\+", TokenConstants.OP_ADD);
        RULES.put("-", TokenConstants.OP_SUB);
        RULES.put("\\*", TokenConstants.OP_MUL);
        RULES.put("/", TokenConstants.OP_DIV);
        RULES.put("%", TokenConstants.OP_MOD);
        RULES.put("<", TokenConstants.OP_LT);
        RULES.put(">", TokenConstants.OP_GT);
        RULES.put("!", TokenConstants.OP_NOT);
        RULES.put("&", TokenConstants.OP_BIT_AND);
        RULES.put("\\|", TokenConstants.OP_BIT_OR);
        RULES.put("\\^", TokenConstants.OP_BIT_XOR);
        RULES.put("~", TokenConstants.OP_BIT_NOT);

        // 4. 分隔符 (Delimiters)
        RULES.put(";", TokenConstants.SEMICOLON);
        RULES.put(",", TokenConstants.COMMA);
        RULES.put("\\.", TokenConstants.DOT);
        RULES.put("\\(", TokenConstants.LPAREN);
        RULES.put("\\)", TokenConstants.RPAREN);
        RULES.put("\\{", TokenConstants.LBRACE);
        RULES.put("\\}", TokenConstants.RBRACE);
        RULES.put("\\[", TokenConstants.LBRACKET);
        RULES.put("\\]", TokenConstants.RBRACKET);
        RULES.put("#", TokenConstants.PREPROCESSOR);

        // 5. 常量 (Constants)
        // 浮点数 (如 3.14)
        RULES.put("\\b\\d+\\.\\d+\\b", TokenConstants.FLOAT_CONST);
        // 整数
        RULES.put("\\b\\d+\\b", TokenConstants.INT_CONST);
        // 字符串常量 (如 "hello")
        RULES.put("\".*?\"", TokenConstants.STRING_CONST);
        // 字符常量 (如 'a')
        RULES.put("'.'", TokenConstants.CHAR_CONST);

        // 6. 标识符 (Identifier) - 放在最后，确保不与关键字冲突
        RULES.put("\\b[a-zA-Z_][a-zA-Z0-9_]*\\b", TokenConstants.IDENTIFIER);
    }
}