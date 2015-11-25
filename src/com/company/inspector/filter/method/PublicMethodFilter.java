package com.company.inspector.filter.method;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Permet de ne garder que les méthodes public
 */
public class PublicMethodFilter extends MethodFilter implements Filter<Method> {

    @Override
    public List<Method> meetFilter(List<Method> objects) {
        List<Method> result = new ArrayList<Method>();

        for (Method method : objects)
            if (Modifier.isPublic(method.getModifiers()))
                result.add(method);

        return result;
    }
}