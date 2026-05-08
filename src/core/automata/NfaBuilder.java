package core.automata;

import java.util.*;

/**
 * NfaBuilder: 实现 Thompson 构造算法
 * 功能：将正则表达式转换为 NFA 状态转换表
 */
public class NfaBuilder {

    private int stateCount = 0; // 用于生成全局唯一的状态 ID

    /**
     * NFA 片段内部类：表示一个小的 NFA 单元（有起点和终点）
     */
    private static class Fragment {
        int start;
        int end;
        List<AutomataEntity.Transition> transitions;

        Fragment(int start, int end) {
            this.start = start;
            this.end = end;
            this.transitions = new ArrayList<>();
        }
    }

    /**
     * 主函数：将正则字符串转换为 Automaton 对象
     */
    public AutomataEntity.Automaton build(String regex) {
        String processed = addConcatSymbol(regex);     // 1. 加点
        String postfix = infixToPostfix(processed);   // 2. 转后缀
        return postfixToNFA(postfix);                // 3. 构造 NFA
    }

    /**
     * 步骤 1：给正则表达式添加显式的连接符 '.'
     * 例如: ab|c -> a.b|c
     */
    private String addConcatSymbol(String regex) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < regex.length(); i++) {
            char c1 = regex.charAt(i);
            result.append(c1);
            if (i + 1 < regex.length()) {
                char c2 = regex.charAt(i + 1);
                // 在以下情况加 '.' : (字母|数字|')'|'*') 后面跟着 (字母|数字|'(')
                if ((Character.isLetterOrDigit(c1) || c1 == ')' || c1 == '*') &&
                        (Character.isLetterOrDigit(c2) || c2 == '(')) {
                    result.append('.');
                }
            }
        }
        return result.toString();
    }

    /**
     * 步骤 2：中缀转后缀 (调度场算法)
     * 优先级: * > . > |
     */
    private String infixToPostfix(String exp) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();
        Map<Character, Integer> priority = new HashMap<>();
        priority.put('|', 1);
        priority.put('.', 2);
        priority.put('*', 3);

        for (char c : exp.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                postfix.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') postfix.append(stack.pop());
                stack.pop();
            } else {
                while (!stack.isEmpty() && stack.peek() != '(' && priority.get(stack.peek()) >= priority.get(c)) {
                    postfix.append(stack.pop());
                }
                stack.push(c);
            }
        }
        while (!stack.isEmpty()) postfix.append(stack.pop());
        return postfix.toString();
    }

    /**
     * 步骤 3：根据后缀表达式构造 NFA (Thompson 算法)
     */
    private AutomataEntity.Automaton postfixToNFA(String postfix) {
        Stack<Fragment> stack = new Stack<>();
        stateCount = 0;

        for (char c : postfix.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                // 基础片段: s0 --c--> s1
                int s0 = stateCount++, s1 = stateCount++;
                Fragment f = new Fragment(s0, s1);
                f.transitions.add(new AutomataEntity.Transition(String.valueOf(s0), String.valueOf(c), String.valueOf(s1)));
                stack.push(f);
            } else if (c == '.') {
                Fragment f2 = stack.pop();
                Fragment f1 = stack.pop();
                // 连接: f1.end --#--> f2.start
                f1.transitions.addAll(f2.transitions);
                f1.transitions.add(new AutomataEntity.Transition(String.valueOf(f1.end), AutomataEntity.EPSILON, String.valueOf(f2.start)));
                stack.push(new Fragment(f1.start, f2.end) {{ transitions.addAll(f1.transitions); }});
            } else if (c == '|') {
                Fragment f2 = stack.pop();
                Fragment f1 = stack.pop();
                int sStart = stateCount++, sEnd = stateCount++;
                Fragment res = new Fragment(sStart, sEnd);
                res.transitions.addAll(f1.transitions);
                res.transitions.addAll(f2.transitions);
                // 并集逻辑: 新起点连接 f1,f2 起点；f1,f2 终点连接新终点
                res.transitions.add(new AutomataEntity.Transition(String.valueOf(sStart), AutomataEntity.EPSILON, String.valueOf(f1.start)));
                res.transitions.add(new AutomataEntity.Transition(String.valueOf(sStart), AutomataEntity.EPSILON, String.valueOf(f2.start)));
                res.transitions.add(new AutomataEntity.Transition(String.valueOf(f1.end), AutomataEntity.EPSILON, String.valueOf(sEnd)));
                res.transitions.add(new AutomataEntity.Transition(String.valueOf(f2.end), AutomataEntity.EPSILON, String.valueOf(sEnd)));
                stack.push(res);
            } else if (c == '*') {
                Fragment f1 = stack.pop();
                int sStart = stateCount++, sEnd = stateCount++;
                Fragment res = new Fragment(sStart, sEnd);
                res.transitions.addAll(f1.transitions);
                // 闭包逻辑
                res.transitions.add(new AutomataEntity.Transition(String.valueOf(sStart), AutomataEntity.EPSILON, String.valueOf(f1.start))); // 进循环
                res.transitions.add(new AutomataEntity.Transition(String.valueOf(sStart), AutomataEntity.EPSILON, String.valueOf(sEnd)));   // 跳过循环
                res.transitions.add(new AutomataEntity.Transition(String.valueOf(f1.end), AutomataEntity.EPSILON, String.valueOf(f1.start))); // 循环回去
                res.transitions.add(new AutomataEntity.Transition(String.valueOf(f1.end), AutomataEntity.EPSILON, String.valueOf(sEnd)));   // 出循环
                stack.push(res);
            }
        }

        // 组装最终的 Automaton 对象
        Fragment finalFrag = stack.pop();
        AutomataEntity.Automaton automaton = new AutomataEntity.Automaton();
        for (AutomataEntity.Transition t : finalFrag.transitions) {
            automaton.addTransition(t.getFromState(), t.getSymbol(), t.getToState());
        }
        automaton.addStartState(String.valueOf(finalFrag.start));
        automaton.addEndState(String.valueOf(finalFrag.end));
        return automaton;
    }
}