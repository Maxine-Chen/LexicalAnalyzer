package core.automata;

import java.util.*;

/**
 * 实现子集构造法
 * 功能：将 NFA 转换为确定有限自动机 (DFA)
 */
public class DfaConstructor {

    /**
     * 主函数：将 NFA 对象转换为 DFA 对象
     */
    public AutomataEntity.Automaton convert(AutomataEntity.Automaton nfa) {
        AutomataEntity.Automaton dfa = new AutomataEntity.Automaton();

        // 获取字母表
        Set<String> alphabet = getAlphabet(nfa);

        // 存储已发现的 DFA 状态（每一个 DFA 状态对应一个 NFA 状态集合）
        List<Set<String>> dfaStates = new ArrayList<>();
        // 用于存储转换关系，方便后续构建对象
        Queue<Set<String>> unmarkedStates = new LinkedList<>();

        // 1. 计算 NFA 初始状态的 epsilon-闭包，作为 DFA 的起始状态
        Set<String> startClosure = epsilonClosure(nfa.getStartStates(), nfa);
        dfaStates.add(startClosure);
        unmarkedStates.add(startClosure);
        dfa.addStartState("0"); // DFA 的起始状态命名为 "0"

        int currentStateIdx = 0;
        while (!unmarkedStates.isEmpty()) {
            Set<String> T = unmarkedStates.poll();
            int T_idx = dfaStates.indexOf(T);

            // 2. 对字母表中的每个符号进行 move 操作
            for (String symbol : alphabet) {
                Set<String> U = epsilonClosure(move(T, symbol, nfa), nfa);

                if (U.isEmpty()) continue;

                // 3. 检查这个状态集 U 是否已经存在
                int U_idx = -1;
                for (int i = 0; i < dfaStates.size(); i++) {
                    if (dfaStates.get(i).equals(U)) {
                        U_idx = i;
                        break;
                    }
                }

                // 如果是新发现的状态集，加入队列
                if (U_idx == -1) {
                    dfaStates.add(U);
                    unmarkedStates.add(U);
                    U_idx = dfaStates.size() - 1;
                }

                // 添加 DFA 转换
                dfa.addTransition(String.valueOf(T_idx), symbol, String.valueOf(U_idx));
            }
        }

        // 4. 确定 DFA 的终结状态
        // 规则：只要 DFA 状态对应的 NFA 集合中包含原 NFA 的终态，该 DFA 状态即为终态
        for (int i = 0; i < dfaStates.size(); i++) {
            Set<String> currentSet = dfaStates.get(i);
            for (String nfaEndState : nfa.getEndStates()) {
                if (currentSet.contains(nfaEndState)) {
                    dfa.addEndState(String.valueOf(i));
                    break;
                }
            }
        }

        return dfa;
    }

    /**
     * 计算一个状态集合的 epsilon-闭包
     */
    private Set<String> epsilonClosure(Set<String> states, AutomataEntity.Automaton nfa) {
        Set<String> closure = new TreeSet<>(states); // 使用 TreeSet 保证顺序一致性
        Stack<String> stack = new Stack<>();
        for (String s : states) stack.push(s);

        while (!stack.isEmpty()) {
            String t = stack.pop();
            for (AutomataEntity.Transition trans : nfa.getTransitions()) {
                if (trans.getFromState().equals(t) && trans.getSymbol().equals(AutomataEntity.EPSILON)) {
                    if (!closure.contains(trans.getToState())) {
                        closure.add(trans.getToState());
                        stack.push(trans.getToState());
                    }
                }
            }
        }
        return closure;
    }

    /**
     * 计算状态集合在接收符号 symbol 后的跳转集合
     */
    private Set<String> move(Set<String> states, String symbol, AutomataEntity.Automaton nfa) {
        Set<String> result = new TreeSet<>();
        for (String s : states) {
            for (AutomataEntity.Transition trans : nfa.getTransitions()) {
                if (trans.getFromState().equals(s) && trans.getSymbol().equals(symbol)) {
                    result.add(trans.getToState());
                }
            }
        }
        return result;
    }

    /**
     * 提取 NFA 中的所有输入符号
     */
    private Set<String> getAlphabet(AutomataEntity.Automaton nfa) {
        Set<String> alphabet = new TreeSet<>();
        for (AutomataEntity.Transition trans : nfa.getTransitions()) {
            if (!trans.getSymbol().equals(AutomataEntity.EPSILON)) {
                alphabet.add(trans.getSymbol());
            }
        }
        return alphabet;
    }
}