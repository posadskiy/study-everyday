package com.posadskiy.java.release.v9.features.concurrency;

import java.lang.System.Logger.Level;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

class Consumer implements Flow.Subscriber<Integer> {

    @Override
    public void onSubscribe(Subscription subscription) {
        final Product product = (Product) subscription;
        System.getLogger("default").log(Level.INFO, product.getValue());
    }

    @Override
    public void onNext(Integer item) {
        System.getLogger("default").log(Level.INFO, item);
    }

    @Override
    public void onError(Throwable throwable) {
        System.getLogger("default").log(Level.ERROR, this.getClass().getName() + " thrown error in the process");
    }

    @Override
    public void onComplete() {
        System.getLogger("default").log(Level.INFO, this.getClass().getName() + " completed receiving");
    }
}
