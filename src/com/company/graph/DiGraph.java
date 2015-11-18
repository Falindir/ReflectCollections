package com.company.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jimmy on 16/11/15.
 */
public class DiGraph {

    private List<Node> nodes = new ArrayList<Node>();

    public DiGraph() {

    }

    public void addNode(Class c) {
        Node node = new Node(c);

        if(!haveThisNode(node))
            nodes.add(node);
        else
            node = getNode(c);

        List<Class> superI = new ArrayList<Class>();
        Collections.addAll(superI, c.getInterfaces());

        if(!c.isInterface())
            superI.add(c.getSuperclass());

        for(Class cl : superI) {
            Node nSI = new Node(cl);

            if(haveThisNode(nSI)) {
                Node nSIO = getNode(cl);
                nSIO.addEdge(node, EdgeType.OUT);
                node.addEdge(nSIO, EdgeType.IN);
            }
            else {
                nodes.add(nSI);
                nSI.addEdge(node, EdgeType.OUT);
                node.addEdge(nSI, EdgeType.IN);
            }
        }
    }

    public boolean haveThisNode(Node n) {
        for(Node node : nodes)
            if(node.isSameNode(n))
                return true;
        return false;
    }

    public Node getNode(Class c) {
        for(Node node : nodes)
            if(node.getValue().equals(c))
                return node;
        return null;
    }

    @Override
    public String toString() {
        String result =  "DiGraph{";

        for(Node node : nodes)
            result += "\n\n" + node.toString();

        return result + '}';
    }
}
