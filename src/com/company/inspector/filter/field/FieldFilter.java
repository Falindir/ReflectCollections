package com.company.inspector.filter.field;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by jimmy on 18/11/15.
 */
public abstract class FieldFilter implements Filter<Field> {


    @Override
    public List<Field> meetFilter(List<Field> objects) {
        return objects;
    }


}
