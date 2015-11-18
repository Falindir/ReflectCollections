package com.company.inspector.filter.field;

import com.company.inspector.filter.Filter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 18/11/15.
 */
public class PublicFieldFilter extends FieldFilter implements Filter<Field> {

    @Override
    public List<Field> meetFilter(List<Field> objects) {
        List<Field> result = new ArrayList<Field>();

        for(Field field : objects)
            if(Modifier.isPublic(field.getModifiers()))
                result.add(field);

        return result;
    }
}