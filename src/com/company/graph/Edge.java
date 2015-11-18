package com.company.graph;

/**
 * Created by jimmy on 16/11/15.
 */
public class Edge {

    private Node node;

    public Edge(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "Edge {N = " + node.getValue().getSimpleName() + "}";
    }
}
