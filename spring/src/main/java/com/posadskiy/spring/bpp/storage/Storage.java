package com.posadskiy.spring.bpp.storage;


import com.posadskiy.spring.bpp.dto.ToDo;

import java.io.Serializable;
import java.util.Map;

public interface Storage extends Serializable {

    Map<Integer, ToDo> getAll();

    ToDo get(Integer id);

    void add(ToDo toDo);

    void edit(Integer id, ToDo toDo);

    void done(Integer id);

}
