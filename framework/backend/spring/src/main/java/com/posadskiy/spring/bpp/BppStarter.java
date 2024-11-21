package com.posadskiy.spring.bpp;

import com.posadskiy.spring.bpp.dto.ToDo;
import com.posadskiy.spring.bpp.storage.Storage;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BppStarter {
    private static final String STORAGE_NAME = "storage.LocalStorage";

    private final Storage storage;

    public BppStarter(Storage storage) {
        this.storage = storage;
    }

    public void run() {
        storage.add(new ToDo());
        log.info("Storage = " + storage);
    }
}
