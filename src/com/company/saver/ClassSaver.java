package com.company.saver;

import com.company.graph.DiGraph;
import com.company.graph.Edge;
import com.company.graph.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ClassSaver {

    private boolean interfaceNotChildOfRoot = false;

    public ClassSaver() {
    }

    public ClassSaver(boolean interfaceNotChildOfRoot) {
        this.interfaceNotChildOfRoot = interfaceNotChildOfRoot;
    }

    /**
     * Permet de sauvegarder dans un fichier.txt le nom complet de notre liste de classe
     * @param classes : la liste des classes
     * @param nameFile : le nom du fichier
     * @param type : the type of classes
     */
    public void savedToFile(List<Class> classes, String nameFile, String type) {
        try {
            File file = new File(nameFile+".txt");
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);

            for(Class c : classes) {
                writer.write(type + " : " + c.getName());
                writer.write("\n");
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de récuperer la liste des classes qui ont comme racine notre classe
     */
    public List<Class> getClassOfGraphWithRoot(Class c, DiGraph graph) {

        List<Node> nodes = new ArrayList<Node>();
        Node root = graph.getNode(c); // on recup le noeud racine
        nodes.add(root);

        getNode(nodes, root); // on recup l'arbre par rapport au noeud racine

        List<Class> classes = new ArrayList<Class>();
        for(Node node : nodes)
            classes.add(node.getValue()); // et on ajoute a notre liste la valeur de chaque noeud

        return classes;
    }

    /**
     * Permet de récuperer un ensemble de noeud celon une racine donnée
     */
    private void getNode(List<Node> nodes, Node root) {

        if(interfaceNotChildOfRoot) {
            for(Edge e : root.getParents()) {
                Node temp = e.getNode();

                if(!nodes.contains(temp))
                    if(temp.getValue().isInterface())
                        nodes.add(temp);
            }
        }

        for(Edge e : root.getChildren()) {
            Node temp = e.getNode();

            if (!nodes.contains(temp))
                nodes.add(temp);

            getNode(nodes, temp);
        }
    }




}
