package com.company.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Permet de représenter un graphe orienté composé de noeud
 */
public class DiGraph {

    /**
     * La liste des noeuds du graphe
     */
    private List<Node> nodes = new ArrayList<Node>();

    public DiGraph() {
    }

    /**
     * Permet d'ajouter un nouveau noeud à notre graphe
     * à partir d'une classe
     * @param c Class : la classe que l'on veut ajouter au graphe
     */
    public void addNode(Class c) {
        Node node = new Node(c); // par défaut on crée un nouveau noeud

        if(!haveThisNode(node)) // on vérifie si notre noeud n'est pas déjà dans notre graphe
            nodes.add(node); // on ajoute notre noeud au graphe
        else
            node = getNode(c); // sinon on récupère le noeud dans notre graphe

        // la liste qui va contenir les super classes de notre classe
        List<Class> superI = new ArrayList<Class>();

        // on récupère la liste de toutes ses interfaces
        // remarque : si notre classe c est une interface
        //            alors on récupère directement ses super classes
        Collections.addAll(superI, c.getInterfaces());

        if(!c.isInterface()) // si jamais notre classe c est une vrai classe
            superI.add(c.getSuperclass()); // alors on récupère sa super classes

        // pour finir on ajoute toutes les arếtes parent/enfant entre notre classe
        // et ses super classes
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

    /**
     * Permet de savoir si un noeud n est dans notre graphe
     * On ne veut pas de doublon dans notre graphe
     * @param n Node : le noeud à tester
     * @return boolean : true si n est dans notre graphe false sinon
     */
    public boolean haveThisNode(Node n) {
        for(Node node : nodes)
            if(node.isSameNode(n))
                return true;
        return false;
    }

    /**
     * Permet de trouver un noeud de notre graphe à partir
     * de sa classe associé
     * @param c Class
     * @return Node
     */
    public Node getNode(Class c) {
        for(Node node : nodes)
            if(node.getValue().equals(c))
                return node;
        return null; // TODO exception
    }

    /**
     * toString d'un DiGraph
     * @return String
     */
    @Override
    public String toString() {
        String result =  "DiGraph{";

        for(Node node : nodes)
            result += "\n\n" + node.toString();

        return result + '}';
    }
}
