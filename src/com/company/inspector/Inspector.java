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
 * Created by jimmy on 18/11/15.
 */
public class Inspector {

    public Inspector() {

    }

    public List<Field> inspectAllFields (Class c, FieldFilter[] filters, DiGraph graph) {
        List<Field> fields = new ArrayList<Field>();

        Collections.addAll(fields, c.getDeclaredFields()); // Local public

        inspectFieldsOfSuperClass(fields, c, graph);

        for(FieldFilter filter : filters)
            conserveFields(fields, filter.meetFilter(fields));

        return fields;
    }

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

    private void conserveFields (List<Field> fields, List<Field> conserve) {
        List<Field> f = new ArrayList<Field>(fields);

        for(Field field : f)
            if(!conserve.contains(field))
                fields.remove(field);
    }

    private void conserveMethods (List<Method> methods, List<Method> conserve) {
        List<Method> m = new ArrayList<Method>(methods);

        for(Method method : m)
            if(!conserve.contains(method))
                methods.remove(method);
    }

    public List<Method> inspectAllMethods (Class c, MethodFilter[] filters, DiGraph graph) {
        List<Method> methods = new ArrayList<Method>();

        Collections.addAll(methods, c.getDeclaredMethods());

        inspectMethodsOfSuperClass(methods, c, graph);

        for(MethodFilter filter : filters)
            conserveMethods(methods, filter.meetFilter(methods));

        return methods;
    }

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

    public List<Constructor> inspectAllConstructors (Class c, ConstructorFilter[] filters, DiGraph graph) {
        List<Constructor> constructors = new ArrayList<Constructor>();

        Collections.addAll(constructors, c.getDeclaredConstructors());

        inspectConstructorsOfSuperClass(constructors, c, graph);

        for(ConstructorFilter filter : filters)
            conserveConstructor(constructors, filter.meetFilter(constructors));

        return constructors;
    }

    private void conserveConstructor (List<Constructor> constructors, List<Constructor> conserve) {
        List<Constructor> c = new ArrayList<Constructor>(constructors);

        for(Constructor constructor : c)
            if(!conserve.contains(constructor))
                constructors.remove(constructor);
    }

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
