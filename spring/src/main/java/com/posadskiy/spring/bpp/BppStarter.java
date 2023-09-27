package com.posadskiy.spring.bpp;

import com.posadskiy.spring.bpp.dto.ToDo;
import com.posadskiy.spring.bpp.storage.Storage;
import org.springframework.stereotype.Service;

@Service
public class BppStarter {
    private static final String STORAGE_NAME = "storage.LocalStorage";

    private final Storage storage;

    public BppStarter(Storage storage) {
        this.storage = storage;
    }

    public void run() {
        storage.add(new ToDo());
        System.out.println("Storage = " + storage);
    }
}
