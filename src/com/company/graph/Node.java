package com.company.graph;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Chaque noeud va contenir une classe qui lui sera propre.
 * Nous aurons donc la relation parents enfants
 * entre les noeuds ce qui permettra d'avoir la relation
 * d'héritage/implémentation entres les différentes classes.
 */
public class Node {

    /**
     * la classe associé au noeud
     */
    private Class value;

    /**
     * la liste de ses arêtes vers ses noeuds parents
     */
    private List<Edge> parents = new ArrayList<Edge>();

    /**
     * la liste de ses arêtes vers ses noeuds enfants
     */
    private List<Edge> children = new ArrayList<Edge>();

    /**
     *
     * @param value : la classe que l'on veut stocker dans un noeud
     */
    public Node(Class value) {
        this.value = value;
    }

    /**
     * @return Class : la classe contenue dans le noeud
     */
    public Class getValue() {
        return value;
    }

    /**
     * Permet d'ajouter une arête entre deux noeud
     * @param n Node : l'autre noeud
     * @param et EdgeType : le type d'arête (IN/OUT)
     */
    public void addEdge(Node n, EdgeType et) {
        if(!isSameNode(n)) {
            if(et.equals(EdgeType.IN)) { // si on veut une relation parent
                if(!haveThisParent(n)) {
                    Edge e = new Edge(n);
                    parents.add(e);
                }
            }
            else if (et.equals(EdgeType.OUT)) { // si on veut une relation enfant
                if(!haveThisChild(n)) {
                    Edge e = new Edge(n);
                    children.add(e);
                }
            }
        }
    }

    /**
     * Deux noeud sont en effet identique si leurs classes
     * correspondantes sont identiques
     * @param n : l'autre noeud à comparer à notre noeud courant
     * @return boolean : true si deux noeuds sont identiques false sinon
     */
    public boolean isSameNode(Node n) {
        return value.equals(n.getValue());
    }

    /**
     * Permet de savoir si le noeud n est un enfants de notre noeud
     * @param n Node : le noeud à tester
     * @return boolean : true si n est enfant de this sinon false
     */
    public boolean haveThisChild(Node n) {
        for(Edge edge : children)
            if(edge.getNode().isSameNode(n))
                return true;
        return false;
    }

    /**
     * Permet de savoir si le noeud n est un parent de notre noeud
     * @param n Node : le noeud à tester
     * @return boolean : true si n est parent de this sinon false
     */
    public boolean haveThisParent(Node n) {
        for(Edge edge : parents)
            if(edge.getNode().isSameNode(n))
                return true;
        return false;
    }

    /**
     * le toString d'un noeud
     * @return String
     */
    @Override
    public String toString() {
        String result =  "Node{" +
                "value = " + value.getName() + " - isClass = " + !value.isInterface();

                if(!value.isInterface()) // on identifie si notre classe  n'est pas une interface
                    result += " - isAbstract = " + Modifier.isAbstract(value.getModifiers());

                result += "\nparents : " + " \n\t";

                for (Edge parent : parents) {
                    result += parent.getNode().getValue().getName() + " - ";
                }

                result += "\nchild : " + (children.size()) + " \n\t";

                for (Edge child : children) {
                    result += child.getNode().getValue().getName() + " - ";
                }
        return result;
    }

    /**
     * @return List<Edge> : la liste des arêtes vers les noeuds parents de notre noeud
     */
    public List<Edge> getParents() {
        return parents;
    }

    /**
     * @return List<Edge> : la liste des arêtes vers les noeuds enfants de notre noeud
     */
    public List<Edge> getChildren() {
        return children;
    }
}
