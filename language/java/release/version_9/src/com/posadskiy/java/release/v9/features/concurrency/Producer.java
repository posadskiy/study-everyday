package com.posadskiy.java.release.v9.features.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscriber;

class Producer implements Flow.Publisher<Integer> {

    List<Subscriber<? super Integer>> subscriberList = new ArrayList<>();

    public void createProduct() {
        final Product product = new Product(new Random().nextInt());

        subscriberList.forEach(subscriber -> subscriber.onSubscribe(product));
    }

    @Override
    public void subscribe(Subscriber<? super Integer> subscriber) {
        subscriberList.add(subscriber);
    }
}
