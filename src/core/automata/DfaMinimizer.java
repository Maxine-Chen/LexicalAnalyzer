package core.automata;

import java.util.*;

/**
 * DfaMinimizer: 实现 DFA 状态最小化算法 (分割法)
 * 功能：消除冗余状态，生成最小化确定有限自动机 (MFA)
 */
public class DfaMinimizer {

    /**
     * 主函数：将 DFA 对象转换为最小化的 MFA 对象
     */
    public AutomataEntity.Automaton minimize(AutomataEntity.Automaton dfa) {
        // 1. 初始分割：将状态分为 终态组 和 非终态组
        Set<String> endStates = dfa.getEndStates();
        Set<String> nonEndStates = new TreeSet<>(dfa.getAllStates());
        nonEndStates.removeAll(endStates);

        List<Set<String>> partition = new ArrayList<>();
        if (!nonEndStates.isEmpty()) partition.add(nonEndStates);
        if (!endStates.isEmpty()) partition.add(endStates);

        // 获取字母表
        Set<String> alphabet = getAlphabet(dfa);

        // 2. 迭代分裂
        boolean changed = true;
        while (changed) {
            changed = false;
            List<Set<String>> newPartition = new ArrayList<>();

            for (Set<String> group : partition) {
                if (group.size() <= 1) {
                    newPartition.add(group);
                    continue;
                }

                // 将当前组进一步拆分
                List<Set<String>> splitGroups = split(group, partition, alphabet, dfa);
                if (splitGroups.size() > 1) {
                    changed = true;
                }
                newPartition.addAll(splitGroups);
            }
            partition = newPartition;
        }

        // 3. 构建 MFA 对象
        return buildMfa(partition, alphabet, dfa);
    }

    /**
     * 拆分逻辑：检查组内状态在接收相同符号后，是否跳转到同一个分区
     */
    private List<Set<String>> split(Set<String> group, List<Set<String>> partition, Set<String> alphabet, AutomataEntity.Automaton dfa) {
        List<Set<String>> subGroups = new ArrayList<>();

        for (String state : group) {
            boolean found = false;
            for (Set<String> subGroup : subGroups) {
                // 比较当前 state 是否与 subGroup 中的第一个元素等价
                if (isEquivalent(state, subGroup.iterator().next(), partition, alphabet, dfa)) {
                    subGroup.add(state);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Set<String> newSub = new TreeSet<>();
                newSub.add(state);
                subGroups.add(newSub);
            }
        }
        return subGroups;
    }

    /**
     * 判断两个状态在当前划分下是否等价
     */
    private boolean isEquivalent(String s1, String s2, List<Set<String>> partition, Set<String> alphabet, AutomataEntity.Automaton dfa) {
        for (String symbol : alphabet) {
            String target1 = getTarget(s1, symbol, dfa);
            String target2 = getTarget(s2, symbol, dfa);

            // 找这两个目标状态分别属于当前 partition 的哪个组（索引）
            if (getGroupIndex(target1, partition) != getGroupIndex(target2, partition)) {
                return false;
            }
        }
        return true;
    }

    private String getTarget(String from, String symbol, AutomataEntity.Automaton dfa) {
        for (AutomataEntity.Transition t : dfa.getTransitions()) {
            if (t.getFromState().equals(from) && t.getSymbol().equals(symbol)) {
                return t.getToState();
            }
        }
        return null; // 陷阱状态或无跳转
    }

    private int getGroupIndex(String state, List<Set<String>> partition) {
        if (state == null) return -1;
        for (int i = 0; i < partition.size(); i++) {
            if (partition.get(i).contains(state)) return i;
        }
        return -1;
    }

    private AutomataEntity.Automaton buildMfa(List<Set<String>> partition, Set<String> alphabet, AutomataEntity.Automaton dfa) {
        AutomataEntity.Automaton mfa = new AutomataEntity.Automaton();

        for (int i = 0; i < partition.size(); i++) {
            Set<String> group = partition.get(i);
            String representative = group.iterator().next();

            // 设置开始状态
            for (String start : dfa.getStartStates()) {
                if (group.contains(start)) mfa.addStartState(String.valueOf(i));
            }
            // 设置终结状态
            for (String end : dfa.getEndStates()) {
                if (group.contains(end)) {
                    mfa.addEndState(String.valueOf(i));
                    break;
                }
            }

            // 添加转移
            for (String symbol : alphabet) {
                String oldTarget = getTarget(representative, symbol, dfa);
                int targetGroupIdx = getGroupIndex(oldTarget, partition);
                if (targetGroupIdx != -1) {
                    mfa.addTransition(String.valueOf(i), symbol, String.valueOf(targetGroupIdx));
                }
            }
        }
        return mfa;
    }

    private Set<String> getAlphabet(AutomataEntity.Automaton dfa) {
        Set<String> alphabet = new TreeSet<>();
        for (AutomataEntity.Transition trans : dfa.getTransitions()) {
            alphabet.add(trans.getSymbol());
        }
        return alphabet;
    }
}
