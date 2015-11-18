package com.company.inspector.filter.method;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by jimmy on 18/11/15.
 */
public class MethodFilter implements Filter<Method> {

    @Override
    public List<Method> meetFilter(List<Method> objects) {
        return objects;
    }

}
