package com.company.graph;

/**
 * Permet de représenter la liaison entre deux noeud
 * Une arête ne stocke que le noeud cible
 * Car on représente ici un graphe orienté
 */
public class Edge {

    /**
     * le noeud cible de l'arête
     */
    private Node node;

    public Edge(Node node) {
        this.node = node;
    }

    /**
     * @return Node : on recupère le noeud cible de notre arète
     */
    public Node getNode() {
        return node;
    }

    /**
     * toString d'une arête
     * @return String
     */
    @Override
    public String toString() {
        return "Edge {N = " + node.getValue().getSimpleName() + "}";
    }
}
