package com.posadskiy.spring.bpp.storage;

import com.posadskiy.spring.bpp.dto.ToDo;
import com.posadskiy.spring.bpp.utils.annotation.Profiling;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Profiling
@Service
public class LocalStorage implements Storage {

    private static final AtomicInteger idCounter = new AtomicInteger();
    private final Map<Integer, ToDo> storage = new HashMap<>();

    public void add(ToDo toDo) {
        storage.put(idCounter.getAndIncrement(), toDo);
    }

    public void edit(Integer id, ToDo toDo) {
        if (!storage.containsKey(id)) {
            throw new IllegalArgumentException("Элемент с таким id отсутствует");
        }
        storage.replace(id, toDo);
    }

    public void done(Integer id) {
        storage.get(id).setIsDone(Boolean.TRUE);
    }

    public Map<Integer, ToDo> getAll() {
        return storage;
    }

    public ToDo get(Integer id) {
        return storage.get(id);
    }

    @Override
    public String toString() {
        return "LocalStorage: ".concat(String.valueOf(storage.size()));
    }

}
