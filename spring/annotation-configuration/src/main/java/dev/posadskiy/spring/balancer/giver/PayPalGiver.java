package dev.posadskiy.spring.balancer.giver;

import dev.posadskiy.spring.balancer.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PayPalGiver {

    private final Storage storage;

    @Autowired
    public PayPalGiver(@Qualifier("siteStorage") Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        System.out.println("Welcome to PayPal System!");
        return storage;
    }

}
