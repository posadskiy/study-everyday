package storage;

import dto.ToDo;

import java.io.Serializable;
import java.util.Map;

public interface Storage extends Serializable {

    public Map<Integer, ToDo> getAll();
    public ToDo get(Integer id);
    public void add(ToDo toDo);
    public void edit(Integer id, ToDo toDo);
    public void done(Integer id);

}
