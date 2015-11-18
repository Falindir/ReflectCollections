package com.company.graph;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by jimmy on 16/11/15.
 */
public class Node {

    private Class value;

    private List<Edge> parents = new ArrayList<Edge>();
    private List<Edge> children = new ArrayList<Edge>();

    private int size = 0;

    public Node(Class value) {
        this.value = value;
    }

    public Class getValue() {
        return value;
    }

    public void addEdge(Node n, EdgeType et) {
        if(!isSameNode(n)) {
            if(et.equals(EdgeType.IN)) { // parent
                if(!haveThisParent(n)) {
                    Edge e = new Edge(n);
                    parents.add(e);
                }
            }
            else if (et.equals(EdgeType.OUT)) { // child
                if(!haveThisChild(n)) {
                    Edge e = new Edge(n);
                    children.add(e);
                }
                size++;
            }
        }
    }

    public boolean isSameNode(Node n) {
        return value.equals(n.getValue());
    }

    public boolean haveThisChild(Node n) {
        for(Edge edge : children)
            if(edge.getNode().isSameNode(n))
                return true;
        return false;
    }

    public boolean haveThisParent(Node n) {
        for(Edge edge : parents)
            if(edge.getNode().isSameNode(n))
                return true;
        return false;
    }

    @Override
    public String toString() {
        String result =  "Node{" +
                "value = " + value.getName() + " - isClass = " + !value.isInterface();

                if(!value.isInterface())
                    result += " - isAbstract = " + Modifier.isAbstract(value.getModifiers());

                result += "\nparents : " + (parents.size()) + " \n\t";

                for (Edge parent : parents) {
                    result += parent.getNode().getValue().getName() + " - ";
                }

                result += "\nchild : " + (children.size()) + " \n\t";

                for (Edge child : children) {
                    result += child.getNode().getValue().getName() + " - ";
                }
        return result;
    }

    public String printTree(){
        String result = toString();

        for (Edge e : children)
            result += "\n\n" + e.getNode().printTree();

        return result;
    }

    public List<Edge> getParents() {
        return parents;
    }

    public List<Edge> getChildren() {
        return children;
    }

    public int getSize() {
        int s = size;
        for (Edge e : children)
            s += e.getNode().getSize();
        return s;
    }
}
