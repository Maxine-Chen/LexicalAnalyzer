package core.automata;

import java.util.*;

/**
 * AutomataEntity: 自动机实体定义类
 * 包含状态转换关系的定义以及自动机对象的封装
 */
public class AutomataEntity {

    /**
     * Transition 类：描述自动机的一条边
     */
    public static class Transition {
        private String fromState;  // 起始状态名
        private String symbol;     // 接收符号
        private String toState;    // 到达状态名

        public Transition(String fromState, String symbol, String toState) {
            this.fromState = fromState;
            this.symbol = symbol;
            this.toState = toState;
        }

        // Getters
        public String getFromState() { return fromState; }
        public String getSymbol() { return symbol; }
        public String getToState() { return toState; }

        /**
         * 转换为表格行数据，方便 UI 展示
         */
        public Object[] toTableRow() {
            return new Object[]{fromState, symbol, toState};
        }
    }

    /**
     * Automaton 类：表示一个完整的自动机（NFA, DFA 或 MFA）
     */
    public static class Automaton {
        private List<Transition> transitions; // 转换规则集合
        private Set<String> startStates;      // 开始状态集 (NFA可能有多个，DFA通常一个)
        private Set<String> endStates;        // 终结状态集 (接收态)
        private Set<String> allStates;        // 所有状态的集合

        public Automaton() {
            this.transitions = new ArrayList<>();
            this.startStates = new TreeSet<>();
            this.endStates = new TreeSet<>();
            this.allStates = new TreeSet<>();
        }

        /**
         * 添加一条转换规则
         */
        public void addTransition(String from, String symbol, String to) {
            transitions.add(new Transition(from, symbol, to));
            allStates.add(from);
            allStates.add(to);
        }

        /**
         * 设置开始状态
         */
        public void addStartState(String state) {
            startStates.add(state);
            allStates.add(state);
        }

        /**
         * 设置终结状态
         */
        public void addEndState(String state) {
            endStates.add(state);
            allStates.add(state);
        }

        // Getters
        public List<Transition> getTransitions() { return transitions; }
        public Set<String> getStartStates() { return startStates; }
        public Set<String> getEndStates() { return endStates; }
        public Set<String> getAllStates() { return allStates; }

        /**
         * 辅助方法：将所有转换规则转换为 JTable 所需的二维数组格式
         */
        public Object[][] getTableData() {
            Object[][] data = new Object[transitions.size()][3];
            for (int i = 0; i < transitions.size(); i++) {
                data[i] = transitions.get(i).toTableRow();
            }
            return data;
        }

        /**
         * 辅助方法：将状态集合转换为字符串显示
         */
        public String statesToString(Set<String> states) {
            return String.join(", ", states);
        }
    }

    // 定义空转换符号常量
    public static final String EPSILON = "#";
}