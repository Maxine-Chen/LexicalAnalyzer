package core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lexer 类：实现基于正则表达式的词法分析逻辑
 */
public class Lexer {
    private String sourceCode;             // 待分析的源代码
    private List<Token> tokens;            // 存储生成的 Token 序列
    private StringBuilder errorLog;        // 存储词法错误信息

    public Lexer(String sourceCode) {
        this.sourceCode = sourceCode;
        this.tokens = new ArrayList<>();
        this.errorLog = new StringBuilder();
    }

    public void analyze() {
        tokens.clear();
        errorLog.setLength(0);

        // 将源码按行拆分处理，方便统计行号
        String[] lines = sourceCode.split("\\R");

        for (int i = 0; i < lines.length; i++) {
            String lineContent = lines[i];
            int currentPos = 0;
            int lineNum = i + 1; // 实际行号从 1 开始

            while (currentPos < lineContent.length()) {
                // 1. 跳过空白字符
                if (Character.isWhitespace(lineContent.charAt(currentPos))) {
                    currentPos++;
                    continue;
                }

                boolean matched = false;
                String remainingStr = lineContent.substring(currentPos);

                // 2. 遍历 TokenType 中定义的规则进行匹配
                for (Map.Entry<String, Integer> entry : TokenType.RULES.entrySet()) {
                    String regex = entry.getKey();
                    int typeCode = entry.getValue();

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(remainingStr);

                    // 使用 lookingAt() 匹配字符串的前缀
                    if (matcher.lookingAt()) {
                        String matchText = matcher.group();

                        // 发现有效单词，创建 Token
                        tokens.add(new Token(lineNum, matchText, typeCode));

                        // 移动当前指针
                        currentPos += matchText.length();
                        matched = true;
                        break; // 匹配到最高优先级的规则后，跳出当前规则循环
                    }
                }

                // 3. 如果所有规则都无法匹配，说明存在非法字符
                if (!matched) {
                    char errorChar = lineContent.charAt(currentPos);
                    errorLog.append("词法错误 [行 ").append(lineNum)
                            .append("]: 无法识别的字符 '").append(errorChar).append("'\n");
                    currentPos++; // 跳过错误字符继续向下分析
                }
            }
        }

        if (errorLog.length() == 0) {
            errorLog.append("词法分析结束 - 0 error(s)");
        }
    }
    public List<Token> getTokens() {
        return tokens;
    }

    public String getErrorLog() {
        return errorLog.toString();
    }
}