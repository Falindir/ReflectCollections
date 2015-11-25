package com.company.inspector.filter.constructor;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Permet de filtrer les contructeurs d'une classe
 */
public abstract class ConstructorFilter implements Filter<Constructor> {

    @Override
    public List<Constructor> meetFilter(List<Constructor> objects) {
        return objects; // comportement par défault notre retourne notre liste initiale
    }
}
