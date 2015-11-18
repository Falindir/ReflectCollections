package com.company.inspector.filter.method;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 18/11/15.
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