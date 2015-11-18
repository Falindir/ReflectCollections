package com.company.inspector.filter;

import java.util.List;

/**
 * Created by jimmy on 18/11/15.
 */
public interface Filter<T> {

    public List<T> meetFilter(List<T> objects);



}
