package dev.posadskiy.spring.balancer.giver;

import dev.posadskiy.spring.balancer.storage.Storage;

public class GlobalBankGiver {

    private Storage storage;

    public Storage getStorage() {
        System.out.println("Добро пожаловать в Global Bank. Самый большой банк в Галлактике.");
        System.out.println("Когда вернете деньги?");
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

}
