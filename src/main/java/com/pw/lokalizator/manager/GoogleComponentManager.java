package com.pw.lokalizator.manager;

import com.pw.lokalizator.model.google.map.GoogleMapComponent;

/**
 * Created by Patryk on 2016-08-29.
 */
public interface GoogleComponentManager<ID, T extends GoogleMapComponent> {
    boolean add(ID id, T element);
    boolean delete(ID id);
    boolean update(ID id, T element);
    T find(ID id);
    void clear();
}
