package com.muffin.web.util;

import java.util.Optional;

public interface GenericService<T> {
    public Iterable<T> findAll();
    public int count();
    public void delete(T t);
    public boolean exists(String id);
}
