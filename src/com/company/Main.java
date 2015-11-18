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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by jimmy on 17/11/15.
 */
public class Main {

    public static void main(String[] args) {
        ClassFinder cf = new ClassFinder();
        DiGraph g = new DiGraph();
        for (Class t : cf.getAllClass(Collection.class, true)) {
            g.addNode(t);
        }

        //System.out.println(g.getNode(Collection.class).getSize());
        //System.out.println(g.getNode(Collection.class).printTree());

        //for (Class t : cf.getAllClass(Map.class, true)) {
          //  g.addNode(t);
        //}

        //System.out.println(g.getNode(Collection.class).getSize());
        //System.out.println(g.getNode(Map.class).printTree());

        Inspector inspector = new Inspector();

        List<Field> fields = new ArrayList<Field>();
        FieldFilter[] filtersF = {new StaticFieldFilter(), new PublicFieldFilter(), new FinalFieldFilter()};
        fields.addAll(inspector.inspectAllFields(HashSet.class, filtersF, g));

        List<Method> methods = new ArrayList<Method>();
        MethodFilter[] filtersM = {new PublicMethodFilter()};
        methods.addAll(inspector.inspectAllMethods(HashSet.class, filtersM, g));

        List<Constructor> constructors = new ArrayList<Constructor>();
        ConstructorFilter[] filtersG = {new PublicConstructorFilter()};
        constructors.addAll(inspector.inspectAllConstructors(HashSet.class, filtersG, g));

        inspector.printFields(HashSet.class, fields);
        inspector.printMethods(HashSet.class, methods);
        inspector.printConstructor(HashSet.class, constructors);
    }
}
