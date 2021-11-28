package dev.llun.practice;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class TreeNode {
    private final Character code;
    public TreeNode dash;
    public TreeNode dot;

    public TreeNode() {
        this.code = Character.MIN_VALUE;
    }

    public TreeNode(char code) {
        this.code = code;
    }

    public List<String> possibilities(String signals) {
        List<Character> chars = signals.chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        List<TreeNode> allPossibilitiesNode = Arrays.asList(this);
        while (chars.size() > 0) {
            char head = chars.remove(0);
            if (head == '.') {
                allPossibilitiesNode = allPossibilitiesNode.stream().map(node -> node.dot).collect(Collectors.toList());
            } else if (head == '-') {
                allPossibilitiesNode = allPossibilitiesNode.stream().map(node -> node.dash).collect(Collectors.toList());
            } else if (head == '?') {
                allPossibilitiesNode = allPossibilitiesNode.stream().map(node -> Arrays.asList(node.dot, node.dash)).flatMap(Collection::stream).collect(Collectors.toList());
            }
        }
        return allPossibilitiesNode.stream().map(node -> node.code.toString()).collect(Collectors.toList());
    }
}

public class JavaPlayground {
    private static TreeNode root = null;

    public static void main(String[] args) {
        // Build tree
        if (JavaPlayground.root == null) {
            root = new TreeNode();
            root.dash = new TreeNode('T');
            root.dash.dash = new TreeNode('M');
            root.dash.dash.dash = new TreeNode('O');
            root.dash.dash.dot = new TreeNode('G');
            root.dash.dot = new TreeNode('N');
            root.dash.dot.dash = new TreeNode('K');
            root.dash.dot.dot = new TreeNode('D');
            root.dot = new TreeNode('E');
            root.dot.dash = new TreeNode('A');
            root.dot.dash.dash = new TreeNode('W');
            root.dot.dash.dot = new TreeNode('R');
            root.dot.dot = new TreeNode('I');
            root.dot.dot.dash = new TreeNode('U');
            root.dot.dot.dot = new TreeNode('S');
        }

        // Guess signals
        System.out.println(root.possibilities("?."));
    }
}
