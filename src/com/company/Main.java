package com.company;

import com.company.finder.ClassFinder;
import com.company.graph.DiGraph;
import com.company.inspector.Inspector;
import com.company.inspector.filter.Filter;
import com.company.inspector.filter.constructor.ConstructorFilter;
import com.company.inspector.filter.constructor.PublicConstructorFilter;
import com.company.inspector.filter.field.FieldFilter;
import com.company.inspector.filter.field.FinalFieldFilter;
import com.company.inspector.filter.field.PublicFieldFilter;
import com.company.inspector.filter.field.StaticFieldFilter;
import com.company.inspector.filter.method.MethodFilter;
import com.company.inspector.filter.method.PublicMethodFilter;
import com.company.saver.ClassRCTF;
import com.company.saver.ClassSaver;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        // Q1
        ClassFinder cf = new ClassFinder();
        DiGraph g = new DiGraph();
        for (Class t : cf.getAllClass(Collection.class, true)) {
            g.addNode(t);
        }

        ClassSaver saver = new ClassSaver();
        List<Class> allClasses = new ArrayList<Class>();
        allClasses.addAll(saver.getClassOfGraphWithRoot(Collection.class, g));
        allClasses.addAll(saver.getClassOfGraphWithRoot(Map.class, g));

        List<Class> classes = new ArrayList<Class>();
        List<Class> abstracts = new ArrayList<Class>();
        List<Class> interfaces = new ArrayList<Class>();

        for (Class c : allClasses) {
            if (c.isInterface())
                interfaces.add(c);
            else if (Modifier.isAbstract(c.getModifiers()))
                abstracts.add(c);
            else
                classes.add(c);
        }

        saver.savedToFile(classes, "classSaved", "Class");
        saver.savedToFile(abstracts, "abstractSaved", "Abstract");
        saver.savedToFile(interfaces, "interfaceSaved", "Interface");

        // Q2
        MethodFilter[] filterM = {new PublicMethodFilter()};
        FieldFilter[] filterF = {new StaticFieldFilter(), new PublicFieldFilter(), new FinalFieldFilter()};

        ClassRCTF saverRCFT = new ClassRCTF(allClasses, filterM, filterF, g);
        saverRCFT.initTable();

        try {
            saverRCFT.writeTable("table4.rcft");
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Main Finish");

    }
}
