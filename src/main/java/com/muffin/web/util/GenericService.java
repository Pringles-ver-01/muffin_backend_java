package com.muffin.web.util;

import java.util.Optional;

public interface GenericService<T> {

    public void save(T t);
    public Optional<T> findById(String id);
    public Iterable<T> findAll();
    public int count();
    public void delete(String id);
    public boolean exists(String id);
}
