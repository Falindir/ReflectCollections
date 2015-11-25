package com.company.inspector;

import com.company.graph.DiGraph;
import com.company.graph.Edge;
import com.company.inspector.filter.constructor.ConstructorFilter;
import com.company.inspector.filter.field.FieldFilter;
import com.company.inspector.filter.method.MethodFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Permet d'inspecter une class afin de récuperer un ensemble d'information sur notre classe
 *  + Field
 *  + Method
 *  + Constructor
 *  mais aussi ces meme information de l'ensemble de l'arbre d'héritage
 * L'utilisation de filtre peut être utilisé afin de peaufiner notre recherche d'information
 */
public class Inspector {

    public Inspector() {
    }

    /**
     * Permet de récuperer tout les attributs d'une classe ainsi que ce de tout l'arbre d'héritage
     * @param c Class : la classes a inspecté
     * @param filters FieldFilter[] : les différents filtre à utiliser sur notre recherche
     * @param graph DiGraph : notre structure de donnée qui contient l'ensemble de nos classes
     * @return List<Field>
     */
    public List<Field> inspectAllFields (Class c, FieldFilter[] filters, DiGraph graph) {
        List<Field> fields = new ArrayList<Field>();

        Collections.addAll(fields, c.getDeclaredFields()); // récuprer l'ensemble des attributs de la class

        // on va chercher les différents attributs dans les usper classe
        inspectFieldsOfSuperClass(fields, c, graph);

        // on utilise les différents filtres pour affiner la recherche
        for(FieldFilter filter : filters)
            conserveFields(fields, filter.meetFilter(fields));

        return fields;
    }

    /**
     * permet des récupérer l'ensemble des attributs de super class de la class
     */
    private void inspectFieldsOfSuperClass (List<Field> fields , Class c, DiGraph graph) {

        List<Edge> edgeSuper = graph.getNode(c).getParents();

        List<Class> superClass = new ArrayList<Class>();

        for(Edge e : edgeSuper)
            superClass.add(e.getNode().getValue());

        if(superClass.size() > 0) {
            for(Class sup : superClass) {
                Collections.addAll(fields, sup.getDeclaredFields());
                inspectFieldsOfSuperClass(fields, sup, graph);
            }
        }
    }

    /**
     * Permet de supprimer de la liste de tout les attributs
     * ce qui ne nous intéresse pas
     */
    private void conserveFields (List<Field> fields, List<Field> conserve) {
        List<Field> f = new ArrayList<Field>(fields);

        for(Field field : f)
            if(!conserve.contains(field))
                fields.remove(field);
    }

    /**
     * Permet de supprimer de la liste de toutes les méthodes
     * ce qui ne nous intéresse pas
     */
    private void conserveMethods (List<Method> methods, List<Method> conserve) {
        List<Method> m = new ArrayList<Method>(methods);

        for(Method method : m)
            if(!conserve.contains(method))
                methods.remove(method);
    }

    /**
     * Permet de récuperer toutes les méthodes d'une classe ainsi que ce de tout l'arbre d'héritage
     * @param c Class : la classes a inspecté
     * @param filters MethodFilter[] : les différents filtre à utiliser sur notre recherche
     * @param graph DiGraph : notre structure de donnée qui contient l'ensemble de nos classes
     * @return List<Method>
     */
    public List<Method> inspectAllMethods (Class c, MethodFilter[] filters, DiGraph graph) {
        List<Method> methods = new ArrayList<Method>();

        Collections.addAll(methods, c.getDeclaredMethods());

        inspectMethodsOfSuperClass(methods, c, graph);

        for(MethodFilter filter : filters)
            conserveMethods(methods, filter.meetFilter(methods));

        return methods;
    }

    /**
     * permet des récupérer l'ensemble des methods de super class de la class
     */
    private void inspectMethodsOfSuperClass (List<Method> methods , Class c, DiGraph graph) {
        List<Edge> edgeSuper = graph.getNode(c).getParents();

        List<Class> superClass = new ArrayList<Class>();

        for(Edge e : edgeSuper)
            superClass.add(e.getNode().getValue());

        if(superClass.size() > 0) {
            for(Class sup : superClass) {
                Collections.addAll(methods, sup.getDeclaredMethods());
                inspectMethodsOfSuperClass(methods, sup, graph);
            }
        }
    }

