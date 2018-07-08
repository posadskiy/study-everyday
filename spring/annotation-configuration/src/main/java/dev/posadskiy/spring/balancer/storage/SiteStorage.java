package dev.posadskiy.spring.balancer.storage;

import java.math.BigInteger;

public class SiteStorage implements Storage {
    @Override
    public BigInteger getAmount() {
        System.out.println("Отправляю запрос на сайт");
        System.out.println("Обработка...");
        System.out.println("Ответ получен!");
        return BigInteger.TEN;
    }

    @Override
    public void addValue(BigInteger value) {
        System.out.println("Отправляю запрос на сайт");
        System.out.println("В данный момент пополнение счета невозможно");
    }

    @Override
    public void withdraw(BigInteger value) {
        System.out.println("Отправляю запрос на сайт");
        System.out.println("Согласно условиям системы, снятие СайтКоинов не допускается");
    }
}
