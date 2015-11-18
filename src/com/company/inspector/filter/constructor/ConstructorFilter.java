package com.company.inspector.filter.constructor;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Created by jimmy on 18/11/15.
 */
public abstract class ConstructorFilter implements Filter<Constructor> {

    @Override
    public List<Constructor> meetFilter(List<Constructor> objects) {
        return objects;
    }
}
