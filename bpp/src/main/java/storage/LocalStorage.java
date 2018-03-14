package storage;

import dto.ToDo;
import org.springframework.stereotype.Component;
import utils.annotation.Profiling;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Profiling
@Component
public class LocalStorage implements Storage {

    private Map<Integer, ToDo> storage = new HashMap<>();
    private static AtomicInteger idCounter = new AtomicInteger();

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
