package com.company.saver;

import com.company.graph.DiGraph;
import com.company.graph.Edge;
import com.company.graph.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 19/11/15.
 */
public class ClassSaver {

    public ClassSaver() {

    }

    public void savedToFile(List<Class> classes, String nameFile, String name) {
        try {
            File file = new File(nameFile+".txt");
            file.createNewFile();
            FileWriter writer = new FileWriter(file, true);

            for(Class c : classes) {
                writer.write(getInfoToSavedOfClass(name, c));
                writer.write("\n");
            }

            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String getInfoToSavedOfClass(String name, Class c) {
        String result = name + " : " + c.getName();

        return result;
    }

    public List<Class> getClassOfGraphWithRoot(Class c, DiGraph graph) {

        List<Node> nodes = new ArrayList<Node>();
        Node root = graph.getNode(c);
        nodes.add(root);

        getNode(nodes, root);

        List<Class> classes = new ArrayList<Class>();
        for(Node node : nodes)
            classes.add(node.getValue());

        return classes;
    }

    private void getNode(List<Node> nodes, Node root) {

        for(Edge e : root.getChildren()) {
            Node temp = e.getNode();

            if (!nodes.contains(temp))
                nodes.add(temp);

            getNode(nodes, temp);
        }
    }




}
