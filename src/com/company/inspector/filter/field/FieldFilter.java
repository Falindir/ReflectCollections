package com.company.inspector.filter.field;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Permet de filtrer les attributs d'une classe
 */
public abstract class FieldFilter implements Filter<Field> {


    @Override
    public List<Field> meetFilter(List<Field> objects) {
        return objects; // comportement par défault notre retourne notre liste initiale
    }


}