    /**
     * Permet de récuperer tout les constructeur d'une classe ainsi que ce de tout l'arbre d'héritage
     * @param c Class : la classes a inspecté
     * @param filters ConstructorFilter[] : les différents filtre à utiliser sur notre recherche
     * @param graph DiGraph : notre structure de donnée qui contient l'ensemble de nos classes
     * @return List<Constructor>
     */
    public List<Constructor> inspectAllConstructors (Class c, ConstructorFilter[] filters, DiGraph graph) {
        List<Constructor> constructors = new ArrayList<Constructor>();

        Collections.addAll(constructors, c.getDeclaredConstructors());

        inspectConstructorsOfSuperClass(constructors, c, graph);

        for(ConstructorFilter filter : filters)
            conserveConstructor(constructors, filter.meetFilter(constructors));

        return constructors;
    }

    /**
     * Permet de supprimer de la liste de tout les contructeur
     * ce qui ne nous intéresse pas
     */
    private void conserveConstructor (List<Constructor> constructors, List<Constructor> conserve) {
        List<Constructor> c = new ArrayList<Constructor>(constructors);

        for(Constructor constructor : c)
            if(!conserve.contains(constructor))
                constructors.remove(constructor);
    }

    /**
     * permet des récupérer l'ensemble des constructeur de super class de la class
     */
    private void inspectConstructorsOfSuperClass (List<Constructor> constructors, Class c, DiGraph graph) {
        List<Edge> edgeSuper = graph.getNode(c).getParents();

        List<Class> superClass = new ArrayList<Class>();

        for(Edge e : edgeSuper)
            superClass.add(e.getNode().getValue());

        if(superClass.size() > 0) {
            for(Class sup : superClass) {
                Collections.addAll(constructors, sup.getDeclaredConstructors());
                inspectConstructorsOfSuperClass(constructors, sup, graph);
            }
        }
    }

    /**
     * Permet d'afficher les informations sur les attributs de notre
     * class inspecté
     */
    public void printFields(Class c, List<Field> fields) {

        System.out.println("Class : " + c.getSimpleName() + "\n\n {");

        for(Field field : fields) {
            field.setAccessible(true);
            System.out.println("Field {");

            System.out.println("Name : " + field.getName());

            System.out.println("Type : " + field.getType());

            try {
                System.out.println("Value : " + field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            System.out.println("}");
        }

        System.out.println("}");
    }

    /**
     * Permet d'afficher les informations sur les méthodes de notre
     * class inspecté
     */
    public void printMethods(Class c, List<Method> methods) {

        System.out.println("Class : " + c.getSimpleName() + "\n\n {");

        for(Method method : methods) {
            method.setAccessible(true);

            System.out.println("Method {");

            System.out.println("Name : " + method.getName());

            System.out.println("Return type : " + method.getReturnType());

            System.out.println("Parameters - " + method.getParameterCount() + " {");
            for(Parameter param : method.getParameters()) {
                System.out.println("Name : " + param.getName());
                System.out.println("Type : " + param.getType());
            }
            System.out.println("}");

            System.out.println("Exeptions - " + method.getExceptionTypes().length + " {");
            for(Class exception : method.getExceptionTypes()) {
                System.out.println("Name : " + exception.getSimpleName());
            }
            System.out.println("}");

            System.out.println("}");
        }

        System.out.println("}");
    }

    /**
     * Permet d'afficher les informations sur les constructeurs de notre
     * class inspecté
     */
    public void printConstructor(Class c, List<Constructor> constructors) {

        System.out.println("Class : " + c.getSimpleName() + "\n\n {");

        for(Constructor constructor : constructors) {
            constructor.setAccessible(true);

            System.out.println("Constructor {");

            System.out.println("Name : " + constructor.getName());

            System.out.println("Parameters - " + constructor.getParameterCount() + " {");
            for(Parameter param : constructor.getParameters()) {
                System.out.println("Name : " + param.getName());
                System.out.println("Type : " + param.getType());
            }
            System.out.println("}");


            System.out.println("}");
        }

        System.out.println("}");
    }
}
