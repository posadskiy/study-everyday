package dev.posadskiy.spring.balancer.storage;

import java.math.BigInteger;

public class LocalStorage implements Storage {
    @Override
    public BigInteger getAmount() {
        System.out.println("Вы очень богаты. Мысленно");
        return BigInteger.valueOf(10000);
    }

    @Override
    public void addValue(BigInteger value) {
        System.out.println("Добавлено, сколько пожелали");
    }

    @Override
    public void withdraw(BigInteger value) {
        System.out.println("Снято. Но на балансе практически не отразилось");
    }
}
