package dev.posadskiy.spring.balancer.storage;

import java.math.BigInteger;

public class BankStorage implements Storage {
    @Override
    public BigInteger getAmount() {
        System.out.println("Отправляю запрос в Банк");
        System.out.println("Ответ получен");
        System.out.println("Для более подробной информации обратитесь в ближайшее отделение");
        return BigInteger.ONE;
    }

    @Override
    public void addValue(BigInteger value) {
        System.out.println("Отправляю запрос в Банк");
        System.out.println("Баланс пополнен");
        System.out.println("Не забудьте о предстоящем списании по кредиту");
        System.out.println("Не забудьте о предстоящем списании по ипотеке");
    }

    @Override
    public void withdraw(BigInteger value) {
        System.out.println("Отправляю запрос в Банк");
        System.out.println("Вы успешно сняли сумму денег");
        System.out.println("Комиссия составила 10%");
        System.out.println("Спасибо, что вы с нами!");
    }
}
