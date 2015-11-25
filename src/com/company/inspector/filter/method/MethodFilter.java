package com.company.inspector.filter.method;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Permet de filtrer les méthodes d'une classe
 */
public class MethodFilter implements Filter<Method> {

    @Override
    public List<Method> meetFilter(List<Method> objects) {
        return objects; // comportement par défault notre retourne notre liste initiale
    }

}
