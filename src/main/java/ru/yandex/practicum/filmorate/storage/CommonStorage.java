package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface CommonStorage<T> {
    T create(T t);

    T update(T t);

    List<T> findAll();
}
