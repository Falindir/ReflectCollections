package com.company;

import com.company.finder.ClassFinder;
import com.company.graph.DiGraph;

import java.util.Collection;
import java.util.Map;

/**
 * Created by jimmy on 17/11/15.
 */
public class Main {

    public static void main(String[] args) {
        ClassFinder cf = new ClassFinder();
        DiGraph g = new DiGraph();
        //for (Class t : cf.getAllClass(Collection.class, true)) {
          //  g.addNode(t);
        //}

        //System.out.println(g.getNode(Collection.class).getSize());
        //System.out.println(g.getNode(Collection.class).printTree());

        for (Class t : cf.getAllClass(Map.class, true)) {
            g.addNode(t);
        }

        //System.out.println(g.getNode(Collection.class).getSize());
        System.out.println(g.getNode(Map.class).printTree());
    }

}
