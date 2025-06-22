package dao;

import java.util.List;

public interface GenericDAO<T> {
    void insert(T t);
    void update(T t);
    void delete(int id);
    T getById(int id);
    List<T> getAll();
}