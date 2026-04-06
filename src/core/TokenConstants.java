package core;


// 定义词法分析器使用的Token类型常量

public class TokenConstants {
    // 特殊标识
    public static final int TOKEN_EOF = 0;
    public static final int TOKEN_ERROR = 1;

    // 关键字 (100-199)
    public static final int KEYWORD_INT = 100;
    public static final int KEYWORD_FLOAT = 101;
    public static final int KEYWORD_CHAR = 102;
    public static final int KEYWORD_VOID = 103;
    public static final int KEYWORD_IF = 104;
    public static final int KEYWORD_ELSE = 105;
    public static final int KEYWORD_WHILE = 106;
    public static final int KEYWORD_FOR = 107;
    public static final int KEYWORD_RETURN = 108;
    public static final int KEYWORD_BREAK = 109;
    public static final int KEYWORD_CONTINUE = 110;
    public static final int KEYWORD_STRUCT = 111;
    public static final int KEYWORD_TYPEDEF = 112;
    public static final int KEYWORD_STATIC = 113;
    public static final int KEYWORD_CONST = 114;

    // 标识符和常量 (200-299)
    public static final int IDENTIFIER = 200;
    public static final int INT_CONST = 201;
    public static final int FLOAT_CONST = 202;
    public static final int STRING_CONST = 203;
    public static final int CHAR_CONST = 204;

    // 运算符 (300-399)
    public static final int OP_ASSIGN = 300;      // =
    public static final int OP_ADD = 301;         // +
    public static final int OP_SUB = 302;         // -
    public static final int OP_MUL = 303;         // *
    public static final int OP_DIV = 304;         // /
    public static final int OP_MOD = 305;         // %
    public static final int OP_INC = 306;         // ++
    public static final int OP_DEC = 307;         // --

    // 比较运算符 (350-359)
    public static final int OP_EQ = 350;          // ==
    public static final int OP_NE = 351;          // !=
    public static final int OP_LT = 352;          // <
    public static final int OP_GT = 353;          // >
    public static final int OP_LE = 354;          // <=
    public static final int OP_GE = 355;          // >=

    // 逻辑运算符 (360-369)
    public static final int OP_AND = 360;         // &&
    public static final int OP_OR = 361;          // ||
    public static final int OP_NOT = 362;         // !

    // 位运算符 (370-379)
    public static final int OP_BIT_AND = 370;     // &
    public static final int OP_BIT_OR = 371;      // |
    public static final int OP_BIT_XOR = 372;     // ^
    public static final int OP_BIT_NOT = 373;     // ~
    public static final int OP_LEFT_SHIFT = 374;  // <<
    public static final int OP_RIGHT_SHIFT = 375; // >>

    // 分隔符 (400-499)
    public static final int SEMICOLON = 400;      // ;
    public static final int COMMA = 401;          // ,
    public static final int DOT = 402;            // .
    public static final int LPAREN = 403;         // (
    public static final int RPAREN = 404;         // )
    public static final int LBRACE = 405;         // {
    public static final int RBRACE = 406;         // }
    public static final int LBRACKET = 407;       // [
    public static final int RBRACKET = 408;       // ]
    public static final int PREPROCESSOR = 409;   // #
}