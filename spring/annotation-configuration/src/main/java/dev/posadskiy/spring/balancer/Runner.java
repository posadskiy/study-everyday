package dev.posadskiy.spring.balancer;

import dev.posadskiy.spring.balancer.giver.PayPalGiver;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        PayPalGiver payPalGiver = (PayPalGiver) context.getBean("payPalGiver");
        System.out.println(payPalGiver.getStorage().getAmount());

    }
}
