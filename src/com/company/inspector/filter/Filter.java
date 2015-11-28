package com.company.inspector.filter;

import java.util.List;

/**
 * Utilisation du Filter Pattern
 * http://www.tutorialspoint.com/design_pattern/filter_pattern.htm
 */
public interface Filter<T> {

    /**
     * @param objects List<T> : notre liste initiale
     * @return List<T> : une nouvelle liste une fois filtrée
     */
    public List<T> meetFilter(List<T> objects);

}
