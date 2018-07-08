package dev.posadskiy.spring.balancer.giver;

import dev.posadskiy.spring.balancer.storage.Storage;

public class MyComputerGiver {
    private Storage storage;

    public Storage getStorage() {
        System.out.println("Привет, хозяин!");
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
