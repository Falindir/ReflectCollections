package com.company.inspector.filter.constructor;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de ne garder que les constructeurs public
 */
public class PublicConstructorFilter extends ConstructorFilter implements Filter<Constructor> {

    @Override
    public List<Constructor> meetFilter(List<Constructor> objects) {
        List<Constructor> result = new ArrayList<Constructor>();

        for (Constructor constructor : objects)
            if (Modifier.isPublic(constructor.getModifiers()))
                result.add(constructor);

        return result;
    }

}
